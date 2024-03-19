package jp.tf.jp.tf.coroutine1

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*


fun main() = runBlocking {
    val NUM_COROUTINES = 1000

    val client = HttpClient(CIO) {
        engine {
            endpoint.maxConnectionsPerRoute = NUM_COROUTINES
        }
    }
    repeat(NUM_COROUTINES) { // launch a lot of coroutines
        launch {
//            delay(5000L)
            val response: HttpResponse = client.get("http://localhost:8080/delay")
            print(".")
        }
    }

    delay(6000L)
    client.close()
}

