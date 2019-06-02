package com.briefbytes.remoteplayer.utils;

import java.io.IOException;

/**
 * Utility class to handle OS specific code
 */
public class OsUtils {

    public static String os = System.getProperty("os.name");
    public static boolean isWindows = os.startsWith("Windows");

    private OsUtils() {
    }

    public static void shutdown() throws IOException {
        if (isWindows) {
            exec("shutdown", "-s", "-t", "60");
        } else {
            exec("sudo", "shutdown", "-h", "+1");
        }
    }

    public static Process exec(String... command) throws IOException {
        return new ProcessBuilder(command).start();
    }

}
