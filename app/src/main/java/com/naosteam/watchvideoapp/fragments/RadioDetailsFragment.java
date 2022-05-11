package com.naosteam.watchvideoapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.activities.MainActivity;
import com.naosteam.watchvideoapp.databinding.FragmentRadioDetailsBinding;
import com.naosteam.watchvideoapp.listeners.ControlRadioListener;
import com.naosteam.watchvideoapp.listeners.OnUpdateViewRadioPlayListener;
import com.naosteam.watchvideoapp.models.Videos_M;
import com.naosteam.watchvideoapp.utils.Constant;
import com.naosteam.watchvideoapp.utils.PlayerRadio;
import com.squareup.picasso.Picasso;

public class RadioDetailsFragment extends Fragment {


    private View rootView;
    private NavController navController;
    private FragmentRadioDetailsBinding binding;
    private Videos_M radio;
    private PlayerRadio playerRadio;
    private ControlRadioListener controlRadioListener;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRadioDetailsBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);
        playerRadio = PlayerRadio.getInstance(new OnUpdateViewRadioPlayListener() {
            @Override
            public void onBuffering() {
                binding.imvPlayRadio.setClickable(false);
                binding.imvPlayRadio.setImageResource(R.drawable.ic_play_radio);
            }

            @Override
            public void onReady() {
                binding.imvPlayRadio.setClickable(true);
                binding.imvPlayRadio.setImageResource(R.drawable.ic_pause_radio);
            }

            @Override
            public void onEnd() {
                controlRadioListener.onNext();
                updateView();
                playerRadio.startRadio();
            }
        });
        LoadData();

        return rootView;
    }

    private void LoadData(){

//        player = RadioFragment.getPlayer();
//        RadioFragment.setListener(listener);
        updateView();

        controlRadioListener = (ControlRadioListener)
                getArguments().getSerializable("listener");

        if(getArguments().getString("from").equals("from_home_screen")){
            playerRadio.startRadio();
        }

        PlayerRadio.setPlayer(binding.playerViewRadioDetails);
        binding.playerViewRadioDetails.showController();
        binding.playerViewRadioDetails.setControllerHideOnTouch(false);
        binding.playerViewRadioDetails.setControllerShowTimeoutMs(0);
        int img_playbtn = (playerRadio.checkPlay()) ? R.drawable.ic_pause_radio : R.drawable.ic_play_radio;
        binding.imvPlayRadio.setImageResource(img_playbtn);

        binding.radioDetailFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        binding.imvNextRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlRadioListener.onNext();
                updateView();
                playerRadio.startRadio();
            }
        });

        binding.imvBackRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlRadioListener.onPrevious();
                updateView();
                playerRadio.startRadio();
            }
        });

        binding.imvPlayRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerRadio.checkPlay()){
                    playerRadio.pauseRadio();
                    binding.imvPlayRadio.setImageResource(R.drawable.ic_play_radio);
                } else {
                    playerRadio.playRadio();
                    binding.imvPlayRadio.setImageResource(R.drawable.ic_pause_radio);
                }
            }
        });

        binding.imvBackRadioDetail.setOnClickListener(v->{
            if(getArguments().getString("from").equals("from_cat_item")) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("category", getArguments().getSerializable("category"));
                navController.navigate(R.id.raio_detail_to_radio_cat_item, bundle);
            }
            else if(getArguments().getString("from").equals("from_radio_screen")){
                navController.navigate(R.id.radio_detail_to_radio_screen);
            } else if(getArguments().getString("from").equals("from_home_screen")){
                navController.navigate(R.id.radioDetail_to_homeFrag);
                MainActivity.choice_Navi(R.id.baseRadioFragment);
            }
        });
    }

    private void updateView(){
        radio = Constant.Radio_Listening;
        Picasso.get().load(radio.getVid_thumbnail()).into(binding.imvRadio);
        binding.tvRadioName.setText(radio.getVid_title());
    }
}