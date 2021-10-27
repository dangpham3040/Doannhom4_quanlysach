package com.example.doannhom4_quanlythuvien.model;

import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;

public class Comment {
    private String book_id;
    private String user_id;
    private String text;
    private float rating;
    private long timestamp;

    public Comment(String book_id, String user_id, String text, float rating) {
        this.book_id = book_id;
        this.user_id = user_id;
        this.text = text;
        this.rating = rating;
        this.timestamp= StaticConfig.timestamp;
    }

    public Comment() {
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
