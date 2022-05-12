package com.naosteam.watchvideoapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.activities.LoginActivity;
import com.naosteam.watchvideoapp.activities.MainActivity;
import com.naosteam.watchvideoapp.activities.UpdateProfileActivity;
import com.naosteam.watchvideoapp.databinding.FragmentMoreBinding;
import com.naosteam.watchvideoapp.databinding.FragmentPrivacyBinding;

public class PrivacyFragment extends Fragment {
    private View rootView;
    private NavController navController;
    private FragmentPrivacyBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPrivacyBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);

        binding.imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.privacy_to_more);
            }
        });

        binding.webviewPrivacy.getSettings().setJavaScriptEnabled(true);
        binding.webviewPrivacy.loadUrl("file:///android_asset/"+"privacy.html");

        return rootView;
    }


}