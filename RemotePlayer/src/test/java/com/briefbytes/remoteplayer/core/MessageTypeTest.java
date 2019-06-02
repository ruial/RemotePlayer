package com.briefbytes.remoteplayer.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageTypeTest {

    @Test
    public void validFromInt() throws Exception {
        assertEquals(MessageType.BCAST, MessageType.fromInt(1));
        assertEquals(MessageType.SHUTDOWN, MessageType.fromInt(9));
    }

    @Test
    public void invalidFromInt() throws Exception {
        assertEquals(MessageType.INVALID, MessageType.fromInt(-1));
        assertEquals(MessageType.INVALID, MessageType.fromInt(10));
    }

}