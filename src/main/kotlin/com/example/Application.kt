package com.example

import com.example.plugins.*
import io.ktor.server.application.*
import kotlin.time.Duration.Companion.seconds
import io.ktor.server.plugins.ratelimit.*

// you can run this main function to start the server
// or you can run the server by running the gradle task `run`
// NB. in gradle case, the main function is not called.
fun main(args: Array<String>) {
    println("starting from the main function directly. you won't see this message if you run the server using gradle task `run`. ")
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(RateLimit) {
        register(RateLimitName("protected")) {
            rateLimiter(limit = 5, refillPeriod = 60.seconds)
        }
    }

    configureRouting()
}
