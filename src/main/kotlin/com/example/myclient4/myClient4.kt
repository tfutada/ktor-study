package com.example.myclient4

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {

    val client = HttpClient(CIO)

    val firstRequest: Deferred<String> = async { client.get("http://localhost:8080/").bodyAsText() }
    val secondRequest: Deferred<String> = async { client.get("http://localhost:8080/").bodyAsText() }

    //
    val firstRequestContent = firstRequest.await()
    val secondRequestContent = secondRequest.await()

    println("firstRequestContent: $firstRequestContent")
    println("secondRequestContent: $secondRequestContent")

    client.close()
}
