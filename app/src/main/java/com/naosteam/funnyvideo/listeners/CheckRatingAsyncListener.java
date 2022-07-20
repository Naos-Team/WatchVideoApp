package com.naosteam.funnyvideo.listeners;

public interface CheckRatingAsyncListener {
    void onStart();
    void onEnd(boolean status, double rate);
}
