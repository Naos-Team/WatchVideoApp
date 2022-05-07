package com.naosteam.watchvideoapp.fragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.adapters.SlideShowHomeFragAdapter;
import com.naosteam.watchvideoapp.databinding.FragmentHomeBinding;
import com.naosteam.watchvideoapp.listeners.SlideClickListeners;
import com.naosteam.watchvideoapp.models.Category_M;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private NavController navController;
    private FragmentHomeBinding binding;
    private SlideShowHomeFragAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        navController = NavHostFragment.findNavController(this);
        setUp();
//        tv_home = rootView.findViewById(R.id.tv_home);
//        tv_home.setOnClickListener(v -> {
////            Bundle bundle = new Bundle();
////            bundle.putInt("amount", 15);
//            navController.navigate(R.id.HomeToRadio);
//        });

        return  rootView;
    }

    public void setUp(){
        ArrayList<Category_M> list = new ArrayList<>();
        list.add(
                new Category_M(1,"Ten", "https://d1j8r0kxyu9tj8.cloudfront.net/images/1566809284X4pyEDCj7CFMsGu.jpg", 1)
        );
        list.add(
                new Category_M(1,"Ten", "https://intphcm.com/data/upload/mau-poster-hinh-anh-lon-phim-mat-biec.jpg", 1)
        );
        list.add(
                new Category_M(1,"Ten", "https://upload.wikimedia.org/wikipedia/vi/3/32/Poster_phim_T%C3%AAn_b%E1%BA%A1n_l%C3%A0_g%C3%AC.jpg", 1)
        );
        list.add(
                new Category_M(1,"Ten", "https://d1j8r0kxyu9tj8.cloudfront.net/images/1566809340Y397jnilYDd15KN.jpg", 1)
        );
        adapter = new SlideShowHomeFragAdapter(list, new SlideClickListeners() {
            @Override
            public void onClick_SlideShow(int position) {

            }
        });

        binding.vpgSlideHomeFrag.setAdapter(adapter);
        binding.indicatorHomeFrag.setViewPager(binding.vpgSlideHomeFrag);

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(getActivity().getResources().
                getDisplayMetrics().widthPixels, getActivity().getResources().getDisplayMetrics().widthPixels);
        binding.vpgSlideHomeFrag.setLayoutParams(layoutParams);
    }
}