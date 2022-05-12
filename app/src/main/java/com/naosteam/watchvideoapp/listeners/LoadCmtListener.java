package com.naosteam.watchvideoapp.listeners;

import com.naosteam.watchvideoapp.models.Comment_M;

import java.util.ArrayList;

public interface LoadCmtListener {
    public void onPre();
    public void onEnd(Boolean is_done, ArrayList<Comment_M> list_cmt);
}
