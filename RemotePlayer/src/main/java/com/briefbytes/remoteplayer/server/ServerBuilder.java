package com.briefbytes.remoteplayer.server;

import com.briefbytes.remoteplayer.videoplayer.VideoPlayer;
import com.briefbytes.remoteplayer.videoplayer.VideoPlayerFactory;

import java.io.File;
import java.net.SocketException;

public class ServerBuilder {

    private VideoPlayer videoPlayer;
    private File videosDirectory;
    private String serverName;

    public ServerBuilder withVideoPlayer(String videoPlayer) {
        VideoPlayerFactory factory = new VideoPlayerFactory();
        this.videoPlayer = factory.videoPlayer(videoPlayer);
        return this;
    }

    public ServerBuilder withVideosDirectory(String videosDirectory) {
        this.videosDirectory = new File(videosDirectory);
        return this;
    }

    public ServerBuilder withServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

    public Server build() throws SocketException {
        return new Server(videoPlayer, videosDirectory, serverName);
    }

}
