package com.naosteam.funnyvideo.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.naosteam.funnyvideo.fragments.RadioFavoriteFragment;
import com.naosteam.funnyvideo.fragments.TVFavoriteFragment;
import com.naosteam.funnyvideo.fragments.VideoFavoriteFragment;
import com.naosteam.funnyvideo.listeners.FavoriteToDetailListener;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private VideoFavoriteFragment videoFavoriteFragment;
    private TVFavoriteFragment tvFavoriteFragment;
    private RadioFavoriteFragment radioFavoriteFragment;
    private FavoriteToDetailListener listener;

    public ViewPagerAdapter(FavoriteToDetailListener listener, @NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.listener = listener;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                videoFavoriteFragment = new VideoFavoriteFragment(listener);
                return videoFavoriteFragment;
            case 1:
                tvFavoriteFragment = new TVFavoriteFragment(listener);
                return tvFavoriteFragment;
            case 2:
                radioFavoriteFragment = new RadioFavoriteFragment(listener);
                return radioFavoriteFragment;
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

    public void onVideoPage(String text){
        if (videoFavoriteFragment!=null){
            videoFavoriteFragment.searchVideo(text);
        }


    }
    public void onTVPage(String text){
        if (tvFavoriteFragment!=null){
            tvFavoriteFragment.searchTV(text);
        }

    }
    public void onRadioPage(String text){
        if (radioFavoriteFragment!=null){
            radioFavoriteFragment.searchRadio(text);
        }


    }
}
