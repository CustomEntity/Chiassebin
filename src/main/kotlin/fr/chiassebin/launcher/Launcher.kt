package fr.chiassebin.launcher

import fr.chiassebin.module
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin


import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

class Launcher {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
                .start(wait = true)
        }
    }
}