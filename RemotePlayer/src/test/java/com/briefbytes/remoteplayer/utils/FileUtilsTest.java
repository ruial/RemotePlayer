package com.briefbytes.remoteplayer.utils;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class FileUtilsTest {

    @Test
    public void extension() throws Exception {
        assertEquals("exe", FileUtils.extension(new File("test.example.exe")));
    }

    @Test
    public void noExtension() throws Exception {
        assertEquals("", FileUtils.extension(new File("test")));
    }

}