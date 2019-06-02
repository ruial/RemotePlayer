package com.briefbytes.remoteplayer.core;

import java.util.Arrays;

/**
 * Class to hold message type and additional content, which may be empty in some messages
 */
public class Message {

    private MessageType type;
    private byte[] content;

    public Message(MessageType type, byte[] content) {
        if (type == null || content == null) throw new IllegalArgumentException("Null values not allowed");
        this.type = type;
        this.content = content;
    }

    public Message(MessageType type) {
        this(type, new byte[0]);
    }

    public MessageType getType() {
        return type;
    }

    public byte[] getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;

        Message message = (Message) o;

        if (type != message.type) return false;
        return Arrays.equals(content, message.content);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", content=" + Arrays.toString(content) +
                '}';
    }
}
