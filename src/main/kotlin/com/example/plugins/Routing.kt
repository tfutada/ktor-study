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

val GOFR: String = System.getenv("GOFR") ?: run {
    System.err.println("Error: Environment variable 'GOFR' is not set.")
    System.exit(1)
    "" // This is a dummy return to satisfy the type system; it will never be reached because of System.exit(1)
}

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World! 888")
        }
        get("/delay") {
            // Get the sleep time from the query parameter
            val sleepTime = call.parameters["time"]?.toLongOrNull() ?: 3000L
            delay(sleepTime)
            call.respondText("slept for $sleepTime")
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
