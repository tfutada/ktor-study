import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

fun main() {
    val serverAddress = InetAddress.getByName("::1")
    val serverPort = 5106

    val data = "Hello, UDP Server!".toByteArray()
    val clientSocket = DatagramSocket()
    val packet = DatagramPacket(data, data.size, serverAddress, serverPort)
    clientSocket.send(packet)
    clientSocket.close()
}

