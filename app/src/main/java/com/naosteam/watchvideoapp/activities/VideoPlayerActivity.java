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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.databinding.ActivityVideoPlayerBinding;
import com.naosteam.watchvideoapp.models.Videos_M;
import com.naosteam.watchvideoapp.utils.Constant;

public class VideoPlayerActivity extends AppCompatActivity {

    private ActivityVideoPlayerBinding binding;
    private ExoPlayer player;
    private NavController navController;
    private ImageView btn_ffwd, btn_rew, btn_back, btn_speed, btn_rotate;
    private TextView tv_title;
    private Videos_M mVideo;
    private ConstraintLayout main_cs, cs_toolbar, cs_shadow_up, cs_shadow_down, cs_control, cs_progress;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        binding = ActivityVideoPlayerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mVideo = (Videos_M) getIntent().getExtras().getSerializable("video");

        progressBar = findViewById(R.id.progressBar);
        btn_rotate = findViewById(R.id.btn_rotate);
        btn_speed = findViewById(R.id.btn_speed);
        cs_progress = findViewById(R.id.cs_progress);
        cs_control = findViewById(R.id.cs_control);
        cs_shadow_up = findViewById(R.id.cs_shadow_up);
        cs_shadow_down = findViewById(R.id.cs_shadow_down);
        main_cs = findViewById(R.id.main_cs);
        cs_toolbar = findViewById(R.id.cs_toolbar);
        btn_ffwd = findViewById(R.id.btn_ffwd);
        btn_rew = findViewById(R.id.btn_rew);
        tv_title = findViewById(R.id.tv_title);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v->{
            onBackPressed();
        });
        btn_rotate.setOnClickListener(v->{
            int orientation = this.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }else{
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        });

        tv_title.setText(mVideo.getVid_title());

        playVideo();

    }

    private void playVideo() {

        String video_url = mVideo.getVid_url();

        player = new ExoPlayer.Builder(this).build();
        binding.playerView.setPlayer(player);

        MediaItem mediaItem = MediaItem.fromUri(video_url);
        //MediaItem mediaItem = MediaItem.fromUri("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
        player.addMediaItem(mediaItem);
        player.prepare();

        playListener();

        player.setPlayWhenReady(true);

        btn_ffwd.setOnClickListener(v->{
            player.seekTo(player.getCurrentPosition() + 10000);
        });

        btn_rew.setOnClickListener(v->{
            player.seekTo(player.getCurrentPosition() - 10000);
        });

        btn_speed.setOnClickListener(v->{
            openSpeedSelection();
        });
    }

    private void playListener() {
        player.addAnalyticsListener(new AnalyticsListener() {
            @Override
            public void onPlayerError(EventTime eventTime, PlaybackException error) {
                Toast.makeText(VideoPlayerActivity.this, "Video playing error. Try again!", Toast.LENGTH_SHORT).show();
                AnalyticsListener.super.onPlayerError(eventTime, error);
            }

            @Override
            public void onPlaybackStateChanged(EventTime eventTime, int state) {
                AnalyticsListener.super.onPlaybackStateChanged(eventTime, state);
                switch (state){
                    case Player.STATE_BUFFERING:
                        progressBar.setVisibility(View.VISIBLE);
                        Log.e(Constant.ERR_TAG, "buffering");
                        break;
                    case Player.STATE_READY:
                        progressBar.setVisibility(View.GONE);
                        Log.e(Constant.ERR_TAG, "ready");
                }
            }
        });

    }

    private void openSpeedSelection(){

        PopupMenu menu = new PopupMenu(this, btn_speed);

        menu.inflate(R.menu.menu_speed);

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.menu_speed_x0_5:
                        player.setPlaybackSpeed(0.5f);
                        Toast.makeText(VideoPlayerActivity.this, "x0.25 speed", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_speed_x0_75:
                        player.setPlaybackSpeed(0.75f);
                        Toast.makeText(VideoPlayerActivity.this, "x0.75 speed", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_speed_x1:
                        player.setPlaybackSpeed(1f);
                        Toast.makeText(VideoPlayerActivity.this, "Standard speed", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_speed_x1_25:
                        player.setPlaybackSpeed(1.25f);
                        Toast.makeText(VideoPlayerActivity.this, "x1.25 speed", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_speed_x1_5:
                        player.setPlaybackSpeed(1.5f);
                        Toast.makeText(VideoPlayerActivity.this, "x1.5 speed", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_speed_x1_75:
                        player.setPlaybackSpeed(1.75f);
                        Toast.makeText(VideoPlayerActivity.this, "x1.75 speed", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_speed_x2:
                        player.setPlaybackSpeed(2f);
                        Toast.makeText(VideoPlayerActivity.this, "x2 speed", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        menu.show();
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
            cs.constrainPercentHeight(cs_shadow_up.getId(), 0.35f);
            cs.constrainPercentHeight(cs_shadow_down.getId(), 0.4f);
            cs.constrainPercentHeight(cs_control.getId(), 0.1f);
            cs.constrainPercentHeight(cs_progress.getId(), 0.1f);
            cs.constrainPercentHeight(progressBar.getId(), 0.15f);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            cs.constrainPercentHeight(cs_toolbar.getId(), 0.05f);
            cs.constrainPercentHeight(cs_shadow_up.getId(), 0.13f);
            cs.constrainPercentHeight(cs_shadow_down.getId(), 0.18f);
            cs.constrainPercentHeight(cs_control.getId(), 0.05f);
            cs.constrainPercentHeight(cs_progress.getId(), 0.05f);
            cs.constrainPercentHeight(progressBar.getId(), 0.07f);
        }

        cs.applyTo(main_cs);

        super.onConfigurationChanged(newConfig);

    }
}