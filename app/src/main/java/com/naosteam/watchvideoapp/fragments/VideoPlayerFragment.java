package com.naosteam.watchvideoapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.ui.PlayerView;
import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.activities.MainActivity;
import com.naosteam.watchvideoapp.databinding.FragmentMoreBinding;
import com.naosteam.watchvideoapp.databinding.FragmentVideoPlayerBinding;
import com.naosteam.watchvideoapp.models.Videos_M;

import java.util.EventListener;

public class VideoPlayerFragment extends Fragment {

    private FragmentVideoPlayerBinding binding;
    private ExoPlayer player;
    private View rootView;
    private NavController navController;
    private ImageView btn_ffwd, btn_rew, btn_back;
    private TextView tv_title;
    private Videos_M mVideo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentVideoPlayerBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);

        mVideo = (Videos_M) getArguments().getSerializable("video");

        MainActivity.hide_Navi();

        btn_ffwd = rootView.findViewById(R.id.btn_ffwd);
        btn_rew = rootView.findViewById(R.id.btn_rew);
        tv_title = rootView.findViewById(R.id.tv_title);
        btn_back = rootView.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v->{
        });

        tv_title.setText(mVideo.getVid_title());

        playVideo();

        return rootView;
    }

    private void playVideo() {
        player = new ExoPlayer.Builder(getContext()).build();
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
                Toast.makeText(getContext(), "Video playing error. Try again!", Toast.LENGTH_SHORT).show();
                AnalyticsListener.super.onPlayerError(eventTime, error);
            }
        });

    }

    @Override
    public void onDestroy() {
        MainActivity.show_Navi();

        if(player.isPlaying()){
            player.stop();
        }

        MainActivity.hide_Navi();

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
}