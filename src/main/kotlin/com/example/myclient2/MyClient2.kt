package com.example.myclient

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

suspend fun main() {
    val client = HttpClient(CIO)
    for (i in 1..10) {
        val response: HttpResponse = client.get("https://ktor.io/")
        println(response.bodyAsText())
    }
    client.close()
}

