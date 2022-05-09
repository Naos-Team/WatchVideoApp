package com.naosteam.watchvideoapp.fragments;

import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.databinding.FragmentVideoDetailBinding;
import com.naosteam.watchvideoapp.listeners.CheckFavListener;
import com.naosteam.watchvideoapp.models.Videos_M;
import com.naosteam.watchvideoapp.utils.Constant;
import com.naosteam.watchvideoapp.utils.Methods;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import wseemann.media.FFmpegMediaMetadataRetriever;


public class VideoDetailFragment extends Fragment {

    private View rootView;
    private NavController navController;
    private FragmentVideoDetailBinding binding;
    private Videos_M mVideo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentVideoDetailBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);

        mVideo = (Videos_M) getArguments().getSerializable("video");

        SetupView();

        Methods.getInstance().checkVideoFav(getContext(), mVideo.getVid_id(), new CheckFavListener() {
            @Override
            public void isFav(boolean isFav) {

            }

            @Override
            public void onFailure() {

            }
        });

        return rootView;
    }

    private void SetupView() {
        binding.btnBack.setOnClickListener(v->{
            navController.navigate(R.id.DetailVideoToVideo);
        });

        Picasso.get()
                .load(mVideo.getVid_thumbnail())
                .into(binding.ivThumb);

        binding.tvTitle.setText(mVideo.getVid_title());
        binding.tvDescription.setText(mVideo.getVid_description());

        Date currentDate = new Date();
        Date postDate = mVideo.getVid_time();

        long diffInTime = currentDate.getTime() - postDate.getTime();
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInTime);
        long diffInHour = TimeUnit.MILLISECONDS.toHours(diffInTime);
        long diffInYear = TimeUnit.MILLISECONDS.toDays(diffInTime) / 365l;
        long diffInMonth = TimeUnit.MILLISECONDS.toDays(diffInTime) / 30l;
        long diffInDay = TimeUnit.MILLISECONDS.toDays(diffInTime);


        if (diffInYear < 1) {
            if (diffInMonth < 1) {
                if (diffInDay < 1) {
                    if (diffInHour < 1) {
                        if (diffInMinutes < 1) {
                            binding.tvTime.setText("Just now");
                        } else {
                            binding.tvTime.setText(diffInMinutes + " minutes ago");
                        }
                    } else {
                        binding.tvTime.setText(diffInHour + " hours ago");
                    }
                } else {
                    binding.tvTime.setText(diffInDay + " days ago");
                }
            } else {
                binding.tvTime.setText(diffInMonth + " months ago");
            }
        } else {
            binding.tvTime.setText(diffInYear + " years ago");
        }

        int views = mVideo.getVid_view();

        if(views > 1000){
            if(views > 1000000){
                if(views > 1000000000){
                    double view1 = (double)Math.round(views/1000000000 * 10d) / 10d;
                    binding.tvViews.setText(view1 + "B views");
                }else{
                    double view1 = (double)Math.round(views/1000000 * 10d) / 10d;
                    binding.tvViews.setText(view1 + "M views");
                }
            }else{
                double view1 = (double)Math.round(views/1000 * 10d) / 10d;
                binding.tvViews.setText(view1 + "K views");
            }
        }else{
            binding.tvViews.setText(views + " views");
        }

        int sec = mVideo.getDuration();
        int hours = sec / 3600;
        int minutes = (sec % 3600) / 60;
        int seconds = sec % 60;


        String timeString = "";

        if(hours >= 1){
            timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }else{
            timeString = String.format("%02d:%02d", minutes, seconds);
        }

        binding.tvDuration.setText(timeString);

        binding.tvRate.setText("" + mVideo.getVid_avg_rate());
    }
}