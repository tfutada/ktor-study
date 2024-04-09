package com.example.posts

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable

@Serializable
data class Post(val title: String, val body: String, val userId: Int)

suspend fun main() {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
        install(HttpTimeout) {
            connectTimeoutMillis = 10_000
            requestTimeoutMillis = 10_000
        }
    }

    try {
        val response: HttpResponse = client.post("https://jsonplaceholder.typicode.com/posts") {
            headers {
                append(HttpHeaders.Authorization, "abc123")
                append(HttpHeaders.UserAgent, "ktor client")
            }
            contentType(ContentType.Application.Json)
            setBody(Post("foo", "bar", 1))
        }

        if (response.status.value in 200..299) {
            println("Success: ${response.bodyAsText()}")
        } else {
            println("Failed with status code: ${response.status.value}")
        }
    } catch (e: Exception) {
        println("An error occurred: ${e.message}")
        e.printStackTrace() // This will dump the stack trace to standard error stream
    } finally {
        client.close()
    }
}
