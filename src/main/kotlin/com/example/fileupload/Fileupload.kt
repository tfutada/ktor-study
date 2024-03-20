package com.example.fileupload

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import java.io.*

fun main() {
    runBlocking {
        val client = HttpClient(CIO)

        val response: HttpResponse = client.submitFormWithBinaryData(
            url = "http://localhost:8080/upload",
            formData = formData {
                append("description", "Ktor logo")
                append("image", File("image.jpg").readBytes(), Headers.build {
                    append(HttpHeaders.ContentType, "image/jpg")
                    append(HttpHeaders.ContentDisposition, "filename=\"image.jpg\"")
                })
            }
        )

        println(response.bodyAsText())
    }
}