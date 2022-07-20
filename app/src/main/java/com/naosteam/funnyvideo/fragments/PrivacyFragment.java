package com.naosteam.funnyvideo.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.naosteam.funnyvideo.R;
import com.naosteam.funnyvideo.databinding.FragmentPrivacyBinding;
import com.naosteam.funnyvideo.utils.AdsManager;

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
                AdsManager.showAdmobInterAd(getActivity(), new AdsManager.InterAdsListener() {
                    @Override
                    public void onClick() {
                        navController.navigate(R.id.privacy_to_more);
                    }
                });

            }
        });

        binding.webviewPrivacy.getSettings().setJavaScriptEnabled(true);
        binding.webviewPrivacy.loadUrl("file:///android_asset/"+"privacy.html");

        return rootView;
    }


}