package com.example.myclient

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

suspend fun main() {
    val client = HttpClient(CIO)
    val response: HttpResponse = client.get("http://localhost:8080")
    println(response.bodyAsText().substring(0, response.contentLength()?.toInt() ?: 0))
}

