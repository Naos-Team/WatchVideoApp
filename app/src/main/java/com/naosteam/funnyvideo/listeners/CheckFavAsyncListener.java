package com.naosteam.funnyvideo.listeners;

public interface CheckFavAsyncListener {
    void onStart();
    void onEnd(boolean status, boolean isFav);
}
