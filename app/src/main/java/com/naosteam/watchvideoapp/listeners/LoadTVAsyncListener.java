package com.naosteam.watchvideoapp.listeners;

import com.naosteam.watchvideoapp.models.Category_M;
import com.naosteam.watchvideoapp.models.Videos_M;

import java.util.ArrayList;

public interface LoadTVAsyncListener {
    public void onPre();
    public void onEnd(Boolean ablBoolean, ArrayList<Videos_M> list_tv_all,
                      ArrayList<Videos_M> list_tv_trending, ArrayList<Category_M> list_categList_tv);
}
