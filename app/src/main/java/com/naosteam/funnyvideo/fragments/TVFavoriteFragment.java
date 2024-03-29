package com.naosteam.funnyvideo.fragments;

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

import com.google.firebase.auth.FirebaseAuth;
import com.naosteam.funnyvideo.R;
import com.naosteam.funnyvideo.adapters.TVFragmentAdapter;
import com.naosteam.funnyvideo.asynctasks.LoadFavoriteListAsync;
import com.naosteam.funnyvideo.databinding.FragmentRadioFavoriteBinding;
import com.naosteam.funnyvideo.databinding.FragmentTVFavoriteBinding;
import com.naosteam.funnyvideo.listeners.FavoriteToDetailListener;
import com.naosteam.funnyvideo.listeners.LoadSearchVideoAsyncListener;
import com.naosteam.funnyvideo.listeners.OnHomeItemClickListeners;
import com.naosteam.funnyvideo.models.Videos_M;
import com.naosteam.funnyvideo.utils.Methods;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class TVFavoriteFragment extends Fragment {
    private View rootView;
    private FragmentTVFavoriteBinding binding;
    private ArrayList<Videos_M> arrayListfav, mArrayList;
    private TVFragmentAdapter tvFragmentAdapter;
    private NavController navController;
    private FavoriteToDetailListener listener;
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

    public TVFavoriteFragment() {
    }

    public TVFavoriteFragment(FavoriteToDetailListener listener) {
        this.listener = listener;
    }

    private void LoadData(){
        Bundle bundle = new Bundle();
        bundle.putInt("vid_type",2);
        bundle.putString("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
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
                            binding.progress.setVisibility(View.GONE);
                            if(arrayListfav==null){
                                binding.tvNoResult.setVisibility(View.VISIBLE);
                                binding.recyclerFavTv.setVisibility(View.GONE);
                            }
                            else{

                                arrayListfav.addAll(arrayList_fav);
                                mArrayList = arrayListfav;
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

    public void searchTV(String text){
        if (arrayListfav!=null){
            if(text.length()!=0){
                ArrayList<Videos_M> list_search = new ArrayList<>();
                for(Videos_M i : arrayListfav){
                    if(i.getVid_title().toLowerCase().contains(text.toLowerCase()))
                        list_search.add(i);
                }
                if(list_search.isEmpty()) {
                    if (text.length() > 0){}
                    //Toast.makeText(getActivity(), "No TV Found", Toast.LENGTH_SHORT).show();
                } else {
                    tvFragmentAdapter.setList_TV(list_search);
                    tvFragmentAdapter.notifyDataSetChanged();
                }
            }
            else{
                tvFragmentAdapter.setList_TV(mArrayList);
                tvFragmentAdapter.notifyDataSetChanged();
                //LoadData();
            }
        }

    }

    private void updateUI() {
        binding.recyclerFavTv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams((int) Math.round(width*0.45), (int) Math.round(width * 0.4));
        layoutParams.setMargins(20, 10, 20, 10);

        tvFragmentAdapter = new TVFragmentAdapter(arrayListfav, layoutParams, new OnHomeItemClickListeners() {
            @Override
            public void onClick_homeItem(int position) {
                try{
                    Bundle bundle = new Bundle();
                    bundle.putString("url", arrayListfav.get(position).getVid_url());
                    bundle.putString("url_img", arrayListfav.get(position).getVid_thumbnail());
                    bundle.putString("des", arrayListfav.get(position).getVid_description());
                    bundle.putBoolean("isFavorite", true);
                    bundle.putInt("id", arrayListfav.get(position).getVid_id());
                    listener.onDirect(2, bundle);
                }
                catch (Exception e){
                    e.printStackTrace();
                }


            }
        });
        binding.recyclerFavTv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.recyclerFavTv.setAdapter(tvFragmentAdapter);

    }
}