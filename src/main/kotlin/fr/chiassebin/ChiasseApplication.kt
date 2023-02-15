package fr.chiassebin

import fr.chiassebin.api.domain.entity.PostPaste
import fr.chiassebin.injection.AppModule
import fr.chiassebin.web.router
import fr.kettl.api.database.ScyllaGateway
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.routing.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

class ChiasseApplication : KoinComponent {

    fun start() {
        val scyllaDBGateway by inject<ScyllaGateway>()
        scyllaDBGateway.init()
        scyllaDBGateway.start()


    }
}

@OptIn(ExperimentalSerializationApi::class)
fun Application.module() {
    install(Koin) {
        modules(AppModule.appModule)
    }
    install(ContentNegotiation) {
        json()
    }
    val kettlApplication by inject<ChiasseApplication>()
    kettlApplication.start()

    routing {
        router()
    }
}
