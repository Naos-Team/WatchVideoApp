package com.naosteam.watchvideoapp.utils;

import android.content.Context;
import android.view.View;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;
import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.listeners.OnUpdateViewRadioPlayListener;

public class PlayerRadio {
    private static PlayerRadio instance;
    private static ExoPlayer player;
    private MediaItem mediaItem;
    private static Context context;

    public static void setContext(Context context) {
        PlayerRadio.context = context;
    }

    public static void setPlayer(PlayerView playerview) {
        playerview.setPlayer(player);
    }

    private PlayerRadio(){
    }

    public static PlayerRadio getInstance(OnUpdateViewRadioPlayListener listener){
        setUpPlayer(listener);
        if(instance == null){
            instance = new PlayerRadio();
        }
        return instance;
    }

    private static void setUpPlayer(OnUpdateViewRadioPlayListener listener){
        if(player == null){
            player = new ExoPlayer.Builder(context).build();
        }
        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
                switch (playbackState) {
                    case Player.STATE_BUFFERING:
                        listener.onBuffering();
                        break;
                    case Player.STATE_READY:
                        listener.onReady();
                        break;
                    case Player.STATE_ENDED:
                        listener.onEnd();
                        break;
                }
            }
        });
    }

    public boolean checkPlay(){
        return player.isPlaying();
    }

    public void startRadio(){
        if(player.isPlaying()){
            player.stop();
        }
        mediaItem = MediaItem.fromUri(Constant.Radio_Listening.getVid_url());
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }

    public void playRadio(){
        player.play();
    }

    public void pauseRadio(){
        player.pause();
    }
}
