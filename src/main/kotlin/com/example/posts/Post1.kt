package com.example.posts


import io.ktor.client.*
import io.ktor.client.engine.cio.*
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
    }

    val response: HttpResponse = client.post("https://jsonplaceholder.typicode.com/posts") {
        contentType(ContentType.Application.Json)
        setBody(Post("foo", "bar", 1))
    }

    println(response.bodyAsText())
}



