package com.naosteam.watchvideoapp.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.naosteam.watchvideoapp.adapters.FeaturedVideoAdapter;
import com.naosteam.watchvideoapp.adapters.SlideShowHomeFragAdapter;
import com.naosteam.watchvideoapp.adapters.TVFragmentAdapter;
import com.naosteam.watchvideoapp.databinding.FragmentHomeBinding;
import com.naosteam.watchvideoapp.listeners.OnHomeItemClickListeners;
import com.naosteam.watchvideoapp.listeners.OnVideoFeatureClickListener;
import com.naosteam.watchvideoapp.models.Category_M;
import com.naosteam.watchvideoapp.models.Videos_M;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class HomeFragment extends Fragment {
    private NavController navController;
    private ArrayList<Category_M> list_cat_Video;
    private ArrayList<Videos_M> list_video_trending, list_TV_trending;
    private FragmentHomeBinding binding;
    private SlideShowHomeFragAdapter slideShowHomeFragAdapter;
    private FeaturedVideoAdapter featuredVideoAdapter;
    private TVFragmentAdapter tvFragmentAdapter;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int currentPosition = binding.vpgSlideHomeFrag.getCurrentItem();
            if(currentPosition == list_cat_Video.size() - 1){
                binding.vpgSlideHomeFrag.setCurrentItem(0);
            } else {
                binding.vpgSlideHomeFrag.setCurrentItem(currentPosition + 1);
            }
        }
    };

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
        Handler handler = new Handler(Looper.getMainLooper());
        if(runnable != null){
            runnable = null;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int currentPosition = binding.vpgSlideHomeFrag.getCurrentItem();
                if(currentPosition == list_cat_Video.size() - 1){
                    binding.vpgSlideHomeFrag.setCurrentItem(0);
                } else {
                    binding.vpgSlideHomeFrag.setCurrentItem(currentPosition + 1);
                }
            }
        };

        binding.vpgSlideHomeFrag.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                SlideShowHomeFragAdapter.setSelected_index(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 2500);
                slideShowHomeFragAdapter.notifyDataSetChanged();
            }
        });

        LinearLayout.LayoutParams layoutParams_slideShow = new LinearLayout.LayoutParams(getActivity().getResources().
                getDisplayMetrics().widthPixels, 4*(getActivity().getResources().getDisplayMetrics().widthPixels)/3);
        binding.vpg2HomeFrag.setLayoutParams(layoutParams_slideShow);

        LinearLayout.LayoutParams layoutParams_TextView = new LinearLayout.LayoutParams(getActivity().getResources().
                getDisplayMetrics().widthPixels, 5*(getActivity().getResources().getDisplayMetrics().heightPixels)/100);
        binding.txtTrendingVidHomeFrag.setLayoutParams(layoutParams_TextView);
        binding.txtTrendingTVHomeFrag.setLayoutParams(layoutParams_TextView);
        binding.txtTrendingRadHomeFrag.setLayoutParams(layoutParams_TextView);
        list_cat_Video = new ArrayList<>();
        list_cat_Video.add(
                new Category_M(1,"Ten", "https://d1j8r0kxyu9tj8.cloudfront.net/images/1566809284X4pyEDCj7CFMsGu.jpg", 1)
        );
        list_cat_Video.add(
                new Category_M(1,"Ten", "https://intphcm.com/data/upload/mau-poster-hinh-anh-lon-phim-mat-biec.jpg", 1)
        );
        list_cat_Video.add(
                new Category_M(1,"Ten", "https://upload.wikimedia.org/wikipedia/vi/3/32/Poster_phim_T%C3%AAn_b%E1%BA%A1n_l%C3%A0_g%C3%AC.jpg", 1)
        );
        list_cat_Video.add(
                new Category_M(1,"Ten", "https://d1j8r0kxyu9tj8.cloudfront.net/images/1566809340Y397jnilYDd15KN.jpg", 1)
        );
        slideShowHomeFragAdapter = new SlideShowHomeFragAdapter(list_cat_Video, new OnHomeItemClickListeners() {
            @Override
            public void onClick_homeItem(int position) {

            }
        });
        binding.vpgSlideHomeFrag.setAdapter(slideShowHomeFragAdapter);
        binding.indicatorHomeFrag.setViewPager(binding.vpgSlideHomeFrag);


        list_video_trending = new ArrayList<>();
        String strTime = "20:15:40";
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");

        Date d = new Date();
        try {
            d = dateFormat.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        list_video_trending.add(
                new Videos_M(1, 1, "Tenchnology 1",
                        "https://ieltsplanet.info/wp-content/uploads/2017/04/healthcare-technology-8-04-2015.jpg","des",
                        "https://www.youtube.com/watch?v=2OKYsYErfFo", 44, 0, 4.5f, 1,
                        true, d)
        );

        list_video_trending.add(
                new Videos_M(1, 1, "Tenchnology 1",
                        "https://ieltsplanet.info/wp-content/uploads/2017/04/healthcare-technology-8-04-2015.jpg","des",
                        "https://www.youtube.com/watch?v=2OKYsYErfFo", 44, 0, 4.5f, 1,
                        true, d)
        );

        list_video_trending.add(
                new Videos_M(1, 1, "Tenchnology 1",
                        "https://ieltsplanet.info/wp-content/uploads/2017/04/healthcare-technology-8-04-2015.jpg","des",
                        "https://www.youtube.com/watch?v=2OKYsYErfFo", 44, 0, 4.5f, 1,
                        true, d)
        );


        list_video_trending.add(
                new Videos_M(1, 1, "Tenchnology 1",
                        "https://ieltsplanet.info/wp-content/uploads/2017/04/healthcare-technology-8-04-2015.jpg","des",
                        "https://www.youtube.com/watch?v=2OKYsYErfFo", 44, 0, 4.5f, 1,
                        true, d)
        );

        LinearLayout.LayoutParams layoutParams_video_item = new LinearLayout.LayoutParams(getActivity().getResources().
                getDisplayMetrics().widthPixels*1/3, (getActivity().getResources().getDisplayMetrics().widthPixels)*1/3);
        layoutParams_video_item.setMargins(20, 30,20,30);

        featuredVideoAdapter = new FeaturedVideoAdapter(layoutParams_video_item, list_video_trending, new OnVideoFeatureClickListener() {
            @Override
            public void onClick(int position) {

            }
        });

        binding.rcvTrendVidHomeFrag.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        binding.rcvTrendVidHomeFrag.setAdapter(featuredVideoAdapter);

        list_TV_trending = new ArrayList<>();

        ConstraintLayout.LayoutParams layoutParams_TV_item = new ConstraintLayout.LayoutParams(getActivity().getResources().
                getDisplayMetrics().widthPixels*1/3, (getActivity().getResources().getDisplayMetrics().widthPixels)*1/3*3/4);
        layoutParams_TV_item.setMargins(20, 10,20,10);

        tvFragmentAdapter = new TVFragmentAdapter(list_video_trending, layoutParams_TV_item, new OnHomeItemClickListeners() {
            @Override
            public void onClick_homeItem(int position) {

            }
        });

        binding.rcvTrendTVHomeFrag.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        binding.rcvTrendTVHomeFrag.setAdapter(tvFragmentAdapter);

    }
}