package com.ckt.io.wifidirect.utils;

/**
 * Created by admin on 2016/3/8.
 */
public class Song {
    private String fileName;
    private String title;
    private int duration;
    private String singer;
    private String album;
    private String size;
    private String fileUrl;
    private int id;
    private int album_id;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
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

    public Song() {
        super();
    }
    public Song(String fileName, String title, int duration, String singer, String album, String size,
                String fileUrl){
        super();
        this.fileName = fileName;
        this.title = title;
        this.duration = duration;
        this.singer = singer;
        this.album = album;
        this.size = size;
        this.fileUrl = fileUrl;
    }

    @Override
    public String toString() {
        return fileName;
    }
}
