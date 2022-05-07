package com.naosteam.watchvideoapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.databinding.FragmentTvBinding;

import java.util.ArrayList;

public class TvFragment extends Fragment {
    private FragmentTvBinding binding;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTvBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);
        setUp();

        return rootView;
    }

    private void setUp(){

    }
}