package com.naosteam.watchvideoapp.models;

public class Category_M {
    private int cat_id;
    private String cat_name;
    private String cat_image;
    private int cat_type;
    private boolean cat_status;

    public Category_M(int cat_id, String cat_name, String cat_image, int cat_type, boolean cat_status) {
        this.cat_id = cat_id;
        this.cat_name = cat_name;
        this.cat_image = cat_image;
        this.cat_type = cat_type;
        this.cat_status = cat_status;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCat_image() {
        return cat_image;
    }

    public void setCat_image(String cat_image) {
        this.cat_image = cat_image;
    }

    public int getCat_type() {
        return cat_type;
    }

    public void setCat_type(int cat_type) {
        this.cat_type = cat_type;
    }

    public boolean isCat_status() {
        return cat_status;
    }

    public void setCat_status(boolean cat_status) {
        this.cat_status = cat_status;
    }

    @Override
    public String toString() {
        return "Category_M{" +
                "cat_id=" + cat_id +
                ", cat_name='" + cat_name + '\'' +
                ", cat_image='" + cat_image + '\'' +
                ", cat_type=" + cat_type +
                ", cat_status=" + cat_status +
                '}';
    }
}
