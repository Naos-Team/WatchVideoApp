package com.naosteam.watchvideoapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.naosteam.watchvideoapp.adapters.FeaturedVideoAdapter;
import com.naosteam.watchvideoapp.adapters.HorizontalCategoryAdapter;
import com.naosteam.watchvideoapp.adapters.TrendingMoviePagerAdapter;
import com.naosteam.watchvideoapp.asynctasks.LoadVideoAsync;
import com.naosteam.watchvideoapp.databinding.FragmentVideoBinding;
import com.naosteam.watchvideoapp.listeners.LoadVideoAsyncListener;
import com.naosteam.watchvideoapp.listeners.OnCategoryHorizontalListener;
import com.naosteam.watchvideoapp.listeners.OnVideoFeatureClickListener;
import com.naosteam.watchvideoapp.models.Category_M;
import com.naosteam.watchvideoapp.models.Videos_M;
import com.naosteam.watchvideoapp.utils.Methods;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
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
        LoadVideoAsyncListener listener = new LoadVideoAsyncListener() {
            @Override
            public void onStart() {
                binding.llList.setVisibility(View.GONE);
                binding.viewPager.setVisibility(View.GONE);
                binding.circleIndicator.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.progressBarPager.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEnd(boolean status, ArrayList<Videos_M> arrayList_trending, ArrayList<Videos_M> arrayList_mostview, ArrayList<Videos_M> arrayList_latest, ArrayList<Videos_M> arrayList_toprate, ArrayList<Category_M> arrayList_category) {
                if(getContext() != null){
                    if(Methods.getInstance().isNetworkConnected(getContext())){
                        if(status){
                            mTrendings.addAll(arrayList_trending);
                            mMostViews.addAll(arrayList_mostview);
                            mLatests.addAll(arrayList_latest);
                            mTopRates.addAll(arrayList_toprate);
                            mCategories.addAll(arrayList_category);

                            updateUI();

                            binding.viewPager.setVisibility(View.VISIBLE);
                            binding.circleIndicator.setVisibility(View.VISIBLE);
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
            }
        };
        LoadVideoAsync async = new LoadVideoAsync(requestBody, listener, Methods.getInstance());
        async.execute();
    }

    private void updateUI() {
        binding.rvLatestVideo.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.rvMostView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.rvTopRating.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.rvCategory.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int)Math.round(width*0.5), (int)Math.round(width*0.45));
        layoutParams.setMargins(35,0,0,0);

        binding.rvLatestVideo.setAdapter(new FeaturedVideoAdapter(layoutParams, mLatests, new OnVideoFeatureClickListener() {
            @Override
            public void onClick(int position) {

            }
        }));

        binding.rvTopRating.setAdapter(new FeaturedVideoAdapter(layoutParams, mTopRates, new OnVideoFeatureClickListener() {
            @Override
            public void onClick(int position) {

            }
        }));

        binding.rvMostView.setAdapter(new FeaturedVideoAdapter(layoutParams, mMostViews, new OnVideoFeatureClickListener() {
            @Override
            public void onClick(int position) {

            }
        }));

        RelativeLayout.LayoutParams layoutParam_category = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (int) Math.round(width*0.1));
        layoutParam_category.setMargins(30, 0, 0 ,0);

        binding.rvCategory.setAdapter(new HorizontalCategoryAdapter(layoutParam_category, mCategories, new OnCategoryHorizontalListener() {
            @Override
            public void onClick(int position) {

            }
        }));

        TrendingMoviePagerAdapter pagerAdapter = new TrendingMoviePagerAdapter(getContext(), mTrendings, new OnVideoFeatureClickListener() {
            @Override
            public void onClick(int position) {

            }
        });

        binding.viewPager.setAdapter(pagerAdapter);
        binding.circleIndicator.setViewPager(binding.viewPager);
        pagerAdapter.registerDataSetObserver(binding.circleIndicator.getDataSetObserver());



    }
}