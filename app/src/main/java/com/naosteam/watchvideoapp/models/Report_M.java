package com.naosteam.watchvideoapp.models;

public class Report_M {
    private int report_id;
    private String uid;
    private int vid_id;
    private String report_content;
    private boolean report_status;

    public Report_M(int report_id, String uid, int vid_id, String report_content, boolean report_status) {
        this.report_id = report_id;
        this.uid = uid;
        this.vid_id = vid_id;
        this.report_content = report_content;
        this.report_status = report_status;
    }

    public int getReport_id() {
        return report_id;
    }

    public void setReport_id(int report_id) {
        this.report_id = report_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getVid_id() {
        return vid_id;
    }

    public void setVid_id(int vid_id) {
        this.vid_id = vid_id;
    }

    public String getReport_content() {
        return report_content;
    }

    public void setReport_content(String report_content) {
        this.report_content = report_content;
    }

    public boolean isReport_status() {
        return report_status;
    }

    public void setReport_status(boolean report_status) {
        this.report_status = report_status;
    }

    @Override
    public String toString() {
        return "Report_M{" +
                "report_id=" + report_id +
                ", uid='" + uid + '\'' +
                ", vid_id=" + vid_id +
                ", report_content='" + report_content + '\'' +
                ", report_status=" + report_status +
                '}';
    }
}
