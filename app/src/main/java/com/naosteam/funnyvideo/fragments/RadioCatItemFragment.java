package com.naosteam.funnyvideo.fragments;

import android.graphics.drawable.Drawable;
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
import android.widget.SearchView;
import android.widget.Toast;

import com.naosteam.funnyvideo.R;
import com.naosteam.funnyvideo.adapters.RadioItemAdapter;
import com.naosteam.funnyvideo.asynctasks.LoadRadioCatItemAsync;
import com.naosteam.funnyvideo.databinding.FragmentRadioBinding;
import com.naosteam.funnyvideo.databinding.FragmentRadioCatItemBinding;
import com.naosteam.funnyvideo.listeners.ControlRadioListener;
import com.naosteam.funnyvideo.listeners.LoadRadioCatItemAsyncListener;
import com.naosteam.funnyvideo.listeners.OnRadioClickListeners;
import com.naosteam.funnyvideo.listeners.OnUpdateViewRadioPlayListener;
import com.naosteam.funnyvideo.models.Category_M;
import com.naosteam.funnyvideo.models.Videos_M;
import com.naosteam.funnyvideo.utils.AdsManager;
import com.naosteam.funnyvideo.utils.Constant;
import com.naosteam.funnyvideo.utils.Methods;
import com.naosteam.funnyvideo.utils.PlayerRadio;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class RadioCatItemFragment extends Fragment {

    private View rootView;
    private NavController navController;
    private FragmentRadioCatItemBinding binding;
    private static ArrayList<Videos_M> mRadios;
    private static int index_selected = -1;
    private PlayerRadio playerRadio;
    private Category_M cat;
    private RadioItemAdapter radioItemAdapter;

    public void nextRadio(){
        if(index_selected == mRadios.size() - 1){
            index_selected = 0;
        } else {
            index_selected = index_selected + 1;
        }
        Constant.Radio_Listening = mRadios.get(index_selected);
    }

    public void previousRadio(){
        if(index_selected == 0 || index_selected == -1){
            index_selected = mRadios.size() - 1;
        } else {
            index_selected = index_selected - 1;
        }
        Constant.Radio_Listening = mRadios.get(index_selected);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRadioCatItemBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);
        radioItemAdapter = null;

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

        if(mRadios == null){
            mRadios = new ArrayList<>();
        }

        LoadData();

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

    private void LoadData(){
        cat = (Category_M) getArguments().getSerializable("category");

        Picasso.get().load(cat.getCat_image()).error(R.drawable.image_offline).into(binding.imvCatRadio);
        binding.tvCatName.setText(cat.getCat_name());
        Bundle bundle = new Bundle();
        bundle.putInt("cat_id",cat.getCat_id());

        Picasso.get().load(cat.getCat_image()).into(binding.imvBackground);

        RequestBody requestBody = Methods.getInstance().GetRadioRequestBody("LOAD_RADIOS_OF_CATEGORY", bundle);
        LoadRadioCatItemAsyncListener listener = new LoadRadioCatItemAsyncListener() {
            @Override
            public void onStart() {
                binding.progressCircular.setVisibility(View.VISIBLE);

            }

            @Override
            public void onEnd(boolean status, ArrayList<Videos_M> arrayList_radios) {
                if(getContext() != null){
                    if(Methods.getInstance().isNetworkConnected(getContext())){
                        if(status){
                            mRadios.clear();
                            mRadios.addAll(arrayList_radios);

                            binding.progressCircular.setVisibility(View.GONE);

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

        LoadRadioCatItemAsync async = new LoadRadioCatItemAsync(requestBody, listener, Methods.getInstance());
        async.execute();
    }

    private void updateUI() {
        binding.rclCatRadioItem.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        ConstraintLayout.LayoutParams layoutParams= new ConstraintLayout.LayoutParams((int)Math.round(width), (int)Math.round(width*0.17));
        layoutParams.setMargins(0,20,0,20);

        binding.imvBackToRadio.setOnClickListener(v->{
            AdsManager.showAdmobInterAd(getActivity(), new AdsManager.InterAdsListener() {
                @Override
                public void onClick() {
                    navController.navigate(R.id.radio_cat_item_to_radio_screen);
                }
            });

        });

        radioItemAdapter = new RadioItemAdapter(layoutParams, mRadios, new OnRadioClickListeners() {
            @Override
            public void onClick(int position) {
                index_selected = position;
                Constant.Radio_Listening = mRadios.get(position);
                playerRadio.startRadio();
                binding.tvRadioListeningName.setText(Constant.Radio_Listening.getVid_title());
                Picasso.get().load(Constant.Radio_Listening.getVid_thumbnail()).into(binding.imvRadioListening);
            }
        });


        binding.rclCatRadioItem.setAdapter(radioItemAdapter);

        binding.itemRadioListening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constant.Radio_Listening.getCat_id()!=-1)
                {
                    AdsManager.showAdmobInterAd(getActivity(), new AdsManager.InterAdsListener() {
                        @Override
                        public void onClick() {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("category", cat);
                            RadioDetailsFragment.setControlRadioListener(new ControlRadioListener() {
                                @Override
                                public void onNext() {
                                    nextRadio();
                                }
                                @Override
                                public void onPrevious() {
                                    previousRadio();
                                }
                            }
                            );
                            bundle.putString("from","from_cat_item");
                            navController.navigate(R.id.radio_cat_item_to_radio_detail, bundle);
                        }
                    });

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

        binding.searchViewCatitem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                onSearch(newText);
                if(newText.length() == 0){
                    radioItemAdapter.setList_Radio(mRadios);
                    radioItemAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
    }

    private void onSearch(String text){
        ArrayList<Videos_M> list_search = new ArrayList<>();
        for(Videos_M i : mRadios){
            if(i.getVid_title().toLowerCase().contains(text.toLowerCase()))
                list_search.add(i);
        }
        if(list_search.isEmpty()) {
            if (text.length() > 0){}
                //Toast.makeText(getActivity(), "No Radio Found", Toast.LENGTH_SHORT).show();
        } else {
            radioItemAdapter.setList_Radio(list_search);
            radioItemAdapter.notifyDataSetChanged();
        }
    }

}