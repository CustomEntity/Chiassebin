package fr.chiassebin.web

import fr.chiassebin.web.controllers.UserController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Routing.router() {
    authRoute()
    userRoute()
}

fun Routing.authRoute() {
    val userController by inject<UserController>()

    route("/auth") {
        get {
            userController.test()
            call.respondText("Hello World!", ContentType.Text.Plain)
        }
    }
}

fun Routing.userRoute() {
    val userController by inject<UserController>()

    route("/user") {
        get {
            userController.test()
            call.respondText("Hello World!", ContentType.Text.Plain)
        }
    }
}
