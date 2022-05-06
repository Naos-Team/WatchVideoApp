package com.naosteam.watchvideoapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.naosteam.watchvideoapp.R;


public class VideoFragment extends Fragment {

    private View rootView;
    private NavController navController;
    private TextView tv_video;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_video, container, false);

        navController = NavHostFragment.findNavController(this);


        return rootView;
    }
}