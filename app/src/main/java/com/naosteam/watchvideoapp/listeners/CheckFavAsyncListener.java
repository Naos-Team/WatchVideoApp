package com.naosteam.watchvideoapp.listeners;

public interface CheckFavAsyncListener {
    void onStart();
    void onEnd(boolean status, boolean isFav);
}
