package com.example

import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlin.time.Duration.Companion.seconds
import io.ktor.server.plugins.ratelimit.*

// you can run this main function to start the server
// or you can run the server by running the gradle task `run`
// NB. in gradle case, the main function is not called.
fun main(args: Array<String>) {
    println("starting from the main function directly. you won't see this message if you run the server using gradle task `run`. ")

    val server = embeddedServer(Netty, port = 8080, module = Application::module)
    server.start(wait = false)

    Runtime.getRuntime().addShutdownHook(Thread {
        println("Shutdown signal received, stopping server...")
        server.stop(gracePeriodMillis = 1000, timeoutMillis = 3000)
        println("Server stopped gracefully.")
    })

    Thread.currentThread().join()
}

fun Application.module() {
    install(RateLimit) {
        register(RateLimitName("protected")) {
            rateLimiter(limit = 5, refillPeriod = 60.seconds)
        }
    }

    configureRouting()
}
