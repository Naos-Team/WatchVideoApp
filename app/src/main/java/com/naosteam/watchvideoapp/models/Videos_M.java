package com.naosteam.watchvideoapp.models;

import java.util.Date;

public class Videos_M {
    private int vid_id;
    private int cat_id;
    private String vid_title;
    private String vid_thumbnail;
    private String vid_description;
    private String vid_url;
    private int vid_view;
    private Date vid_time;
    private float vid_avg_rate;
    private int vid_type;
    private boolean vid_is_premium;

    public Videos_M(int vid_id, int cat_id, String vid_title,
                    String vid_thumbnail, String vid_description,
                    String vid_url, int vid_view, float vid_avg_rate, int vid_type,
                    boolean vid_is_premium, Date vid_time) {
        this.vid_id = vid_id;
        this.cat_id = cat_id;
        this.vid_title = vid_title;
        this.vid_thumbnail = vid_thumbnail;
        this.vid_description = vid_description;
        this.vid_url = vid_url;
        this.vid_view = vid_view;
        this.vid_avg_rate = vid_avg_rate;
        this.vid_type = vid_type;
        this.vid_is_premium = vid_is_premium;
        this.vid_time = vid_time;
    }

    public String getVid_url() {
        return vid_url;
    }

    public void setVid_url(String vid_url) {
        this.vid_url = vid_url;
    }

    public Date getVid_time() {
        return vid_time;
    }

    public void setVid_time(Date vid_time) {
        this.vid_time = vid_time;
    }

    public int getVid_id() {
        return vid_id;
    }

    public void setVid_id(int vid_id) {
        this.vid_id = vid_id;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public String getVid_title() {
        return vid_title;
    }

    public void setVid_title(String vid_title) {
        this.vid_title = vid_title;
    }

    public String getVid_thumbnail() {
        return vid_thumbnail;
    }

    public void setVid_thumbnail(String vid_thumbnail) {
        this.vid_thumbnail = vid_thumbnail;
    }

    public String getVid_description() {
        return vid_description;
    }

    public void setVid_description(String vid_description) {
        this.vid_description = vid_description;
    }

    public int getVid_view() {
        return vid_view;
    }

    public void setVid_view(int vid_view) {
        this.vid_view = vid_view;
    }

    public float getVid_avg_rate() {
        return vid_avg_rate;
    }

    public void setVid_avg_rate(float vid_avg_rate) {
        this.vid_avg_rate = vid_avg_rate;
    }

    public int getVid_type() {
        return vid_type;
    }

    public void setVid_type(int vid_type) {
        this.vid_type = vid_type;
    }

    public boolean isVid_is_premium() {
        return vid_is_premium;
    }

    public void setVid_is_premium(boolean vid_is_premium) {
        this.vid_is_premium = vid_is_premium;
    }


    @Override
    public String toString() {
        return "Videos_MD{" +
                "vid_id=" + vid_id +
                ", cat_id=" + cat_id +
                ", vid_title='" + vid_title + '\'' +
                ", vid_thumbnail='" + vid_thumbnail + '\'' +
                ", vid_description='" + vid_description + '\'' +
                ", vid_view=" + vid_view +
                ", vid_avg_rate=" + vid_avg_rate +
                ", vid_type=" + vid_type +
                ", vid_is_premium=" + vid_is_premium +
                '}';
    }
}
