package com.block.youtube.model;

public class Video {
    public String videoId;
    public String title;
    public String description;
    public String thumbUrl;
    public String url;

    public Video(){}

    public Video(String videoId, String title, String description, String thumbUrl, String url) {
        this.videoId = videoId;
        this.title = title;
        this.description = description;
        this.thumbUrl = thumbUrl;
        this.url = url;
    }
}







