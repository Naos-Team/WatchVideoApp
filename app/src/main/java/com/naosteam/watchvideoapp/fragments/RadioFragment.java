package com.naosteam.watchvideoapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.naosteam.watchvideoapp.adapters.FeaturedVideoAdapter;
import com.naosteam.watchvideoapp.adapters.RadioCategoryAdapter;
import com.naosteam.watchvideoapp.adapters.RadioItemAdapter;
import com.naosteam.watchvideoapp.asynctasks.LoadRadioAsync;
import com.naosteam.watchvideoapp.databinding.FragmentRadioBinding;
import com.naosteam.watchvideoapp.listeners.LoadRadioAsyncListener;
import com.naosteam.watchvideoapp.listeners.OnRadioClickListeners;
import com.naosteam.watchvideoapp.listeners.OnVideoFeatureClickListener;
import com.naosteam.watchvideoapp.models.Category_M;
import com.naosteam.watchvideoapp.models.Videos_M;
import com.naosteam.watchvideoapp.utils.Methods;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class RadioFragment extends Fragment {

    private View rootView;
    private NavController navController;
    private TextView tv_video;
    private FragmentRadioBinding binding;
    private ArrayList<Videos_M> mTrendings;
    private ArrayList<Category_M> mCats;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRadioBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);

        mTrendings = new ArrayList<>();
        mCats = new ArrayList<>();
        mCats.add(new Category_M(0, "All", "", 1));

        LoadData();

        return rootView;
    }

    private void LoadData(){
        RequestBody requestBody = Methods.getInstance().getHomeRequestBody("LOAD_RADIO_SCREEN", null);
        LoadRadioAsyncListener listener = new LoadRadioAsyncListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onEnd(boolean status, ArrayList<Videos_M> arrayList_trending, ArrayList<Category_M> arrayList_category) {
                if(getContext() != null){
                    if(Methods.getInstance().isNetworkConnected(getContext())){
                        if(status){
                            mTrendings.addAll(arrayList_trending);
                            mCats.addAll(arrayList_category);

                            updateUI();
                        }else{
                            Toast.makeText(getContext(), "Something wrong happened, try again!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getContext(), "Please connect to the internet!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        LoadRadioAsync async = new LoadRadioAsync(requestBody, listener, Methods.getInstance());
        async.execute();
    }

    private void updateUI() {
        binding.rclCatRadioFrag.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rclRadioTrending.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int)Math.round(width*0.5), (int)Math.round(width*0.45));
        layoutParams.setMargins(35,0,0,0);

        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams((int)Math.round(width), (int)Math.round(width*0.2));
        layoutParams.setMargins(0,20,0,0);

        binding.rclCatRadioFrag.setAdapter(new RadioCategoryAdapter(layoutParams, mCats, new OnRadioClickListeners() {
            @Override
            public void onClick(int position) {

            }
        }));

        binding.rclRadioTrending.setAdapter(new RadioItemAdapter(layoutParams1, mTrendings, new OnRadioClickListeners() {
            @Override
            public void onClick(int position) {
            }
        }));
    }
}