package com.naosteam.funnyvideo.listeners;

public interface SetRatingAsyncListener {
    void onStart();
    void onEnd(boolean status, float returnRate);
}
