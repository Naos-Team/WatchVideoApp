package com.naosteam.funnyvideo.listeners;

import com.naosteam.funnyvideo.models.Videos_M;

import java.util.ArrayList;

public interface LoadYoutubeSearchAsyncListener {
    void onStart();
    void onEnd(boolean status, String nextPageToken, ArrayList<Videos_M> arrayList);
}
