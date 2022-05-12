package com.naosteam.watchvideoapp.fragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.adapters.RadioItemAdapter;
import com.naosteam.watchvideoapp.adapters.TVFragmentAdapter;
import com.naosteam.watchvideoapp.asynctasks.LoadFavoriteListAsync;
import com.naosteam.watchvideoapp.databinding.FragmentRadioFavoriteBinding;
import com.naosteam.watchvideoapp.databinding.FragmentTVFavoriteBinding;
import com.naosteam.watchvideoapp.listeners.LoadSearchVideoAsyncListener;
import com.naosteam.watchvideoapp.listeners.OnHomeItemClickListeners;
import com.naosteam.watchvideoapp.listeners.OnRadioClickListeners;
import com.naosteam.watchvideoapp.models.Videos_M;
import com.naosteam.watchvideoapp.utils.Constant;
import com.naosteam.watchvideoapp.utils.Methods;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class TVFavoriteFragment extends Fragment {
    private View rootView;
    private FragmentTVFavoriteBinding binding;
    private ArrayList<Videos_M> arrayListfav;
    private TVFragmentAdapter tvFragmentAdapter;
    private NavController navController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTVFavoriteBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();

        arrayListfav = new ArrayList<Videos_M>();
        navController = NavHostFragment.findNavController(this);

        LoadData();

        return rootView;
    }

    private void LoadData(){
        Bundle bundle = new Bundle();
        bundle.putInt("vid_type",2);
        RequestBody requestBody = Methods.getInstance().getVideoRequestBody("GET_FAV_DATA", bundle);
        LoadSearchVideoAsyncListener listener = new LoadSearchVideoAsyncListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onEnd(boolean status, ArrayList<Videos_M> arrayList_fav) {
                if(getContext() != null){
                    if(Methods.getInstance().isNetworkConnected(getContext())){
                        if(status){
                            arrayListfav.addAll(arrayList_fav);
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
        LoadFavoriteListAsync async = new LoadFavoriteListAsync(requestBody, listener, Methods.getInstance());
        async.execute();

    }

    private void updateUI() {
        binding.recyclerFavTv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams((int) Math.round(width*0.4), (int) Math.round(width * 0.3));
        layoutParams.setMargins(0, 10, 0, 10);

        tvFragmentAdapter = new TVFragmentAdapter(arrayListfav, layoutParams, new OnHomeItemClickListeners() {
            @Override
            public void onClick_homeItem(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("url", arrayListfav.get(position).getVid_url());
                bundle.putString("url_img", arrayListfav.get(position).getVid_thumbnail());
                bundle.putString("des", arrayListfav.get(position).getVid_description());
                bundle.putBoolean("isFavorite", true);
                navController.navigate(R.id.favorite_to_tv_detail, bundle);
            }
        });
        binding.recyclerFavTv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.recyclerFavTv.setAdapter(tvFragmentAdapter);

    }
}