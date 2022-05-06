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
import com.naosteam.watchvideoapp.databinding.FragmentHomeBinding;
import com.naosteam.watchvideoapp.databinding.FragmentVideoBinding;


public class VideoFragment extends Fragment {

    private View rootView;
    private NavController navController;
    private TextView tv_video;
    private FragmentVideoBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentVideoBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);


        return rootView;
    }
}