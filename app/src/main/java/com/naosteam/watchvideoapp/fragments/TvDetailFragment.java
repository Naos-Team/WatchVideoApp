package com.naosteam.watchvideoapp.fragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.activities.MainActivity;
import com.naosteam.watchvideoapp.databinding.FragmentTvDeltailBinding;
import com.naosteam.watchvideoapp.listeners.OnHomeItemClickListeners;
import com.squareup.picasso.Picasso;

public class TvDetailFragment extends Fragment {

    private FragmentTvDeltailBinding binding;
    private NavController navController;
    private SimpleExoPlayer player;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTvDeltailBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        setUp();

        return rootView;
    }

    private void setUp(){
        MainActivity.hide_Navi();
        getActivity().setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        navController = NavHostFragment.findNavController(this);

        binding.videoTvDetailFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state = -1;
                if(binding.layoutInfTvDetailFrag.getVisibility() == View.VISIBLE){
                    state = View.GONE;
                } else {
                    state = View.VISIBLE;
                }
                binding.layoutInfTvDetailFrag.setVisibility(state);
                binding.btnOutTvDetailFrag.setVisibility(state);
            }
        });

        Bundle bundle = getArguments();

        Picasso.get().load(bundle.getString("url_img")).into(binding.imgTvDetailFrag);
        binding.txtTvDetailFrag.setText(bundle.getString("des"));

        binding.btnOutTvDetailFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bundle.getBoolean("isHome")){
                    navController.navigate(R.id.TVDetail_to_HomeFrag);
                } else {
                    navController.navigate(R.id.fromDetailtoTVFrag);
                }
            }
        });

        player = new SimpleExoPlayer.Builder(getContext()).build();
        binding.videoTvDetailFrag.setPlayer(player);
        MediaItem mediaItem = MediaItem.fromUri(bundle.getString("url"));
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        player.stop();
        MainActivity.show_Navi();
        getActivity().setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}