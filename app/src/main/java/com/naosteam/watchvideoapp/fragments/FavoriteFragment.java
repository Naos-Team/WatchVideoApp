package com.naosteam.watchvideoapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.adapters.ViewPagerAdapter;
import com.naosteam.watchvideoapp.databinding.FragmentFavoriteBinding;
import com.naosteam.watchvideoapp.listeners.FavoriteToDetailListener;
import com.naosteam.watchvideoapp.utils.AdsManager;

import org.checkerframework.checker.units.qual.A;

public class FavoriteFragment extends Fragment {

    private ViewPagerAdapter viewPagerAdapter;
    private View rootView;
    private NavController navController;
    private FragmentFavoriteBinding binding;
    private FavoriteToDetailListener listener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);

        listener = new FavoriteToDetailListener() {
            @Override
            public void onDirect(int type, Bundle bundle) {
                switch(type){
                    case 1:
                        AdsManager.showAdmobInterAd(getActivity(), new AdsManager.InterAdsListener() {
                            @Override
                            public void onClick() {
                                navController.navigate(R.id.favorite_to_video_detail, bundle);

                            }
                        });
                        break;
                    case 2:
                        AdsManager.showAdmobInterAd(getActivity(), new AdsManager.InterAdsListener() {
                            @Override
                            public void onClick() {
                                navController.navigate(R.id.favorite_to_tv_detail, bundle);
                            }
                        });

                        break;
                    case 3:
                        AdsManager.showAdmobInterAd(getActivity(), new AdsManager.InterAdsListener() {
                            @Override
                            public void onClick() {
                                navController.navigate(R.id.favorite_to_radio_detail, bundle);
                            }
                        });

                        break;
                }

            }
        };


        viewPagerAdapter = new ViewPagerAdapter(listener, getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.viewpager.setAdapter(viewPagerAdapter);

        binding.tablayout.setupWithViewPager(binding.viewpager);

        viewPagerAdapter.notifyDataSetChanged();

        binding.imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdsManager.showAdmobInterAd(getActivity(), new AdsManager.InterAdsListener() {
                    @Override
                    public void onClick() {
                        navController.navigate(R.id.favorite_to_more);
                    }
                });

            }
        });

        binding.searchView2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText!=null){
                    switch( binding.viewpager.getCurrentItem()){
                        case 0:
                            viewPagerAdapter.onVideoPage(newText);
                            break;
                        case 1:
                            viewPagerAdapter.onTVPage(newText);
                            break;
                        case 2:
                            viewPagerAdapter.onRadioPage(newText);
                            break;
                    }
                }
                return false;
            }
        });




        return rootView;
    }
}