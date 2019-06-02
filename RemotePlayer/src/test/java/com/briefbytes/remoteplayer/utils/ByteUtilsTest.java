package com.briefbytes.remoteplayer.utils;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ByteUtilsTest {

    @Test
    public void addByteStart() throws Exception {
        byte[] input = {1, 2};
        byte[] expected = {0, 1, 2};
        byte[] result = ByteUtils.addByte((byte) 0, 0, input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void addByteEnd() throws Exception {
        byte[] input = {1, 2};
        byte[] expected = {1, 2, 3};
        byte[] result = ByteUtils.addByte((byte) 3, input.length, input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void addByteMiddle() throws Exception {
        byte[] input = {1, 3};
        byte[] expected = {1, 2, 3};
        byte[] result = ByteUtils.addByte((byte) 2, 1, input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void byteStringConversion() throws Exception {
        String expected = "test/ç";
        byte[] bytes = ByteUtils.stringToBytes(expected);
        String result = ByteUtils.bytesToString(bytes);
        assertEquals(expected, result);
    }

    @Test
    public void byteLongConversion() throws Exception {
        long expected = Long.MAX_VALUE;
        byte[] bytes = ByteUtils.longToBytes(expected);
        long result = ByteUtils.bytesToLong(bytes);
        assertEquals(expected, result);
    }

    @Test
    public void join() throws Exception {
        byte[] expected = {1, 2, 3, 4};
        byte[] a = {1, 2};
        byte[] b = {3, 4};
        assertArrayEquals(expected, ByteUtils.join(a, b));
    }

    @Test
    public void copy() throws Exception {
        byte[] expected = {1, 2};
        byte[] bytes = {0, 1, 2};
        byte[] result = ByteUtils.copy(bytes, 1, bytes.length);
        assertArrayEquals(expected, result);
    }

}