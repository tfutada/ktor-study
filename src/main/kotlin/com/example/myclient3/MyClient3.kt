package jp.tf.jp.tf.coroutine3

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*


fun main() = runBlocking {
    val NUM_COROUTINES = 1000
    val TIMEOUT = 60_000L

    val client = HttpClient(CIO) {
//        engine {
//            endpoint.maxConnectionsPerRoute = NUM_COROUTINES
//        }
        install(HttpTimeout) {
            connectTimeoutMillis = TIMEOUT // Increase connection timeout to 30 seconds
            // Other timeout settings:
            requestTimeoutMillis = TIMEOUT
            socketTimeoutMillis = TIMEOUT
        }
    }

    repeat(NUM_COROUTINES) { // launch a lot of coroutines
        launch {
            val response: HttpResponse = client.get("http://localhost:8081/") {
                timeout {
                    requestTimeoutMillis = TIMEOUT // 30 seconds
                }
            }
            print(".")
        }
    }

//    delay(TIMEOUT + 1000L) // Wait for all coroutines to finish
    delay(10_000L) // Wait for all coroutines to finish
    client.close()
}

