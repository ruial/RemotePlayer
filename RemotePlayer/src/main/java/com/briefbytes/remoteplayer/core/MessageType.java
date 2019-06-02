package com.briefbytes.remoteplayer.core;

/**
 * Message Types
 * BCAST - Client sends a broadcast message with 1 byte to get available devices
 * BCAST_RESPONSE - Server replies with the name
 * LIST - Client sends a request to list files
 * LIST_RESPONSE - Server replies with files (1 relative file path per udp message)
 * PLAY - Client sends video file path to play
 * SUBTITLES - Client sends message to toggle subtitles
 * PAUSE - Client sends message to toggle pause
 * QUIT - Client sends message to quit video player
 * SHUTDOWN - Client sends message to shutdown the system
 */
public enum MessageType {

    INVALID, BCAST, BCAST_RESPONSE, LIST, LIST_RESPONSE, PLAY, SUBTITLES, PAUSE, QUIT, SHUTDOWN;

    private static final MessageType[] types = values();

    public static MessageType fromInt(int i) {
        return (i < 0 || i >= types.length) ? MessageType.INVALID : types[i];
    }

}
