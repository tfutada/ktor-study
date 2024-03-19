package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.delay

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!999111")
        }
        get("/delay") {
            delay(3000L)
            call.respondText("Hello World!999111")
        }
    }
}
