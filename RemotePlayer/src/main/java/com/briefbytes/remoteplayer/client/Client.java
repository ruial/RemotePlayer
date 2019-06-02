package com.briefbytes.remoteplayer.client;

import com.briefbytes.remoteplayer.core.Message;
import com.briefbytes.remoteplayer.core.MessageType;
import com.briefbytes.remoteplayer.core.UdpMessageHandler;
import com.briefbytes.remoteplayer.utils.ByteUtils;
import com.briefbytes.remoteplayer.utils.Pair;

import java.io.IOException;
import java.net.*;
import java.util.*;

/**
 * Client uses UDP broadcast to find devices and then is used to send messages to 1 address
 * Broadcast requires IPv4 but everything else is compatible with IPv6 (multicast could be an alternative)
 */
public class Client {

    private static final int PORT = 9999;
    private static final int TIMEOUT = 1000; // 1 second
    private static final String BCAST_ADDRESS = "255.255.255.255";

    private InetSocketAddress socketAddress;
    private UdpMessageHandler udpMessageHandler;

    public Client(String address) throws SocketException {
        socketAddress = new InetSocketAddress(address, PORT);
        DatagramSocket udpSocket = new DatagramSocket();
        udpSocket.setBroadcast(true);
        udpSocket.setSoTimeout(TIMEOUT);
        udpMessageHandler = new UdpMessageHandler(udpSocket);
    }

    public Client() throws SocketException {
        this(BCAST_ADDRESS);
    }

    public void close() {
        udpMessageHandler.close();
    }

    public void updateAddress(InetAddress address) {
        socketAddress = new InetSocketAddress(address, PORT);
    }

    public List<AddressInfo> findAddresses() throws IOException {
        List<AddressInfo> addresses = new ArrayList<AddressInfo>();
        udpMessageHandler.send(socketAddress, new Message(MessageType.BCAST));
        try {
            while (true) {
                Pair<Message, InetSocketAddress> pair = udpMessageHandler.receive();
                Message message = pair.getKey();
                InetAddress address = pair.getValue().getAddress();
                if (MessageType.BCAST_RESPONSE.equals(message.getType())) {
                    addresses.add(new AddressInfo(address, ByteUtils.bytesToString(message.getContent())));
                }
            }
        } catch (SocketTimeoutException e) {
            // keep receiving messages until timeout
        }
        Collections.sort(addresses);
        return addresses;
    }

    public List<VideoFile> requestFiles() throws IOException {
        List<VideoFile> files = new ArrayList<VideoFile>();
        udpMessageHandler.send(socketAddress, new Message(MessageType.LIST));
        try {
            while (true) {
                Pair<Message, InetSocketAddress> pair = udpMessageHandler.receive();
                Message message = pair.getKey();
                InetAddress address = pair.getValue().getAddress();
                if (MessageType.LIST_RESPONSE.equals(message.getType()) && address.equals(socketAddress.getAddress())) {
                    byte[] content = message.getContent();
                    long size = ByteUtils.bytesToLong(content);
                    String path = ByteUtils.bytesToString(ByteUtils.copy(content, ByteUtils.LONG_BYTES, content.length));
                    files.add(new VideoFile(path, size));
                }
            }
        } catch (SocketTimeoutException e) {
            // keep receiving messages until timeout
        }
        Collections.sort(files, new Comparator<VideoFile>() {
            @Override
            public int compare(VideoFile a, VideoFile b) {
                return a.getPath().compareTo(b.getPath());
            }
        });
        return files;
    }

    public void play(String file) throws IOException {
        udpMessageHandler.send(socketAddress, new Message(MessageType.PLAY, ByteUtils.stringToBytes(file)));
    }

    public void toggleSubtitles() throws IOException {
        udpMessageHandler.send(socketAddress, new Message(MessageType.SUBTITLES));
    }

    public void pause() throws IOException {
        udpMessageHandler.send(socketAddress, new Message(MessageType.PAUSE));
    }

    public void quit() throws IOException {
        udpMessageHandler.send(socketAddress, new Message(MessageType.QUIT));
    }

    public void shutdown() throws IOException {
        udpMessageHandler.send(socketAddress, new Message(MessageType.SHUTDOWN));
    }

    // provide a way to select an available broadcast address because windows may have problems with the generic one
    public static List<InetAddress> findBroadcastAddresses() throws SocketException {
        List<InetAddress> broadcastAddresses = new ArrayList<InetAddress>();
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();
            for (InterfaceAddress interfaceAddress : interfaceAddresses) {
                InetAddress broadcastAddress = interfaceAddress.getBroadcast();
                if (broadcastAddress != null) {
                    broadcastAddresses.add(broadcastAddress);
                }
            }
        }
        return broadcastAddresses;
    }

}
