package com.example.fileupload2

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import java.io.File

fun main() {
    runBlocking {
        val client = HttpClient()
        val file = File("image.jpg")
        val url = "http://localhost:8080/upload"

        val response: HttpResponse = client.post(url) {
            contentType(ContentType.Application.OctetStream)
            setBody(object : OutgoingContent.WriteChannelContent() {
                override suspend fun writeTo(channel: ByteWriteChannel) {
                    file.inputStream().use { inputStream ->
                        val buffer = ByteArray(4096)
                        var bytes = inputStream.read(buffer)
                        while (bytes >= 0) {
                            channel.writeFully(buffer, 0, bytes)
                            bytes = inputStream.read(buffer)
                        }
                    }
                }
            })
        }

        if (response.status == HttpStatusCode.OK) {
            println("File uploaded successfully.")
        } else {
            println("Failed to upload file.")
        }

        client.close()
    }
}
