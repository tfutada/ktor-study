package com.example

import com.example.plugins.*
import io.ktor.server.application.*
import kotlin.time.Duration.Companion.seconds
import io.ktor.server.plugins.ratelimit.*

fun main(args: Array<String>) {
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
