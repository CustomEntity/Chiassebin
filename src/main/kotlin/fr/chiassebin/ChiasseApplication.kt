package fr.chiassebin

import fr.chiassebin.injection.AppModule
import fr.chiassebin.web.router
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

class ChiasseApplication {
    fun start() {

    }
}

fun Application.module() {
    install(Koin) {
        modules(AppModule.appModule)
    }
    val kettlApplication by inject<ChiasseApplication>()
    kettlApplication.start()

    routing {
        router()
    }
}
