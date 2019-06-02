package com.briefbytes.remoteplayer.videoplayer;

import java.awt.*;

public class VideoPlayerFactory {

    public VideoPlayer videoPlayer(String videoPlayerName) {
        videoPlayerName = videoPlayerName.toLowerCase();
        if (videoPlayerName.startsWith("omx")) {
            return new OmxPlayer();
        }
        if (videoPlayerName.startsWith("vlc")) {
            Robot robot = null;
            try {
                robot = new Robot();
            } catch (AWTException e) {
                System.out.println("Toggle Subtitles and Pause with VLC does not work in headless environment.");
                System.out.println("If running via ssh, execute: export DISPLAY=:0");
            }
            return new VlcPlayer(robot);
        }
        throw new IllegalArgumentException("Unsupported video player");
    }

}
