package com.example.myclient5

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val client = HttpClient(CIO)

    // List of URLs for demonstration. Replace these with your actual URLs.
    val urls = listOf("http://localhost:8080/", "http://localhost:8080/", "http://localhost:8080/")

    // Launch concurrent requests and collect their Deferred objects
    val requests: List<Deferred<String>> = urls.map { url ->
        async { client.get(url).bodyAsText() }
    }

    // Await results and print them
    requests.forEachIndexed { index, deferred ->
        println("Response from request $index: ${deferred.await()}")
    }

    client.close()
}
