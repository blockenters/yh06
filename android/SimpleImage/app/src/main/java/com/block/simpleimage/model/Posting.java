package com.block.simpleimage.model;

import java.io.Serializable;

public class Posting implements Serializable {

    public int id;
    public int albumId;
    public String title;
    public String url;
    public String thumbnailUrl;

    public Posting(){

    }

    public Posting(int id, int albumId, String title, String url, String thumbnailUrl) {
        this.id = id;
        this.albumId = albumId;
        this.title = title;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
    }
}
