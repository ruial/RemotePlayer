package com.briefbytes.remoteplayer.videoplayer;

import com.briefbytes.remoteplayer.utils.OsUtils;

import java.io.*;

/**
 * OMXPlayer is controlled by writing to the stdin
 */
public class OmxPlayer implements VideoPlayer {

    private Writer writer;

    protected OmxPlayer() {
    }

    @Override
    public void play(File video) throws IOException {
        quit();
        Process process = OsUtils.exec("omxplayer", "-b", "-o", "hdmi", video.getAbsolutePath());
        OutputStream stdin = process.getOutputStream();
        writer = new BufferedWriter(new OutputStreamWriter(stdin));
    }

    @Override
    public void toggleSubtitles() {
        write('s');
    }

    @Override
    public void togglePause() {
        write('p');
    }

    @Override
    public void quit() {
        write('q');
        if (writer != null) {
            try {
                writer.close();
                writer = null;
            } catch (IOException e) {
                System.out.println("Error closing stream. " + e.getMessage());
            }
        }
    }

    private void write(char c) {
        if (writer != null) {
            try {
                writer.write(c);
                writer.flush();
            } catch (IOException e) {
                System.out.println("Error writing to stream. " + e.getMessage());
            }
        }
    }

}