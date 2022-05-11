package com.naosteam.watchvideoapp.fragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.adapters.RadioItemAdapter;
import com.naosteam.watchvideoapp.asynctasks.LoadFavoriteListAsync;
import com.naosteam.watchvideoapp.asynctasks.LoadRadioAsync;
import com.naosteam.watchvideoapp.asynctasks.LoadSearchVideoAsync;
import com.naosteam.watchvideoapp.asynctasks.LoadVideoAsync;
import com.naosteam.watchvideoapp.databinding.FragmentRadioFavoriteBinding;
import com.naosteam.watchvideoapp.databinding.FragmentVideoFavoriteBinding;
import com.naosteam.watchvideoapp.listeners.LoadRadioAsyncListener;
import com.naosteam.watchvideoapp.listeners.LoadSearchVideoAsyncListener;
import com.naosteam.watchvideoapp.listeners.LoadVideoAsyncListener;
import com.naosteam.watchvideoapp.listeners.OnRadioClickListeners;
import com.naosteam.watchvideoapp.models.Category_M;
import com.naosteam.watchvideoapp.models.Videos_M;
import com.naosteam.watchvideoapp.utils.Constant;
import com.naosteam.watchvideoapp.utils.Methods;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class RadioFavoriteFragment extends Fragment {
    private View rootView;
    private FragmentRadioFavoriteBinding binding;
    private ArrayList<Videos_M> arrayList_fav;
    private RadioItemAdapter radioItemAdapter;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRadioFavoriteBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        arrayList_fav = new ArrayList<Videos_M>();
        navController = NavHostFragment.findNavController(this);

        LoadData();

        return rootView;
    }
    private void LoadData(){
            Bundle bundle = new Bundle();
            bundle.putInt("vid_type",3);
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
                                arrayList_fav.addAll(arrayList_fav);
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
        binding.recyclerRadioFav.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams((int) Math.round(width), (int) Math.round(width * 0.17));
        layoutParams.setMargins(0, 20, 0, 20);

        radioItemAdapter = new RadioItemAdapter(layoutParams, arrayList_fav, new OnRadioClickListeners() {
            @Override
            public void onClick(int position) {
                Constant.Radio_Listening = arrayList_fav.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("radio", Constant.Radio_Listening );
                bundle.putString("from", "from_favorite");
                navController.navigate(R.id.favorite_to_radio_detail, bundle);
            }
        });


        binding.recyclerRadioFav.setAdapter(radioItemAdapter);

    }
}