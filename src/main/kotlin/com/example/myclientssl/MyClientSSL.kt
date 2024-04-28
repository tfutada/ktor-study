package com.example.myclientssl

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

@Serializable
data class ApiResponse(
    val holdingIdentityShortHash: String,
    val clientRequestId: String,
    val flowId: String,
    val flowStatus: String,
    val flowResult: String,
    val flowError: String?,
    val timestamp: String
)

// invoke a Corda endpoint via ssl and authenticate with basic auth
suspend fun main() {
    val client = client()
    val response: HttpResponse = client.get("https://localhost:8888/api/v1/flow/86F3F0502295/create-1")

    if (response.status.value in 200..299) {
        val body = response.body<ApiResponse>()
        println("Success: $body")
    } else {
        println("Failed with status code: ${response.status.value}")
    }
}

val trust = object : X509TrustManager {
    override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {}

    override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {}

    override fun getAcceptedIssuers(): Array<X509Certificate>? = null
}

fun client() = HttpClient(CIO) {
    engine {
        https {
            trustManager = trust
        }
    }

    install(ContentNegotiation) {
        json(
            Json {
                encodeDefaults = true
                ignoreUnknownKeys = true
                coerceInputValues = true
            },
        )
    }

    defaultRequest {
        val auth = System.getenv("CORDA_AUTH")!!
        header(HttpHeaders.Authorization, "Basic $auth")
    }
}