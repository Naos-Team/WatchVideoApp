package com.naosteam.watchvideoapp.models;

public class Rating_M {
    private int vid_id;
    private String uid;
    private int rate_score;
    private boolean rate_status;

    public Rating_M(int vid_id, String uid, int rate_score, boolean rate_status) {
        this.vid_id = vid_id;
        this.uid = uid;
        this.rate_score = rate_score;
        this.rate_status = rate_status;
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

    public int getRate_score() {
        return rate_score;
    }

    public void setRate_score(int rate_score) {
        this.rate_score = rate_score;
    }

    public boolean isRate_status() {
        return rate_status;
    }

    public void setRate_status(boolean rate_status) {
        this.rate_status = rate_status;
    }

    @Override
    public String toString() {
        return "Rating_M{" +
                "vid_id=" + vid_id +
                ", uid='" + uid + '\'' +
                ", rate_score=" + rate_score +
                ", rate_status=" + rate_status +
                '}';
    }
}
