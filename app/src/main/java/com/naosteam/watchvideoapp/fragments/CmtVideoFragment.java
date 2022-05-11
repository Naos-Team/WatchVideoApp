package com.naosteam.watchvideoapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.databinding.FragmentCmtVideoBinding;
import com.naosteam.watchvideoapp.databinding.FragmentMoreBinding;

public class CmtVideoFragment extends Fragment {
    private View rootView;
    FragmentCmtVideoBinding binding;
    private NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCmtVideoBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);

        setUp();

        return rootView;
    }

    private void setUp(){

    }
}