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
    val NUM_COROUTINES = 2000  // ulimit

    val client = HttpClient(CIO) {
        // !!! 最大同時リクエスト数。他社のサーバでやるとDoS攻撃になります！
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

    val requestCount = 2000 // Number of times to repeat the request
    val url = "http://localhost:8080/delay"

    val requests = List(requestCount) {
        async { client.get(url).bodyAsText() }
    }

//    delay(4000) // delay 1 second
    println("Waiting for all requests to complete...")
    println("Completed requests: ${requests.count { it.isCompleted }}")

    requests.forEach { deferred ->
        println(deferred.await())
    }

    client.close()
}
