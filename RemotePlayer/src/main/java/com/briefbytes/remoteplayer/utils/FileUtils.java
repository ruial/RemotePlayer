package com.briefbytes.remoteplayer.utils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Utility class to handle files
 */
public class FileUtils {

    private FileUtils() {
    }

    public static List<File> listFiles(File root) {
        List<File> files = new LinkedList<File>();

        if (root.isDirectory()) {
            LinkedList<File> directories = new LinkedList<File>();
            directories.add(root);

            while (!directories.isEmpty()) {
                File dir = directories.removeFirst();
                for (File file : dir.listFiles()) {
                    if (file.isDirectory()) {
                        directories.add(file);
                    } else {
                        files.add(file);
                    }
                }
            }
        }

        return files;
    }

    public static String extension(File file) {
        String path = file.getPath();
        int index = path.lastIndexOf('.');
        return index != -1 ? path.substring(index + 1) : "";
    }

}
