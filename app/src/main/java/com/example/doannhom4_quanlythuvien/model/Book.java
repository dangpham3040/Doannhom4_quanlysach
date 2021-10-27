package com.example.doannhom4_quanlythuvien.model;

import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;

import java.io.Serializable;

public class Book implements Serializable {
    private String id, title, author, coverPhotoURL, link;
    private String type;

    private long timestamp;

    public Book() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoverPhotoURL() {
        return coverPhotoURL;
    }

    public void setCoverPhotoURL(String coverPhotoURL) {
        this.coverPhotoURL = coverPhotoURL;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Book(String id, String title, String author, String coverPhotoURL, String link, String type) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.coverPhotoURL = coverPhotoURL;
        this.link = link;
        this.type = type;
        this.timestamp = StaticConfig.timestamp;
    }


}
