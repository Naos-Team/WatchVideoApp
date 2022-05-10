package com.naosteam.watchvideoapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.navigation.NavController;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.databinding.ActivityVideoPlayerBinding;
import com.naosteam.watchvideoapp.models.Videos_M;

public class VideoPlayerActivity extends AppCompatActivity {

    private ActivityVideoPlayerBinding binding;
    private ExoPlayer player;
    private NavController navController;
    private ImageView btn_ffwd, btn_rew, btn_back;
    private TextView tv_title;
    private Videos_M mVideo;
    private ConstraintLayout main_cs, cs_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        binding = ActivityVideoPlayerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mVideo = (Videos_M) getIntent().getExtras().getSerializable("video");

        main_cs = findViewById(R.id.main_cs);
        cs_toolbar = findViewById(R.id.cs_toolbar);
        btn_ffwd = findViewById(R.id.btn_ffwd);
        btn_rew = findViewById(R.id.btn_rew);
        tv_title = findViewById(R.id.tv_title);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v->{
            onBackPressed();
        });

        tv_title.setText(mVideo.getVid_title());

        playVideo();

    }

    private void playVideo() {
        player = new ExoPlayer.Builder(this).build();
        binding.playerView.setPlayer(player);
        MediaItem mediaItem = MediaItem.fromUri("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
        player.addMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true);

        playError();

        btn_ffwd.setOnClickListener(v->{
            player.seekTo(player.getCurrentPosition() + 10000);
        });

        btn_rew.setOnClickListener(v->{
            player.seekTo(player.getCurrentPosition() - 10000);
        });
    }

    private void playError() {
        player.addAnalyticsListener(new AnalyticsListener() {
            @Override
            public void onPlayerError(EventTime eventTime, PlaybackException error) {
                Toast.makeText(VideoPlayerActivity.this, "Video playing error. Try again!", Toast.LENGTH_SHORT).show();
                AnalyticsListener.super.onPlayerError(eventTime, error);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onDestroy() {
        if(player.isPlaying()){
            player.stop();
        }

        super.onDestroy();
    }

    @Override
    public void onPause() {
        player.setPlayWhenReady(false);
        player.getPlaybackState();
        super.onPause();
    }

    @Override
    public void onResume() {
        player.setPlayWhenReady(true);
        player.getPlaybackState();
        super.onResume();
    }

    @Override
    public void onStart() {
        player.setPlayWhenReady(true);
        player.getPlaybackState();
        super.onStart();
    }

    private void setFullScreen(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

        ConstraintSet cs = new ConstraintSet();
        cs.clone(main_cs);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            cs.constrainPercentHeight(cs_toolbar.getId(), 0.1f);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            cs.constrainPercentHeight(cs_toolbar.getId(), 0.05f);

        }

        cs.applyTo(main_cs);

        super.onConfigurationChanged(newConfig);

    }
}