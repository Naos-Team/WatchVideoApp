package com.naosteam.watchvideoapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.naosteam.watchvideoapp.databinding.FragmentTvDeltailBinding;

public class TvDetailFragment extends Fragment {

    private FragmentTvDeltailBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTvDeltailBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        setUp();

        return rootView;
    }

    private void setUp(){

    }
}