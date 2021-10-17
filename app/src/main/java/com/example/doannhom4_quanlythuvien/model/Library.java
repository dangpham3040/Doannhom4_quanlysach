package com.example.doannhom4_quanlythuvien.model;

import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;

import java.io.Serializable;
import java.util.ArrayList;

public class Library implements Serializable {

    String Book_id;
    String User_id;
    Boolean is_heart;

    public Library(String book_id, String user_id, Boolean is_heart) {

        Book_id = book_id;
        User_id = user_id;
        this.is_heart = is_heart;
    }

    public Library() {
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
