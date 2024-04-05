package com.example.udpclient

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

fun main() {
    try {
        val serverAddress = InetAddress.getByName("::1")
        val serverPort = 5106

        val data = "Hello, UDP Server!".toByteArray()
        DatagramSocket().use { clientSocket ->
            val packet = DatagramPacket(data, data.size, serverAddress, serverPort)
            clientSocket.send(packet)
            // Socket is automatically closed
        }
    } catch (e: Exception) {
        e.printStackTrace() // Or handle the exception as appropriate
    }
}
