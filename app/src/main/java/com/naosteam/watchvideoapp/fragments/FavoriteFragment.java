package com.naosteam.watchvideoapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.adapters.ViewPagerAdapter;
import com.naosteam.watchvideoapp.databinding.FragmentFavoriteBinding;
import com.naosteam.watchvideoapp.databinding.FragmentPrivacyBinding;

public class FavoriteFragment extends Fragment {

    private ViewPagerAdapter viewPagerAdapter;
    private View rootView;
    private NavController navController;
    private FragmentFavoriteBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);

        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.viewpager.setAdapter(viewPagerAdapter);
        binding.tablayout.setupWithViewPager(binding.viewpager);

        binding.imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.favorite_to_more);
            }
        });


        return rootView;
    }
}