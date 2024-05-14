package com.example.plugins

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.delay
import java.io.File

val GOFR = System.getenv("GOFR")!!

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World! 888")
        }
        get("/delay") {
            delay(3000L)
            call.respondText("Hello World!999111")
        }

        get("/gofr") {
            val client = HttpClient(CIO)
            val response: HttpResponse = client.get(GOFR)

            // Check if the response status is successful
            if (response.status == HttpStatusCode.OK) {
                // Get the response content as String
                val responseBody = response.bodyAsText()

                // Return the response body directly to the client
                call.respondText(responseBody, ContentType.Application.Json)
            } else {
                // Handle non-OK responses here
                call.respondText(
                    "Failed to fetch data: ${response.status.description}",
                    status = HttpStatusCode.BadGateway
                )
            }
        }

        var fileDescription = ""
        var fileName = ""

        // post
        post("/corda") {
            call.respondText("done")
        }

        post("/upload") {
            val contentLength = call.request.header(HttpHeaders.ContentLength)
            if (contentLength == null) {
                call.respondText("No file uploaded")
                return@post
            }

            val multipartData = call.receiveMultipart()

            multipartData.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        fileDescription = part.value
                    }

                    is PartData.FileItem -> {
                        fileName = part.originalFileName as String
                        println("File is uploaded: $fileName")
                        val file = File("uploads/$fileName")

                        if (false) {
                            val fileBytes = part.streamProvider().readBytes()
                            file.writeBytes(fileBytes)
                        } else {
                            // use InputStream from part to save file
                            part.streamProvider().use { its ->
                                // copy the stream to the file with buffering
                                file.outputStream().buffered().use {
                                    // note that this is blocking
                                    its.copyTo(it)
                                }
                            }
                        }
                    }

                    else -> {}
                }
                part.dispose()
            }

            call.respondText("$fileDescription is uploaded to 'uploads/$fileName'")
        }

        rateLimit(RateLimitName("protected")) {
            get("/limit") {
                call.respondText("OK")
            }
        }
    }
}
