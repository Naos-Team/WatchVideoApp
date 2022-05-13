package com.naosteam.watchvideoapp.listeners;

import com.naosteam.watchvideoapp.models.Videos_M;

import java.util.ArrayList;

public interface LoadYoutubeSearchAsyncListener {
    void onStart();
    void onEnd(boolean status, String nextPageToken, ArrayList<Videos_M> arrayList);
}
