package com.naosteam.funnyvideo.fragments;

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

import com.google.firebase.auth.FirebaseAuth;
import com.naosteam.funnyvideo.R;
import com.naosteam.funnyvideo.adapters.RadioItemAdapter;
import com.naosteam.funnyvideo.asynctasks.LoadFavoriteListAsync;
import com.naosteam.funnyvideo.databinding.FragmentRadioFavoriteBinding;
import com.naosteam.funnyvideo.databinding.FragmentVideoFavoriteBinding;
import com.naosteam.funnyvideo.listeners.FavoriteToDetailListener;
import com.naosteam.funnyvideo.listeners.LoadSearchVideoAsyncListener;
import com.naosteam.funnyvideo.listeners.OnRadioClickListeners;
import com.naosteam.funnyvideo.models.Videos_M;
import com.naosteam.funnyvideo.utils.Constant;
import com.naosteam.funnyvideo.utils.Methods;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class RadioFavoriteFragment extends Fragment {
    private View rootView;
    private FragmentRadioFavoriteBinding binding;
    private ArrayList<Videos_M> arrayList_fav, mArrayList;
    private RadioItemAdapter radioItemAdapter;
    private NavController navController;
    private FavoriteToDetailListener listener;
    private static int index_selected = -1;

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

    public RadioFavoriteFragment() {
    }

    public RadioFavoriteFragment(FavoriteToDetailListener listener) {
        this.listener = listener;
    }

    public void searchRadio(String text){
        if (arrayList_fav!=null) {
            if (text.length() != 0) {
                ArrayList<Videos_M> list_search = new ArrayList<>();
                for (Videos_M i : arrayList_fav) {
                    if (i.getVid_title().toLowerCase().contains(text.toLowerCase()))
                        list_search.add(i);
                }
                if (list_search.isEmpty()) {
                    if (text.length() > 0) {
                    }
                    //Toast.makeText(getActivity(), "No Radio Found", Toast.LENGTH_SHORT).show();
                } else {
                    radioItemAdapter.setList_Radio(list_search);
                    radioItemAdapter.notifyDataSetChanged();
                }
            } else {
                //LoadData();
                radioItemAdapter.setList_Radio(mArrayList);
                radioItemAdapter.notifyDataSetChanged();
            }
        }

    }

    private void LoadData(){
            Bundle bundle = new Bundle();
            bundle.putInt("vid_type",3);
            bundle.putString("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
            RequestBody requestBody = Methods.getInstance().getVideoRequestBody("GET_FAV_DATA", bundle);
            LoadSearchVideoAsyncListener listener = new LoadSearchVideoAsyncListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onEnd(boolean status, ArrayList<Videos_M> arrayListfav) {
                    if(getContext() != null){
                        if(Methods.getInstance().isNetworkConnected(getContext())){
                            if(status){
                                binding.progress.setVisibility(View.GONE);
                                if(arrayListfav==null){
                                    binding.tvNoResult.setVisibility(View.VISIBLE);
                                    binding.recyclerRadioFav.setVisibility(View.GONE);
                                }
                                else{
                                    arrayList_fav.addAll(arrayListfav);
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

    private void updateUI() {
        binding.recyclerRadioFav.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams((int) Math.round(width), (int) Math.round(width * 0.17));
        layoutParams.setMargins(0, 20, 0, 20);

        radioItemAdapter = new RadioItemAdapter(layoutParams, arrayList_fav, new OnRadioClickListeners() {
            @Override
            public void onClick(int position) {
                Constant.Radio_Listening = arrayList_fav.get(position);
                index_selected = position;
                Bundle bundle = new Bundle();
                bundle.putSerializable("radio", Constant.Radio_Listening );
                bundle.putString("from", "from_favorite");
                listener.onDirect(3, bundle);

            }
        });

        binding.recyclerRadioFav.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL ,false));
        binding.recyclerRadioFav.setAdapter(radioItemAdapter);

    }

    public void nextRadio(){
        if(index_selected == arrayList_fav.size() - 1){
            index_selected = 0;
        } else {
            index_selected = index_selected + 1;
        }
        Constant.Radio_Listening = arrayList_fav.get(index_selected);
    }

    public void previousRadio(){
        if(index_selected == 0 || index_selected == -1){
            index_selected = arrayList_fav.size() - 1;
        } else {
            index_selected = index_selected - 1;
        }
        Constant.Radio_Listening = arrayList_fav.get(index_selected);
    }
}