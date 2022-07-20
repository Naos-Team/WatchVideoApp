package com.naosteam.funnyvideo.listeners;

import com.naosteam.funnyvideo.models.Comment_M;

import java.util.ArrayList;

public interface LoadCmtListener {
    public void onPre();
    public void onEnd(Boolean is_done, ArrayList<Comment_M> list_cmt);
}
