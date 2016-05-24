package com.ckt.io.wifidirect.utils;

/**
 * Created by admin on 2016/3/9.
 */
public class Movie {
    private String fileName;
    private String title;
    private int duration;
    private String size;
    private String fileUrl;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Movie() {
        super();
    }

    public Movie(String fileName, String title, int duration, String singer, String album, String size,
                 String fileUrl) {
        super();
        this.fileName = fileName;
        this.title = title;
        this.duration = duration;
        this.size = size;
        this.fileUrl = fileUrl;
    }

    @Override
    public String toString() {
        return fileName;
    }
}
