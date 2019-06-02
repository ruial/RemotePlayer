package com.briefbytes.remoteplayer.videoplayer;

import com.briefbytes.remoteplayer.utils.OsUtils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

/**
 * VLC is controlled using the Robot class to simulate keyboard actions
 */
public class VlcPlayer implements VideoPlayer {

    private Robot robot;
    private Process process;

    protected VlcPlayer(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void play(File video) throws IOException {
        quit();
        process = OsUtils.exec("vlc", "-f", video.getAbsolutePath());
    }

    @Override
    public void toggleSubtitles() {
        keyPress(KeyEvent.VK_V);
    }

    @Override
    public void togglePause() {
        keyPress(KeyEvent.VK_SPACE);
    }

    @Override
    public void quit() {
        if (process != null) {
            process.destroy();
            process = null;
        }
    }

    private void keyPress(int key) {
        if (robot != null && process != null) {
            robot.keyPress(key);
            robot.delay(100);
            robot.keyRelease(key);
        }
    }

}
