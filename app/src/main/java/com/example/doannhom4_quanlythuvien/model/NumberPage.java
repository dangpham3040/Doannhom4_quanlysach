package com.example.doannhom4_quanlythuvien.model;

public class NumberPage {
    String id;
    String book_id;
    String user_id;
    int numberpage;

    public NumberPage(String id, String book_id, String user_id, int numberpage) {
        this.id = id;
        this.book_id = book_id;
        this.user_id = user_id;
        this.numberpage = numberpage;
    }

    public NumberPage() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getNumberpage() {
        return numberpage;
    }

    public void setNumberpage(int numberpage) {
        this.numberpage = numberpage;
    }
}
