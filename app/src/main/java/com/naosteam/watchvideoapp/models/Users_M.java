package com.naosteam.watchvideoapp.models;

import java.io.Serializable;

public class Users_M implements Serializable {
    private String uid;
    private String user_name;
    private String user_email;
    private String user_phone;
    private int user_age;
    private String photo_url;

    public Users_M(String uid, String user_name, String user_email, String user_phone, int user_age, String photo_url) {
        this.uid = uid;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_phone = user_phone;
        this.user_age = user_age;
        this.photo_url = photo_url;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public int getUser_age() {
        return user_age;
    }

    public void setUser_age(int user_age) {
        this.user_age = user_age;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }


    @Override
    public String toString() {
        return "Users_M{" +
                "uid='" + uid + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_email='" + user_email + '\'' +
                ", user_phone='" + user_phone + '\'' +
                ", user_age=" + user_age +
                '}';
    }


}
