package com.briefbytes.remoteplayer.server;

import com.briefbytes.remoteplayer.core.Message;
import com.briefbytes.remoteplayer.core.MessageType;
import com.briefbytes.remoteplayer.core.UdpMessageHandler;
import com.briefbytes.remoteplayer.utils.ByteUtils;
import com.briefbytes.remoteplayer.utils.FileUtils;
import com.briefbytes.remoteplayer.utils.OsUtils;
import com.briefbytes.remoteplayer.utils.Pair;
import com.briefbytes.remoteplayer.videoplayer.VideoPlayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Scanner;

/**
 * Server listens to UDP requests and executes video player actions
 */
public class Server {

    private static final int PORT = 9999;

    private UdpMessageHandler udpMessageHandler;
    private VideoPlayer videoPlayer;
    private File videosDirectory;
    private String serverName;

    protected Server(VideoPlayer videoPlayer, File videosDirectory, String serverName) throws SocketException {
        if (videoPlayer == null || videosDirectory == null || serverName == null) {
            throw new IllegalArgumentException("Null values not allowed");
        }
        if (serverName.trim().isEmpty()) {
            throw new IllegalArgumentException("Server name cannot be empty");
        }
        if (!videosDirectory.isDirectory()) {
            throw new IllegalArgumentException("Invalid videos directory");
        }
        this.udpMessageHandler = new UdpMessageHandler(new DatagramSocket(PORT));
        this.videoPlayer = videoPlayer;
        this.videosDirectory = videosDirectory;
        this.serverName = serverName;
    }

    public void run() {
        boolean b = true;
        while (b) {
            try {
                Pair<Message, InetSocketAddress> pair = udpMessageHandler.receive();
                Message message = pair.getKey();
                InetSocketAddress socketAddress = pair.getValue();
                System.out.println(message);
                switch (message.getType()) {
                    case BCAST:
                        bcastResponse(socketAddress);
                        break;
                    case LIST:
                        listResponse(socketAddress);
                        break;
                    case PLAY:
                        File video = new File(videosDirectory, ByteUtils.bytesToString(message.getContent()));
                        System.out.println("Video: " + video.getAbsolutePath());
                        if (video.isFile()) {
                            videoPlayer.play(video);
                        }
                        break;
                    case SUBTITLES:
                        videoPlayer.toggleSubtitles();

                        break;
                    case PAUSE:
                        videoPlayer.togglePause();
                        break;
                    case QUIT:
                        videoPlayer.quit();
                        break;
                    case SHUTDOWN:
                        videoPlayer.quit();
                        System.out.println("Shutting down " + OsUtils.os + " computer in 1 minute");
                        OsUtils.shutdown();
                        b = false;
                        break;
                    default:
                        System.out.println("Invalid message type");
                        break;
                }
            } catch (IOException e) {
                System.out.println("I/O error\n" + e.getMessage());
            }
        }
        udpMessageHandler.close();
    }

    private void bcastResponse(InetSocketAddress socketAddress) throws IOException {
        udpMessageHandler.send(socketAddress, new Message(MessageType.BCAST_RESPONSE, ByteUtils.stringToBytes(serverName)));
    }

    private void listResponse(InetSocketAddress socketAddress) throws IOException {
        int beginIndex = videosDirectory.getPath().length() + 1;
        for (File file : FileUtils.listFiles(videosDirectory)) {
            if (!"srt".equals(FileUtils.extension(file))) {
                String path = file.getPath().substring(beginIndex);
                long size = file.length();
                byte[] content = ByteUtils.join(ByteUtils.longToBytes(size), ByteUtils.stringToBytes(path));
                udpMessageHandler.send(socketAddress, new Message(MessageType.LIST_RESPONSE, content));
            }
        }
    }

    public static Server createServer(String configPath) throws SocketException, FileNotFoundException {
        ServerBuilder serverBuilder = new ServerBuilder();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(configPath));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.startsWith("video_player=")) {
                    serverBuilder = serverBuilder.withVideoPlayer(line.substring(line.indexOf('=') + 1));
                } else if (line.startsWith("videos_directory=")) {
                    serverBuilder = serverBuilder.withVideosDirectory(line.substring(line.indexOf('=') + 1));
                } else if (line.startsWith("server_name=")) {
                    serverBuilder = serverBuilder.withServerName(line.substring(line.indexOf('=') + 1));
                } else {
                    System.out.println("Invalid config line: " + line);
                }
            }
        } finally {
            if (scanner != null) scanner.close();
        }
        return serverBuilder.build();
    }

}

