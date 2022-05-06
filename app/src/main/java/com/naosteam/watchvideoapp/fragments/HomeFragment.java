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


public class HomeFragment extends Fragment {

    private View rootView;
    private NavController navController;
    private TextView tv_home;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        navController = NavHostFragment.findNavController(this);

        tv_home = rootView.findViewById(R.id.tv_home);
        tv_home.setOnClickListener(v -> {
//            Bundle bundle = new Bundle();
//            bundle.putInt("amount", 15);
            navController.navigate(R.id.HomeToRadio);
        });

        return  rootView;
    }
}