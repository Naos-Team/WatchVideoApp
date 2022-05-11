package com.naosteam.watchvideoapp.listeners;

public interface CheckRatingAsyncListener {
    void onStart();
    void onEnd(boolean status, double rate);
}
