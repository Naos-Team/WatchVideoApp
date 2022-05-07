package com.naosteam.watchvideoapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.asynctasks.LoadVideoAsync;
import com.naosteam.watchvideoapp.databinding.FragmentHomeBinding;
import com.naosteam.watchvideoapp.databinding.FragmentVideoBinding;
import com.naosteam.watchvideoapp.listeners.LoadVideoListener;
import com.naosteam.watchvideoapp.models.Category_M;
import com.naosteam.watchvideoapp.models.Videos_M;
import com.naosteam.watchvideoapp.utils.Methods;

import java.util.ArrayList;

import okhttp3.RequestBody;


public class VideoFragment extends Fragment {

    private View rootView;
    private NavController navController;
    private TextView tv_video;
    private FragmentVideoBinding binding;
    private ArrayList<Videos_M> mTrendings;
    private ArrayList<Videos_M> mMostViews;
    private ArrayList<Videos_M> mLatests;
    private ArrayList<Videos_M> mTopRates;
    private ArrayList<Category_M> mCategories;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentVideoBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);

        mTrendings = new ArrayList<>();
        mMostViews = new ArrayList<>();
        mLatests = new ArrayList<>();
        mTopRates = new ArrayList<>();
        mCategories = new ArrayList<>();
        mCategories.add(new Category_M(0, "All", "", 1));

        LoadVideoData();

        return rootView;
    }

    private void LoadVideoData(){

        RequestBody requestBody = Methods.getInstance().getHomeRequestBody("LOAD_VIDEO_SCREEN", null);
        LoadVideoListener listener = new LoadVideoListener() {
            @Override
            public void onStart() {
                binding.llList.setVisibility(View.GONE);
                binding.viewPager.setVisibility(View.GONE);
                binding.tabLayout.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.progressBarPager.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEnd(boolean status, ArrayList<Videos_M> arrayList_trending, ArrayList<Videos_M> arrayList_mostview, ArrayList<Videos_M> arrayList_latest, ArrayList<Videos_M> arrayList_toprate, ArrayList<Category_M> arrayList_category) {
                if(Methods.getInstance().isNetworkConnected(getContext())){
                    if(status){
                        mTrendings.addAll(arrayList_trending);
                        mMostViews.addAll(arrayList_mostview);
                        mLatests.addAll(arrayList_latest);
                        mTopRates.addAll(arrayList_toprate);
                        mCategories.addAll(arrayList_category);

                        updateUI();

                        binding.viewPager.setVisibility(View.VISIBLE);
                        binding.tabLayout.setVisibility(View.VISIBLE);
                        binding.llList.setVisibility(View.VISIBLE);
                        binding.progressBar.setVisibility(View.GONE);
                        binding.progressBarPager.setVisibility(View.GONE);
                    }else{
                        Toast.makeText(getContext(), "Something wrong happened, try again!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(), "Please connect to the internet!", Toast.LENGTH_SHORT).show();
                }
            }
        };
        LoadVideoAsync async = new LoadVideoAsync(requestBody, listener, Methods.getInstance());
        async.execute();
    }

    private void updateUI() {

    }
}