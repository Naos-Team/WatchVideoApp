package com.naosteam.watchvideoapp.fragments;

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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.adapters.FeaturedVideoAdapter;
import com.naosteam.watchvideoapp.asynctasks.LoadFavoriteListAsync;
import com.naosteam.watchvideoapp.databinding.FragmentVideoFavoriteBinding;
import com.naosteam.watchvideoapp.listeners.LoadSearchVideoAsyncListener;
import com.naosteam.watchvideoapp.listeners.OnVideoFeatureClickListener;
import com.naosteam.watchvideoapp.listeners.FavoriteToDetailListener;
import com.naosteam.watchvideoapp.models.Videos_M;
import com.naosteam.watchvideoapp.utils.Methods;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class VideoFavoriteFragment extends Fragment {
    private View rootView;
    private FragmentVideoFavoriteBinding binding;
    private ArrayList<Videos_M> arrayList_fav, mArrayList;
    private FeaturedVideoAdapter videoAdapter;
    private NavController navController;
    private FavoriteToDetailListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVideoFavoriteBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();

        arrayList_fav = new ArrayList<Videos_M>();
        navController = NavHostFragment.findNavController(this);

        LoadData();

        return rootView;
    }

    public VideoFavoriteFragment() {
    }

    public VideoFavoriteFragment(FavoriteToDetailListener listener) {
        this.listener = listener;
    }

    private void LoadData(){
        Bundle bundle = new Bundle();
        bundle.putInt("vid_type",1);
        bundle.putString("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        RequestBody requestBody = Methods.getInstance().getVideoRequestBody("GET_FAV_DATA", bundle);
        LoadSearchVideoAsyncListener listener = new LoadSearchVideoAsyncListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onEnd(boolean status, ArrayList<Videos_M> arrayList) {
                if(getContext() != null){
                    if(Methods.getInstance().isNetworkConnected(getContext())){
                        if(status){
                            binding.progress.setVisibility(View.GONE);
                            if(arrayList==null){
                                binding.tvNoResult.setVisibility(View.VISIBLE);
                                binding.recyclerFavVideo.setVisibility(View.GONE);
                            }
                            else{

                                arrayList_fav.addAll(arrayList);
                                mArrayList = arrayList_fav;
                                updateUI();
                            }

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

    public void searchVideo(String text){
        if (arrayList_fav!=null){
            if(text.length()!=0){
                ArrayList<Videos_M> list_search = new ArrayList<>();
                for(Videos_M i : arrayList_fav){
                    if(i.getVid_title().toLowerCase().contains(text.toLowerCase()))
                        list_search.add(i);
                }
                if(list_search.isEmpty()) {
                    if (text.length() > 0){}
                    //Toast.makeText(getActivity(), "No TV Found", Toast.LENGTH_SHORT).show();
                } else {
                    videoAdapter.setList_Video(list_search);
                    videoAdapter.notifyDataSetChanged();
                }
            }
            else{
                videoAdapter.setList_Video(mArrayList);
                videoAdapter.notifyDataSetChanged();
                //LoadData();

            }
        }
        else{

        }

    }

    private void updateUI() {
        binding.recyclerFavVideo.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) Math.round(width/2*0.8));
        layoutParams.setMargins(10,20,10,20);

        videoAdapter = new FeaturedVideoAdapter(Methods.getInstance(), layoutParams, arrayList_fav, new OnVideoFeatureClickListener() {
            @Override
            public void onClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("video", arrayList_fav.get(position));
                bundle.putBoolean("is_favorite", true);
                listener.onDirect(1, bundle);

            }
        });

        binding.recyclerFavVideo.setLayoutManager(new GridLayoutManager(getContext(), 2));

        binding.recyclerFavVideo.setAdapter(videoAdapter);

    }
}