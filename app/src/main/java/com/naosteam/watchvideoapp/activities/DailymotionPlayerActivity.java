package com.naosteam.watchvideoapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.naosteam.watchvideoapp.databinding.ActivityDailymotionPlayerBinding;

import java.util.HashMap;
import java.util.Map;

public class DailymotionPlayerActivity extends AppCompatActivity {

    private ActivityDailymotionPlayerBinding binding;
    private String mVideoID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDailymotionPlayerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mVideoID = getIntent().getStringExtra("vid_id");

        Map<String, String> params = new HashMap<>();
        params.put("video", mVideoID);
        binding.playerDailymotion.load(params);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        binding.playerDailymotion.release();

        finish();
    }

    @Override
    public void onPause() {
        binding.playerDailymotion.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        binding.playerDailymotion.play();
        super.onResume();
    }


}