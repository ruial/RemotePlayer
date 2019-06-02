package com.briefbytes.remoteplayer.client.action;

import com.briefbytes.remoteplayer.client.Client;
import com.briefbytes.remoteplayer.client.VideoFile;

import java.io.IOException;

public interface PlayerAction {

    void execute(Client client, VideoFile videoFile) throws IOException;

}
