package com.briefbytes.remoteplayer.videoplayer;

import java.io.File;
import java.io.IOException;

public interface VideoPlayer {

    void play(File video) throws IOException;

    void toggleSubtitles();

    void togglePause();

    void quit();

}
