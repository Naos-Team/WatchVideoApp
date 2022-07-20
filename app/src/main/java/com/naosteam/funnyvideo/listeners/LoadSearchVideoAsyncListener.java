package com.naosteam.funnyvideo.listeners;

import com.naosteam.funnyvideo.models.Videos_M;

import java.util.ArrayList;

public interface LoadSearchVideoAsyncListener {
    void onStart();
    void onEnd(boolean status, ArrayList<Videos_M> arrayList);
}
