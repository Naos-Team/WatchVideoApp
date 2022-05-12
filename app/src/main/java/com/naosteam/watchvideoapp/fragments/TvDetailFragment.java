package com.naosteam.watchvideoapp.fragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.StyledPlayerControlView;
import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.activities.MainActivity;
import com.naosteam.watchvideoapp.databinding.FragmentTvDeltailBinding;
import com.naosteam.watchvideoapp.listeners.CheckFavListener;
import com.naosteam.watchvideoapp.listeners.OnHomeItemClickListeners;
import com.naosteam.watchvideoapp.listeners.SetFavListener;
import com.naosteam.watchvideoapp.utils.Methods;
import com.squareup.picasso.Picasso;

public class TvDetailFragment extends Fragment {

    private FragmentTvDeltailBinding binding;
    private NavController navController;
    private ExoPlayer player;
    private Boolean mIsFav = false;
    private int id;
    Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTvDeltailBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        bundle = getArguments();
        id = bundle.getInt("id");
        setUp();
        Methods.getInstance().checkVideoFav(getContext(), id, new CheckFavListener() {
            @Override
            public void onComplete(boolean isSuccess, boolean isFav) {
                if(isSuccess){
                    mIsFav = isFav;
                }
                setUpFav();
            }
        });

        return rootView;
    }

    private void setUp(){
        MainActivity.hide_Navi();
        (getActivity()).setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        navController = NavHostFragment.findNavController(this);

        binding.layoutMainDetailFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state = -1;
                if(binding.layoutInfTvDetailFrag.getVisibility() == View.VISIBLE){
                    state = View.GONE;
                    binding.controlViewTVDetailFrag.hide();
                } else {
                    state = View.VISIBLE;
                    binding.controlViewTVDetailFrag.show();
                }
                binding.layoutInfTvDetailFrag.setVisibility(state);
                binding.btnOutTvDetailFrag.setVisibility(state);
            }
        });


        Picasso.get().load(bundle.getString("url_img")).into(binding.imgTvDetailFrag);
        binding.txtTvDetailFrag.setText(bundle.getString("des"));

        binding.txtTvDetailFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.controlViewTVDetailFrag.show();
            }
        });

        binding.btnOutTvDetailFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bundle.getBoolean("isHome")){
                    navController.navigate(R.id.TVDetail_to_HomeFrag);
                } else if(bundle.getBoolean("isFavorite")) {
                    navController.navigate(R.id.tv_detail_to_favorite);
                }
                else{
                    navController.navigate(R.id.fromDetailtoTVFrag);
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.scrollViewTvDetailFrag.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    binding.controlViewTVDetailFrag.show();
                }
            });
        }

        binding.layoutInfTvDetailFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.controlViewTVDetailFrag.show();
            }
        });

        player = new ExoPlayer.Builder(getContext()).build();
        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
                switch (playbackState) {
                    case Player.STATE_BUFFERING:
                        binding.prgTvDetailFrag.setVisibility(View.VISIBLE);
                        binding.imgTempTVDetailFrag.setVisibility(View.VISIBLE);
                        break;
                    case Player.STATE_READY:
                        binding.prgTvDetailFrag.setVisibility(View.GONE);
                        binding.imgTempTVDetailFrag.setVisibility(View.GONE);
                        break;
                    case Player.STATE_ENDED:
                        break;
                }
            }
        });
        binding.videoTvDetailFrag.setUseController(false);
        binding.videoTvDetailFrag.setPlayer(player);

        binding.controlViewTVDetailFrag.addVisibilityListener(new PlayerControlView.VisibilityListener() {
            @Override
            public void onVisibilityChange(int visibility) {
                if(visibility == View.VISIBLE){
                    binding.layoutInfTvDetailFrag.setVisibility(View.VISIBLE);
                    binding.btnOutTvDetailFrag.setVisibility(View.VISIBLE);
                } else {
                    binding.layoutInfTvDetailFrag.setVisibility(View.GONE);
                    binding.btnOutTvDetailFrag.setVisibility(View.GONE);
                }
            }
        });

        binding.controlViewTVDetailFrag.setPlayer(player);
        binding.controlViewTVDetailFrag.setShowNextButton(false);
        binding.controlViewTVDetailFrag.setShowPreviousButton(false);

        MediaItem mediaItem = MediaItem.fromUri(bundle.getString("url"));
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();

        binding.imgLikeTvdetailFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.getInstance().setFavState(getContext(), id, !mIsFav, new SetFavListener() {
                    @Override
                    public void onComplete(boolean isSuccess) {
                        if(isSuccess){
                            mIsFav = !mIsFav;
                            if(mIsFav){
                                Picasso.get()
                                        .load(R.drawable.ic_heart4_check)
                                        .into(binding.imgLikeTvdetailFrag);
                            }else{
                                Picasso.get()
                                        .load(R.drawable.ic_heart4_uncheckpng)
                                        .into(binding.imgLikeTvdetailFrag);
                            }
                        }
                    }
                });
                binding.controlViewTVDetailFrag.show();
            }
        });
    }

    private void setUpFav(){
        if(mIsFav){
            Picasso.get()
                    .load(R.drawable.ic_heart4_check)
                    .into(binding.imgLikeTvdetailFrag);
        }else{
            Picasso.get()
                    .load(R.drawable.ic_heart4_uncheckpng)
                    .into(binding.imgLikeTvdetailFrag);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(player!=null){
            player.stop();
        }
        MainActivity.show_Navi();
        getActivity().setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}