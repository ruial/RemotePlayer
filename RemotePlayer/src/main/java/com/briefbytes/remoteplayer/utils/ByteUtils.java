package com.briefbytes.remoteplayer.utils;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Utility class to handle byte arrays
 */
public class ByteUtils {

    public static final int LONG_BYTES = 8;
    public static final Charset CHARSET = Charset.forName("UTF-8");

    private ByteUtils() {
    }

    public static long bytesToLong(byte[] bytes) {
        if (bytes.length < LONG_BYTES) {
            throw new IllegalArgumentException("Invalid bytes length");
        }

        long number = 0, aux = 1, tmp;
        for (int i = LONG_BYTES - 1; i >= 0; i--) {
            tmp = bytes[i];
            if (bytes[i] < 0) tmp += 256; // get unsigned byte value
            number += tmp * aux;
            aux *= 256;
        }
        return number;
    }

    public static byte[] longToBytes(long number) {
        byte[] bytes = new byte[LONG_BYTES];
        for (int i = LONG_BYTES - 1; i >= 0; i--) {
            bytes[i] = (byte) (number % 256);
            number /= 256;
        }
        return bytes;
    }

    public static String bytesToString(byte[] bytes) {
        return new String(bytes, CHARSET);
    }

    public static byte[] stringToBytes(String string) {
        return string.getBytes(CHARSET);
    }

    public static byte[] addByte(byte b, int pos, byte[] arr) {
        byte[] res = new byte[arr.length + 1];
        System.arraycopy(arr, 0, res, 0, pos);
        System.arraycopy(arr, pos, res, pos + 1, arr.length - pos);
        res[pos] = b;
        return res;
    }

    public static byte[] join(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    public static byte[] copy(byte[] bytes, int start, int end) {
        return Arrays.copyOfRange(bytes, start, end);
    }

}
