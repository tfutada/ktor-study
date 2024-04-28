package com.example.myclientssl

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager


suspend fun main() {
    val client = client()
    val response: HttpResponse = client.get("https://localhost:8888/api/v1/flow/86F3F0502295/create-1")
    println(response.bodyAsText())
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