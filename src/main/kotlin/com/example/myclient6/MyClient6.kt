package com.example.myclient6

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*
import kotlinx.coroutines.async

fun main() = runBlocking {
    val client = HttpClient(CIO)
    val requestCount = 3 // Number of times to repeat the request
    val url = "http://localhost:8080/"

    val requests = List(requestCount) {
        async { client.get(url).bodyAsText() }
    }

    requests.forEach { deferred ->
        println(deferred.await())
    }

    client.close()
}
