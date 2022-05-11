package com.naosteam.watchvideoapp.fragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.activities.VideoPlayerActivity;
import com.naosteam.watchvideoapp.databinding.FragmentVideoDetailBinding;
import com.naosteam.watchvideoapp.listeners.CheckFavListener;
import com.naosteam.watchvideoapp.listeners.CheckRatingListener;
import com.naosteam.watchvideoapp.listeners.SetFavListener;
import com.naosteam.watchvideoapp.listeners.SetRatingListener;
import com.naosteam.watchvideoapp.models.Rating_M;
import com.naosteam.watchvideoapp.models.Videos_M;
import com.naosteam.watchvideoapp.utils.Methods;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.concurrent.TimeUnit;


public class VideoDetailFragment extends Fragment {

    private View rootView;
    private NavController navController;
    private FragmentVideoDetailBinding binding;
    private Videos_M mVideo;
    private Boolean mIsFav = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentVideoDetailBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);

        mVideo = (Videos_M) getArguments().getSerializable("video");

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.csMain.setVisibility(View.GONE);

        Methods.getInstance().checkVideoFav(getContext(), mVideo.getVid_id(), new CheckFavListener() {
            @Override
            public void onComplete(boolean isSuccess, boolean isFav) {
                if(isSuccess){
                    mIsFav = isFav;
                }
                SetupView();
            }
        });

        return rootView;
    }

    private void SetupView() {

        binding.btnBack.setOnClickListener(v->{
            if(getArguments().getBoolean("is_home")){
                navController.navigate(R.id.Video_Detail_to_Home);
            } else {
                navController.navigate(R.id.DetailVideoToVideo);
            }
        });

        binding.ivStar.setOnClickListener(v->{
            if(FirebaseAuth.getInstance().getCurrentUser() != null){
                showRatingDialog();
            }else{
                Toast.makeText(getContext(), "Please login first to rate this video!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.playVideo.setOnClickListener(v->{
            Bundle bundle = new Bundle();
            bundle.putSerializable("video", mVideo);
            Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, 225);
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

        if(mIsFav){
            Picasso.get()
                    .load(R.drawable.ic_heart4_check)
                    .into(binding.ivHeart);
        }else{
            Picasso.get()
                    .load(R.drawable.ic_heart4_uncheckpng)
                    .into(binding.ivHeart);
        }

        binding.ivHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.getInstance().setFavState(getContext(), mVideo.getVid_id(), !mIsFav, new SetFavListener() {
                    @Override
                    public void onComplete(boolean isSuccess) {
                        if(isSuccess){
                            mIsFav = !mIsFav;
                            if(mIsFav){
                                Picasso.get()
                                        .load(R.drawable.ic_heart4_check)
                                        .into(binding.ivHeart);
                            }else{
                                Picasso.get()
                                        .load(R.drawable.ic_heart4_uncheckpng)
                                        .into(binding.ivHeart);
                            }
                        }else{
                            Toast.makeText(getContext(), "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        binding.progressBar.setVisibility(View.GONE);
        binding.csMain.setVisibility(View.VISIBLE);
    }

    private void showRatingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.dialog_rating_layout, null,false);
        builder.setView(view1);
        builder.setCancelable(false);

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ProgressBar progressBar = view1.findViewById(R.id.progressBar);
        ConstraintLayout cs_main = view1.findViewById(R.id.cs_main);
        RatingBar ratingBar = view1.findViewById(R.id.ratingBar);
        Button btn_submit = view1.findViewById(R.id.btn_submit);
        Button btn_cancel = view1.findViewById(R.id.btn_cancel);
        TextView tv_dialog_alert = view1.findViewById(R.id.tv_dialog_alert);

        btn_cancel.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        btn_submit.setOnClickListener(v ->{
            if(Methods.getInstance().isNetworkConnected(getContext())){
                if(ratingBar.getRating() > 0){
                    progressBar.setVisibility(View.VISIBLE);
                    cs_main.setVisibility(View.GONE);
                    Methods.getInstance().setRating(getContext(), mVideo.getVid_id(), ratingBar.getRating(), new SetRatingListener(){
                        @Override
                        public void onComplete(boolean isSuccess, float returnRate) {
                            if(isSuccess){
                                binding.tvRate.setText(""+returnRate);
                                Toast.makeText(getContext(), "Rating successfully!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                            cs_main.setVisibility(View.VISIBLE);
                            alertDialog.dismiss();
                        }
                    });
                }else{
                    Toast.makeText(getContext(), "You must rate as least one star!", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getContext(), "Please check internet connection!", Toast.LENGTH_SHORT).show();
            }
        });

        progressBar.setVisibility(View.VISIBLE);
        cs_main.setVisibility(View.GONE);

        Methods.getInstance().checkRating(getContext(), mVideo.getVid_id(), new CheckRatingListener() {
            @Override
            public void onComplete(boolean isSuccess, double rate) {
                if(isSuccess){
                    if(rate == 0){
                        tv_dialog_alert.setText("Is this video awesome?");
                        btn_submit.setText("submit");
                    }else{
                        tv_dialog_alert.setText("You have already rated it \nRate it again!");
                        btn_submit.setText("resubmit");
                    }
                }else{
                    Toast.makeText(getContext(), "Error when check your rating!", Toast.LENGTH_SHORT).show();
                }
                ratingBar.setRating((float) rate);
                progressBar.setVisibility(View.GONE);
                cs_main.setVisibility(View.VISIBLE);
            }
        });

        alertDialog.show();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 225 && resultCode == -1){
            int orientation = this.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }

    }
}