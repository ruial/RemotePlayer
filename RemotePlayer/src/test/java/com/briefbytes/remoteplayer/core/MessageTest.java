package com.briefbytes.remoteplayer.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MessageTest {

    @Test(expected = IllegalArgumentException.class)
    public void invalidConstructor() throws Exception {
        new Message(null);
    }

    @Test
    public void testEquals() throws Exception {
        byte[] content = {1, 2};
        Message message = new Message(MessageType.PLAY, content);
        assertEquals(message, new Message(MessageType.PLAY, content));
        assertNotEquals(message, new Message(MessageType.PAUSE, content));
        assertNotEquals(message, new Message(MessageType.PLAY, new byte[]{}));
    }

    @Test
    public void testHashCode() throws Exception {
        byte[] content = {1, 2};
        Message message = new Message(MessageType.PLAY, content);
        assertEquals(message.hashCode(), new Message(MessageType.PLAY, content).hashCode());
        assertNotEquals(message.hashCode(), new Message(MessageType.PAUSE, content).hashCode());
        assertNotEquals(message.hashCode(), new Message(MessageType.PLAY, new byte[]{}).hashCode());
    }

    @Test
    public void testToString() throws Exception {
        new Message(MessageType.PLAY).toString();
    }

}