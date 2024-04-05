package com.example.udp

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*


fun main() {
    println("Hello, UDP!")

    val selectorManager = SelectorManager(Dispatchers.IO)
    val serverSocket = aSocket(selectorManager).udp().bind(InetSocketAddress("::", 5106)) // 0.0.0.0 for IPv6
    println("Server is listening at ${serverSocket.localAddress}")

    // Use CoroutineScope to manage coroutines better. This scope is tied to the main function's lifecycle.
    CoroutineScope(Dispatchers.IO).launch {
        while (true) {
            // Instead of runBlocking, we just use the scope's launch to avoid blocking.
            // Each packet processing is offloaded to a new coroutine.
            val packet = serverSocket.receive()  // This is a suspending function and will not block the thread.

            launch {
                // This block is now executed in a separate coroutine for each packet,
                // allowing concurrent packet processing.
                val message = packet.packet.readUTF8Line()
                println("Received from ${packet.address}: $message")

                // optional: echo the message back
                serverSocket.send(Datagram(ByteReadPacket("Echo $message".encodeToByteArray()), packet.address))
            }
        }
    }

    println("Press Enter to exit")
    readln()  // Keep the main thread alive until an Enter is pressed.
}
