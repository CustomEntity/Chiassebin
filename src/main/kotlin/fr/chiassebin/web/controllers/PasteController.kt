package fr.chiassebin.web.controllers

import fr.chiassebin.web.responses.BadResponse
import fr.chiassebin.web.responses.FineResponse
import fr.chiassebin.api.domain.dao.PasteDao
import fr.chiassebin.api.domain.entity.Paste
import fr.chiassebin.api.domain.entity.PostPaste
import fr.chiassebin.s3.S3BucketUtil
import fr.chiassebin.util.IdGenerator
import fr.kettl.identity.SnowflakeIdGenerator
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.util.*

class PasteController : KoinComponent {

    private val pasteDao: PasteDao by inject()

    suspend fun findPasteInfoById(call: ApplicationCall) {
        val id = call.parameters["id"];

        if (id.isNullOrEmpty()) {
            call.respond(HttpStatusCode.BadRequest, "Missing id")
        }
        val paste = pasteDao.findById(id.toString()).await()
        if (paste == null) {
            call.respond(
                HttpStatusCode.NotFound, BadResponse(
                    code = 404, message = "Paste not found"
                )
            )
        } else {
            call.respond(
                HttpStatusCode.OK, FineResponse(
                    code = 200, data = paste
                )
            )
        }
    }

    suspend fun getPasteContentById(call: ApplicationCall) {
        val id = call.parameters["id"];

        if (id.isNullOrEmpty()) {
            call.respond(HttpStatusCode.BadRequest, "Missing id")
        }
        val paste = pasteDao.findById(id.toString()).await()
        if (paste == null) {
            call.respond(
                HttpStatusCode.NotFound, BadResponse(
                    code = 404, message = "Paste not found"
                )
            )
            return
        }
        if (paste.bucketKey == null) {
            call.respond(
                HttpStatusCode.InternalServerError, BadResponse(
                    code = 500, message = "Bucket key is null"
                )
            )
            return
        }
        try {
            call.respond(
                HttpStatusCode.OK, FineResponse(
                    code = 200,
                    data = S3BucketUtil.getObjectBytes(S3BucketUtil.BUCKET_NAME, paste.bucketKey)
                )
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError, BadResponse(
                    code = 500, message = "Error while getting paste content: " + e.stackTraceToString()
                )
            )
        }
    }

    suspend fun postPaste(call: ApplicationCall) {
        val paste: PostPaste? = try {
            call.receiveNullable<PostPaste>()
        } catch (e: Exception) {
            e.printStackTrace()
            return call.respond(
                HttpStatusCode.BadRequest, BadResponse(
                    code = 400, message = "Invalid paste"
                )
            )
        }
        when {
            paste == null -> call.respond(HttpStatusCode.BadRequest, BadResponse(code = 400, message = "Missing paste"))
            paste.content.isNullOrEmpty() -> call.respond(
                HttpStatusCode.BadRequest, BadResponse(
                    code = 400, message = "Missing content"
                )
            )

            else -> {
                val snowflakeIdGenerator: SnowflakeIdGenerator by inject()

                val id = IdGenerator.generateId(5)
                var finalPaste = Paste(
                    id,
                    paste.fileName,
                    paste.content.toByteArray().size / 1000,
                    id + "-" + paste.fileName,
                    paste.password,
                    paste.syntaxHighlight,
                    paste.tags,
                    Date().toInstant().toEpochMilli(),
                    paste.expiresAt)

                var tryTime = 0
                while (pasteDao.exists(finalPaste.id).await()) {
                    if (tryTime++ > 10) {
                        call.respond(
                            HttpStatusCode.InternalServerError, BadResponse(
                                code = 500, message = "Internal Server Error: Too many tries"
                            )
                        )
                        return
                    }
                    finalPaste = finalPaste.copy(id = IdGenerator.generateId(5), bucketKey = finalPaste.bucketKey + "-" + IdGenerator.generateId(5))
                }
                this.pasteDao.save(finalPaste).await()
                if (finalPaste.bucketKey.isNullOrEmpty()) {
                    call.respond(
                        HttpStatusCode.BadRequest, BadResponse(
                            code = 500, message = "Internal Server Error: Bucket Key is empty"
                        )
                    )
                    return
                }
                S3BucketUtil.pushObject(S3BucketUtil.BUCKET_NAME, finalPaste.bucketKey!!, paste.content)

                call.respond(
                    HttpStatusCode.OK, FineResponse(
                        code = 200,
                        data = finalPaste
                    )
                )
            }
        }
    }
}