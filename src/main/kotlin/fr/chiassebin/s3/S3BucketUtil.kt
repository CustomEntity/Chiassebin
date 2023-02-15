package fr.chiassebin.s3

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.ByteStream
import aws.smithy.kotlin.runtime.content.toByteArray
import aws.smithy.kotlin.runtime.http.Url
import fr.chiassebin.config.Config
import java.io.File
import javax.net.ssl.HttpsURLConnection


class S3BucketUtil {
    companion object {

        suspend fun pushObject(bucketName: String, keyName: String, content: String) {
            val request = PutObjectRequest {
                bucket = bucketName
                key = keyName
                body = ByteStream.fromBytes(content.toByteArray())
            }

            S3Client {
                endpointUrl = Url.parse(Config.AWS_ENDPOINT)
                region = Config.AWS_REGION
            }.use { client ->
                val response = client.putObject(request);
                println(response.eTag)
            }
        }

        suspend fun getObjectBytes(bucketName: String, keyName: String): String {
            var content = ""
            val request = GetObjectRequest {
                key = keyName
                bucket = bucketName
            }
            S3Client {
                endpointUrl = Url.parse(Config.AWS_ENDPOINT)
                region = Config.AWS_REGION
            }.use { client ->
                client.getObject(request) { resp ->
                    content = resp.body?.toByteArray()?.let { String(it) } ?: ""
                }
            }
            return content
        }
    }
}