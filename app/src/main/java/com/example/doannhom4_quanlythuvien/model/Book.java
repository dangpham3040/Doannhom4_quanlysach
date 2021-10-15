package com.example.doannhom4_quanlythuvien.model;

import java.io.Serializable;

public class Book implements Serializable {
    private String id, title, author, coverPhotoURL, link;
    String type;
    private float rating;

    public Book() {
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Book(String id, String title, String author, String coverPhotoURL, String link, String type, float rating) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.coverPhotoURL = coverPhotoURL;
        this.link = link;
        this.type = type;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", coverPhotoURL='" + coverPhotoURL + '\'' +
                ", link='" + link + '\'' +
                ", type='" + type + '\'' +
                ", rating=" + rating +
                '}';
    }
}
