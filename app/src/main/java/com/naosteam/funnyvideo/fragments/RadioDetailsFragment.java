package com.naosteam.funnyvideo.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.firebase.auth.FirebaseAuth;
import com.naosteam.funnyvideo.R;
import com.naosteam.funnyvideo.activities.MainActivity;
import com.naosteam.funnyvideo.asynctasks.ExecuteQueryAsync;
import com.naosteam.funnyvideo.databinding.FragmentRadioDetailsBinding;
import com.naosteam.funnyvideo.listeners.ControlRadioListener;
import com.naosteam.funnyvideo.listeners.ExecuteQueryAsyncListener;
import com.naosteam.funnyvideo.listeners.OnUpdateViewRadioPlayListener;
import com.naosteam.funnyvideo.listeners.CheckFavListener;
import com.naosteam.funnyvideo.listeners.SetFavListener;
import com.naosteam.funnyvideo.models.Videos_M;
import com.naosteam.funnyvideo.utils.AdsManager;
import com.naosteam.funnyvideo.utils.Constant;
import com.naosteam.funnyvideo.utils.Methods;
import com.naosteam.funnyvideo.utils.PlayerRadio;
import com.squareup.picasso.Picasso;

import okhttp3.RequestBody;

public class RadioDetailsFragment extends Fragment {


    private View rootView;
    private NavController navController;
    private FragmentRadioDetailsBinding binding;
    private Videos_M radio;
    private ExoPlayer player;
    private Boolean mIsFav = false;
    private PlayerRadio playerRadio;
    private static ControlRadioListener controlRadioListener;

    public static void setControlRadioListener(ControlRadioListener controlRadioListener) {
        RadioDetailsFragment.controlRadioListener = controlRadioListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRadioDetailsBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);

//        binding.progressRadioDetail.setVisibility(View.VISIBLE);
        binding.progressRadioDetail.setIndeterminateDrawableTiled(new ThreeBounce());


        playerRadio = PlayerRadio.getInstance(new OnUpdateViewRadioPlayListener() {
            @Override
            public void onBuffering() {
                binding.imvPlayRadio.setClickable(false);
                binding.imvPlayRadio.setImageResource(R.drawable.ic_play_radio);
                Methods.getInstance().checkVideoFav(getContext(), Constant.Radio_Listening.getVid_id(), new CheckFavListener() {
                    @Override
                    public void onComplete(boolean isSuccess, boolean isFav) {
                        if(isSuccess){
                            mIsFav = isFav;
                        }
                        updateFav();
                    }
                });

                binding.imvBg.setVisibility(View.VISIBLE);

                binding.progressRadioDetail.setVisibility(View.VISIBLE);

            }

            @Override
            public void onReady() {
                binding.imvBg.setVisibility(View.GONE);

                binding.progressRadioDetail.setVisibility(View.GONE);
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

        Methods.getInstance().checkVideoFav(getContext(), Constant.Radio_Listening.getVid_id(), new CheckFavListener() {
            @Override
            public void onComplete(boolean isSuccess, boolean isFav) {
                if(isSuccess){
                    mIsFav = isFav;
                }
                updateFav();
            }
        });

        return rootView;
    }

    private void LoadData(){

//        player = RadioFragment.getPlayer();
//        RadioFragment.setListener(listener);
        updateView();

//        controlRadioListener = (ControlRadioListener)
//                getArguments().getSerializable("listener");

        if(getArguments().getString("from").equals("from_home_screen")){
            playerRadio.startRadio();
        }

        PlayerRadio.setPlayer(binding.playerViewRadioDetails);
        binding.playerViewRadioDetails.showController();
        binding.playerViewRadioDetails.setControllerHideOnTouch(false);
        binding.playerViewRadioDetails.setControllerShowTimeoutMs(0);
        int img_playbtn = (playerRadio.checkPlay()) ? R.drawable.ic_pause_radio : R.drawable.ic_play_radio;
        binding.imvPlayRadio.setImageResource(img_playbtn);

        if(mIsFav){
            Picasso.get()
                    .load(R.drawable.ic_heart4_check)
                    .into(binding.radioDetailFav);
        }else{
            Picasso.get()
                    .load(R.drawable.ic_heart4_uncheckpng)
                    .into(binding.radioDetailFav);
        }

        binding.radioDetailFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.getInstance().setFavState(getContext(), radio.getVid_id(), !mIsFav, new SetFavListener() {
                    @Override
                    public void onComplete(boolean isSuccess) {
                        if(isSuccess){

                            mIsFav = !mIsFav;
                            if(mIsFav){
                                Picasso.get()
                                        .load(R.drawable.ic_heart4_check)
                                        .into(binding.radioDetailFav);
                            }else{
                                Picasso.get()
                                        .load(R.drawable.ic_heart4_uncheckpng)
                                        .into(binding.radioDetailFav);
                            }
                        }
                    }
                });
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
                AdsManager.showAdmobInterAd(getActivity(), new AdsManager.InterAdsListener() {
                    @Override
                    public void onClick() {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("category", getArguments().getSerializable("category"));
                        navController.navigate(R.id.raio_detail_to_radio_cat_item, bundle);
                    }
                });
            }
            else if(getArguments().getString("from").equals("from_radio_screen")){
                AdsManager.showAdmobInterAd(getActivity(), new AdsManager.InterAdsListener() {
                    @Override
                    public void onClick() {
                        navController.navigate(R.id.radio_detail_to_radio_screen);
                    }
                });

            } else if(getArguments().getString("from").equals("from_home_screen")){
                AdsManager.showAdmobInterAd(getActivity(), new AdsManager.InterAdsListener() {
                    @Override
                    public void onClick() {
                        navController.navigate(R.id.radioDetail_to_homeFrag);
                        MainActivity.choice_Navi(R.id.baseRadioFragment);
                    }
                });

            }
            else if (getArguments().getString("from").equals("from_favorite")){
                AdsManager.showAdmobInterAd(getActivity(), new AdsManager.InterAdsListener() {
                    @Override
                    public void onClick() {
                        navController.navigate(R.id.radio_detail_to_favorite);
                    }
                });

            }
        });

        binding.radioDetailReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ;if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                    showReportDialog();
                }
                else{
                    Toast.makeText(getContext(), "Please login first!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showReportDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.dialog_report_layout, null,false);
        builder.setView(view1);
        builder.setCancelable(false);

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ConstraintLayout cs_main = view1.findViewById(R.id.cs_main);
        Button btn_submit = view1.findViewById(R.id.btn_submit);
        Button btn_cancel = view1.findViewById(R.id.btn_cancel);
        EditText edt_report_content = view1.findViewById(R.id.edt_report_content);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        try{
            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String content = edt_report_content.getText().toString();
                    ExecuteQueryAsyncListener listener = new ExecuteQueryAsyncListener() {
                        @Override
                        public void onStart() {}

                        @Override
                        public void onEnd(boolean status) {
                            if(status){
                            }else{
                                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                    };

                    Bundle bundle = new Bundle();
                    bundle.putString("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    bundle.putString("report_content",content);
                    bundle.putInt("vid_id", radio.getVid_id());

                    RequestBody requestBody = Methods.getInstance().getReportRequestBody("INSERT_REPORT",bundle);
                    ExecuteQueryAsync async = new ExecuteQueryAsync(requestBody, listener);
                    async.execute();
                    alertDialog.dismiss();
                    Toast.makeText(getContext(), "Thank you for your report!", Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (Exception e){
            Toast.makeText(getContext(), "Please input your report!", Toast.LENGTH_SHORT).show();
        }

        alertDialog.show();

    }

    private void updateFav(){
        if(mIsFav){
            Picasso.get()
                    .load(R.drawable.ic_heart4_check)
                    .into(binding.radioDetailFav);
        }else{
            Picasso.get()
                    .load(R.drawable.ic_heart4_uncheckpng)
                    .into(binding.radioDetailFav);
        }
    }

    private void updateView(){

        radio = Constant.Radio_Listening;
        Picasso.get().load(radio.getVid_thumbnail()).into(binding.imvRadio);
        binding.tvRadioName.setText(radio.getVid_title());
    }
}