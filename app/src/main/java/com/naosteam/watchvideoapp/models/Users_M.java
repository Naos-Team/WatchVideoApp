package com.naosteam.watchvideoapp.models;

public class Users_M {
    private String uid;
    private String user_name;
    private String user_email;
    private String user_phone;
    private int user_age;

    public Users_M(String uid, String user_name, String user_email, String user_phone, int user_age) {
        this.uid = uid;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_phone = user_phone;
        this.user_age = user_age;
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
