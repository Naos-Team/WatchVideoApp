package com.naosteam.funnyvideo.listeners;

import com.naosteam.funnyvideo.models.Category_M;
import com.naosteam.funnyvideo.models.Videos_M;

import java.util.ArrayList;

public interface LoadVideoAsyncListener {
    void onStart();
    void onEnd(boolean status,
               ArrayList<Videos_M> arrayList_trending,
               ArrayList<Videos_M> arrayList_mostview,
               ArrayList<Videos_M> arrayList_latest,
               ArrayList<Videos_M> arrayList_toprate,
               ArrayList<Category_M> arrayList_category);
}
