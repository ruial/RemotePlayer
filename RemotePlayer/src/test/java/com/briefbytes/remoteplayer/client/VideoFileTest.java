package com.briefbytes.remoteplayer.client;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class VideoFileTest {

    private VideoFile videoFile;

    @Before
    public void setUp() throws Exception {
        videoFile = new VideoFile("path", 10);
    }

    @Test
    public void testEquals() throws Exception {
        assertEquals(videoFile, new VideoFile("path", 20));
        assertNotEquals(videoFile, new VideoFile("other", 10));
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(videoFile.hashCode(), new VideoFile("path", 20).hashCode());
        assertNotEquals(videoFile.hashCode(), new VideoFile("other", 10).hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("path (10 bytes)", videoFile.toString());
    }

}