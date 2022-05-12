package com.naosteam.watchvideoapp.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.naosteam.watchvideoapp.fragments.RadioFavoriteFragment;
import com.naosteam.watchvideoapp.fragments.TVFavoriteFragment;
import com.naosteam.watchvideoapp.fragments.VideoFavoriteFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new VideoFavoriteFragment();
            case 1:
                return new TVFavoriteFragment();
            case 2:
                return new RadioFavoriteFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title="";
        switch(position){
            case 0:
                title = "Video";
                break;
            case 1:
                title = "TV";
                break;
            case 2:
                title = "Radio";
                break;
        }
        return title;
    }
}
