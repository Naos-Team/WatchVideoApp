package com.naosteam.watchvideoapp.listeners;

import com.naosteam.watchvideoapp.models.Category_M;
import com.naosteam.watchvideoapp.models.Videos_M;

import java.util.ArrayList;

public interface LoadRadioAsyncListener {
    void onStart();
    void onEnd(boolean status,
               ArrayList<Videos_M> arrayList_trending,
               ArrayList<Category_M> arrayList_category
               );
}
