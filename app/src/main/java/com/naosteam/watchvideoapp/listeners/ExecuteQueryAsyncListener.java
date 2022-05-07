package com.naosteam.watchvideoapp.listeners;

import com.naosteam.watchvideoapp.models.Videos_M;

import java.util.ArrayList;

public interface ExecuteQueryAsyncListener {
    void onStart();
    void onEnd(boolean status);
}
