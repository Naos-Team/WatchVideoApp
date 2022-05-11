package com.naosteam.watchvideoapp.listeners;

public interface SetRatingAsyncListener {
    void onStart();
    void onEnd(boolean status, float returnRate);
}
