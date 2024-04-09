package com.example.postsJava

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

@Serializable
data class Post(val title: String, val body: String, val userId: Int)

// https://ktor.io/docs/proxy.html#proxy_auth
// https://ktor.io/docs/http-client-engines.html#java
suspend fun main() {
    // use Java instead of CIO
    val client = HttpClient(Java) {
        engine {
            pipelining = true
//            proxy = ProxyBuilder.http("http://proxy-server.com/")
            protocolVersion = java.net.http.HttpClient.Version.HTTP_1_1
        }
        install(ContentNegotiation) {
            json()
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
