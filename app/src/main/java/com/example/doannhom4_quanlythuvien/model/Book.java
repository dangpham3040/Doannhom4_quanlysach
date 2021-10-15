package com.example.doannhom4_quanlythuvien.model;

import java.io.Serializable;

public class Book implements Serializable {
    private String id, title, author, coverPhotoURL, link;
    private float rating;

    public Book() {
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", coverPhotoURL='" + coverPhotoURL + '\'' +
                ", link='" + link + '\'' +
                ", rating=" + rating +
                '}';
    }

    public Book(String id, String title, String author, float rating, String coverPhotoURL, String link) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.rating = rating;
        this.coverPhotoURL = coverPhotoURL;
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getCoverPhotoURL() {
        return coverPhotoURL;
    }

    public void setCoverPhotoURL(String coverPhotoURL) {
        this.coverPhotoURL = coverPhotoURL;
    }
}