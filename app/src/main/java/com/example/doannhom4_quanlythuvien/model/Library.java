package com.example.doannhom4_quanlythuvien.model;

public class Library {

    String User_id;
    String Book_id;
    Boolean is_heart;

    public Library(String user_id, String book_id, Boolean is_heart) {

        User_id = user_id;
        Book_id = book_id;
        this.is_heart = is_heart;
    }

    public Boolean getIs_heart() {
        return is_heart;
    }

    public void setIs_heart(Boolean is_heart) {
        this.is_heart = is_heart;
    }


    public String getUser_id() {
        return User_id;
    }

    public void setUser_id(String user_id) {
        User_id = user_id;
    }

    public String getBook_id() {
        return Book_id;
    }

    public void setBook_id(String book_id) {
        Book_id = book_id;
    }
}
