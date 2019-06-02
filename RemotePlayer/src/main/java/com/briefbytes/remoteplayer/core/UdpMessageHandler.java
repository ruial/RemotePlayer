package com.briefbytes.remoteplayer.core;

import com.briefbytes.remoteplayer.utils.ByteUtils;
import com.briefbytes.remoteplayer.utils.Pair;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * Class to handle receiving/sending messages using UDP protocol.
 * UDP was chosen because messages are small, order/error correction is not needed
 * and its broadcast capabilities are useful for network discovery.
 */
public class UdpMessageHandler {

    // udp buffer size, 1 byte for message type and up to 511 bytes for content
    private static final int BUFFER_SIZE = 512;

    private DatagramSocket udpSocket;

    public UdpMessageHandler(DatagramSocket udpSocket) {
        this.udpSocket = udpSocket;
    }

    public void close() {
        udpSocket.close();
    }

    public void send(InetSocketAddress socketAddress, Message message) throws IOException {
        byte[] content = message.getContent();
        if (BUFFER_SIZE <= content.length) {
            return; // Max content size exceeded, do not send message
        }
        byte type = (byte) message.getType().ordinal();
        byte[] buffer = ByteUtils.addByte(type, 0, content);
        DatagramPacket udpPacket = new DatagramPacket(buffer, buffer.length, socketAddress);
        udpSocket.send(udpPacket);
    }

    public Pair<Message, InetSocketAddress> receive() throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket udpPacket = new DatagramPacket(buffer, buffer.length);
        udpSocket.receive(udpPacket);
        int length = udpPacket.getLength();
        MessageType messageType = MessageType.fromInt(buffer[0]);
        byte[] content = ByteUtils.copy(buffer, 1, length);
        InetSocketAddress socketAddress = new InetSocketAddress(udpPacket.getAddress(), udpPacket.getPort());
        return new Pair<Message, InetSocketAddress>(new Message(messageType, content), socketAddress);
    }

}
