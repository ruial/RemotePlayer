package com.briefbytes.remoteplayer.client;

public class VideoFile {

    private String path;
    private long size;

    public VideoFile(String path, long size) {
        if (path == null) throw new IllegalArgumentException("Path cannot be null");
        this.path = path;
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VideoFile)) return false;

        VideoFile videoFile = (VideoFile) o;

        return path.equals(videoFile.path);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public String toString() {
        return path + " (" + size + " bytes)";
    }

}
