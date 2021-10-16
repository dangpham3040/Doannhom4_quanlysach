package com.example.doannhom4_quanlythuvien.model;

import com.example.doannhom4_quanlythuvien.helpers.StaticConfig;
import com.google.firebase.auth.FirebaseAuth;

public class User {
    String id;
    String name;
    String phone;
    String email;
    String sex;
    String pic;


    public User(String name, String phone, String email, String sex, String pic) {
        String keyid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.id = keyid;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.sex = sex;
        this.pic = pic;
        StaticConfig.currentuser = keyid;

    }


    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }


    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getphone() {
        return phone;
    }

    public void setphone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
