package com.example.myclient6

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*
import kotlinx.coroutines.async

fun main() = runBlocking {
    val TIMEOUT = 60_000L

    val client = HttpClient(CIO) {
//        engine {
//            endpoint.maxConnectionsPerRoute = NUM_COROUTINES
//        }
        install(HttpTimeout) {
            connectTimeoutMillis = TIMEOUT // Increase connection timeout to 30 seconds
            // Other timeout settings:
//            requestTimeoutMillis = TIMEOUT
//            socketTimeoutMillis = TIMEOUT
        }
    }

    val requestCount = 1000 // Number of times to repeat the request
    val url = "http://localhost:8080/delay"

    val requests = List(requestCount) {
        async { client.get(url).bodyAsText() }
    }

    requests.forEach { deferred ->
        println(deferred.await())
    }

    client.close()
}
