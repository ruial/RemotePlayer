package com.briefbytes.remoteplayer.client.action;

import com.briefbytes.remoteplayer.client.Client;
import com.briefbytes.remoteplayer.client.VideoFile;

import java.io.IOException;

public class PlayerActionShutdown implements PlayerAction {
    @Override
    public void execute(Client client, VideoFile videoFile) throws IOException {
        if (client != null) client.shutdown();
    }

    @Override
    public String toString() {
        return "Shutdown";
    }
}