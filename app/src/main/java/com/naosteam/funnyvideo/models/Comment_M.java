package com.naosteam.funnyvideo.models;

public class Comment_M {
    private int cmt_id;
    private int vid_id;
    private String uid;
    private String cmt_time;
    private String cmt_text;

    public Comment_M(int cmt_id, int vid_id, String uid, String cmt_time, String cmt_text) {
        this.cmt_id = cmt_id;
        this.vid_id = vid_id;
        this.uid = uid;
        this.cmt_time = cmt_time;
        this.cmt_text = cmt_text;
    }

    public int getCmt_id() {
        return cmt_id;
    }

    public void setCmt_id(int cmt_id) {
        this.cmt_id = cmt_id;
    }

    public int getVid_id() {
        return vid_id;
    }

    public void setVid_id(int vid_id) {
        this.vid_id = vid_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCmt_time() {
        return cmt_time;
    }

    public void setCmt_time(String cmt_time) {
        this.cmt_time = cmt_time;
    }

    public String getCmt_text() {
        return cmt_text;
    }

    public void setCmt_text(String cmt_text) {
        this.cmt_text = cmt_text;
    }

    @Override
    public String toString() {
        return "Comment_M{" +
                "cmt_id=" + cmt_id +
                ", vid_id=" + vid_id +
                ", uid='" + uid + '\'' +
                ", cmt_time='" + cmt_time + '\'' +
                ", cmt_text='" + cmt_text + '\'' +
                '}';
    }
}
