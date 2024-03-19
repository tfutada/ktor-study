package jp.tf.jp.tf.coroutine1

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*


fun main() = runBlocking {
    val client = HttpClient(CIO)

    repeat(10) { // launch a lot of coroutines
        launch {
//            delay(5000L)
            val response: HttpResponse = client.get("http://localhost:8080")
            print(".")
        }
    }
}
