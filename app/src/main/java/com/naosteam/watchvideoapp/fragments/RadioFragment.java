package com.naosteam.watchvideoapp.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.adapters.RadioCategoryAdapter;
import com.naosteam.watchvideoapp.adapters.RadioItemAdapter;
import com.naosteam.watchvideoapp.asynctasks.LoadRadioAsync;
import com.naosteam.watchvideoapp.databinding.FragmentRadioBinding;
import com.naosteam.watchvideoapp.listeners.ControlRadioListener;
import com.naosteam.watchvideoapp.listeners.LoadRadioAsyncListener;
import com.naosteam.watchvideoapp.listeners.OnRadioCatClickListeners;
import com.naosteam.watchvideoapp.listeners.OnRadioClickListeners;
import com.naosteam.watchvideoapp.listeners.OnUpdateViewRadioPlayListener;
import com.naosteam.watchvideoapp.models.Category_M;
import com.naosteam.watchvideoapp.models.Videos_M;
import com.naosteam.watchvideoapp.utils.Constant;
import com.naosteam.watchvideoapp.utils.Methods;
import com.naosteam.watchvideoapp.utils.PlayerRadio;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class RadioFragment extends Fragment {

    private View rootView;
    private NavController navController;
    private FragmentRadioBinding binding;
    private static ArrayList<Videos_M> mTrendings;
    private static ArrayList<Category_M> mCats;
    private static boolean first_time = true;
    private RadioCategoryAdapter categoryAdapter;
    private PlayerRadio playerRadio;
    private static int index_selected = -1;

    public void nextRadio(){
        if(index_selected == mTrendings.size() - 1){
            index_selected = 0;
        } else {
            index_selected = index_selected + 1;
        }
        Constant.Radio_Listening = mTrendings.get(index_selected);
    }

    public void previousRadio(){
        if(index_selected == 0 || index_selected == -1){
            index_selected = mTrendings.size() - 1;
        } else {
            index_selected = index_selected - 1;
        }
        Constant.Radio_Listening = mTrendings.get(index_selected);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRadioBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);
        categoryAdapter = null;

        if(first_time) {
            mTrendings = new ArrayList<>();
            mCats = new ArrayList<>();
            LoadData();
            first_time = false;
        } else {
            updateUI();
        }

        playerRadio = PlayerRadio.getInstance(new OnUpdateViewRadioPlayListener() {
            @Override
            public void onBuffering() {
                binding.prgRadioLoadRadioDetailFrag.setVisibility(View.VISIBLE);
                binding.itemRadioListening.setClickable(false);
                binding.imvPlayListening.setClickable(false);
                binding.imvPlayListening.setImageResource(R.drawable.ic_play_radio);
            }

            @Override
            public void onReady() {
                binding.prgRadioLoadRadioDetailFrag.setVisibility(View.GONE);
                binding.itemRadioListening.setClickable(true);
                binding.imvPlayListening.setClickable(true);
                binding.imvPlayListening.setImageResource(R.drawable.ic_pause_radio);
            }

            @Override
            public void onEnd() {
                nextRadio();
                binding.tvRadioListeningName.setText(Constant.Radio_Listening.getVid_title());
                Picasso.get().load(Constant.Radio_Listening.getVid_thumbnail()).into(binding.imvRadioListening);
                playerRadio.startRadio();
            }
        });

        int img_play_btn = (playerRadio.checkPlay()) ? R.drawable.ic_pause_radio :
                R.drawable.ic_play_radio;
        binding.imvPlayListening.setImageResource(img_play_btn);

        if(Constant.Radio_Listening.getCat_id()==-1){
            binding.tvRadioListeningName.setText("Radio Name");
            Drawable res = getResources().getDrawable(R.drawable.default_radio);
            binding.imvRadioListening.setImageDrawable(res);
        }
        else{
            binding.tvRadioListeningName.setText(Constant.Radio_Listening.getVid_title());
            Picasso.get().load(Constant.Radio_Listening.getVid_thumbnail()).into(binding.imvRadioListening);
        }

        return rootView;
    }

    private void updateUI() {

        binding.rclCatRadioFrag.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false));
        binding.rclRadioTrending.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        binding.swiperRadioFrag.setColorSchemeColors(getResources().getColor(R.color.neonGreen));
        binding.swiperRadioFrag.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadData();
                binding.swiperRadioFrag.setRefreshing(false);
            }
        });

        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams((int)Math.round(width*0.45), (int)Math.round(width*0.4));
        layoutParams.setMargins(20,20,20,20);

        ConstraintLayout.LayoutParams layoutParams1 = new ConstraintLayout.LayoutParams((int)Math.round(width), (int)Math.round(width*0.17));
        layoutParams1.setMargins(0,20,0,20);

        categoryAdapter = new RadioCategoryAdapter(layoutParams, mCats, new OnRadioCatClickListeners() {
            @Override
            public void onClick(Category_M category) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("category", category);
                navController.navigate(R.id.radio_screen_to_cat_item, bundle);
            }
        });

        binding.rclCatRadioFrag.setAdapter(categoryAdapter);

        binding.rclRadioTrending.setAdapter(new RadioItemAdapter(layoutParams1, mTrendings, new OnRadioClickListeners() {
            @Override
            public void onClick(int position) {
                index_selected = position;
                Constant.Radio_Listening = mTrendings.get(position);
                playerRadio.startRadio();
                binding.tvRadioListeningName.setText(Constant.Radio_Listening.getVid_title());
                Picasso.get().load(Constant.Radio_Listening.getVid_thumbnail()).into(binding.imvRadioListening);
            }
        }));

        binding.itemRadioListening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constant.Radio_Listening.getCat_id()!=-1)
                {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("listener", new ControlRadioListener() {
                        @Override
                        public void onNext() {
                            nextRadio();
                        }

                        @Override
                        public void onPrevious() {
                            previousRadio();
                        }
                    });
                    bundle.putString("from","from_radio_screen");
                    navController.navigate(R.id.radio_screen_to_radio_detail, bundle);
                }
                else{
                    Toast.makeText(getActivity(), "No Radio Playing", Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.imvPlayListening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constant.Radio_Listening.getVid_id() == -1){
                    return;
                }
                if(playerRadio.checkPlay()){
                    binding.imvPlayListening.setImageResource(R.drawable.ic_play_radio);
                    playerRadio.pauseRadio();
                } else {
                    binding.imvPlayListening.setImageResource(R.drawable.ic_pause_radio);
                    playerRadio.playRadio();
                }
            }
        });

        binding.imvNextListening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextRadio();
                playerRadio.startRadio();
                binding.tvRadioListeningName.setText(Constant.Radio_Listening.getVid_title());
                Picasso.get().load(Constant.Radio_Listening.getVid_thumbnail()).into(binding.imvRadioListening);
            }
        });

        binding.imvBackListening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousRadio();
                playerRadio.startRadio();
                binding.tvRadioListeningName.setText(Constant.Radio_Listening.getVid_title());
                Picasso.get().load(Constant.Radio_Listening.getVid_thumbnail()).into(binding.imvRadioListening);
            }
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                onSearch(newText);
                if(newText.length() == 0){
                    categoryAdapter.LoadList_Cat(mCats);
                    categoryAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });

    }

    private void LoadData(){
        RequestBody requestBody = Methods.getInstance().GetRadioRequestBody("LOAD_RADIO_SCREEN", null);
        LoadRadioAsyncListener listener = new LoadRadioAsyncListener() {
            @Override
            public void onStart() {
                binding.imgTempRadioFrag1.setVisibility(View.VISIBLE);
                binding.progressCircular1.setVisibility(View.VISIBLE);

            }

            @Override
            public void onEnd(boolean status, ArrayList<Videos_M> arrayList_trending, ArrayList<Category_M> arrayList_category) {
                if(getContext() != null){
                    if(Methods.getInstance().isNetworkConnected(getContext())){
                        if(status){
                            mTrendings.clear();
                            mTrendings.addAll(arrayList_trending);
                            mCats.clear();
                            mCats.addAll(arrayList_category);

                            binding.imgTempRadioFrag1.setVisibility(View.GONE);
                            binding.progressCircular1.setVisibility(View.GONE);
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

    private void onSearch(String text){
        ArrayList<Category_M> list_search = new ArrayList<>();
        for(Category_M i : mCats){
            if(i.getCat_name().toLowerCase().contains(text.toLowerCase()))
                list_search.add(i);
        }
        if(list_search.isEmpty()) {
            if (text.length() > 0)
                Toast.makeText(getActivity(), "No Radio Category Found", Toast.LENGTH_SHORT).show();
        } else {
            categoryAdapter.LoadList_Cat(list_search);
            categoryAdapter.notifyDataSetChanged();
        }
    }

}