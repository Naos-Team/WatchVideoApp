package com.naosteam.watchvideoapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.asynctasks.LoadRadioAsync;
import com.naosteam.watchvideoapp.databinding.FragmentRadioCatItemBinding;
import com.naosteam.watchvideoapp.databinding.FragmentRadioDetailsBinding;
import com.naosteam.watchvideoapp.listeners.LoadRadioAsyncListener;
import com.naosteam.watchvideoapp.models.Category_M;
import com.naosteam.watchvideoapp.models.Videos_M;
import com.naosteam.watchvideoapp.utils.Constant;
import com.naosteam.watchvideoapp.utils.Methods;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class RadioDetailsFragment extends Fragment {

    ImageView imv_radio, fav_radio, btn_next, btn_back, btn_back_radio_frag, btn_play;
    TextView tv_radio, tv_start_du, tv_end_du;
    SeekBar seekbar;
    private View rootView;
    private NavController navController;
    private TextView tv_cat_name;
    private RoundedImageView imv_cat;
    private FragmentRadioDetailsBinding binding;
    private Videos_M radio;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRadioDetailsBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);
        LoadData();

        return rootView;
    }

    private void LoadData(){
        radio = (Videos_M) getArguments().getSerializable("radio");
        Picasso.get().load(radio.getVid_thumbnail()).into(binding.imvRadio);
        binding.tvRadioName.setText(radio.getVid_title());

        binding.radioDetailFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.imvNextRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.imvBackRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.imvPlayRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
            }
        });


    }

}