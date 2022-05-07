package com.naosteam.watchvideoapp.fragments;

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

import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.activities.MainActivity;
import com.naosteam.watchvideoapp.adapters.CateTvFragmentAdapter;
import com.naosteam.watchvideoapp.adapters.TVFragmentAdapter;
import com.naosteam.watchvideoapp.databinding.FragmentTvBinding;
import com.naosteam.watchvideoapp.listeners.OnHomeItemClickListeners;
import com.naosteam.watchvideoapp.models.Category_M;
import com.naosteam.watchvideoapp.models.Videos_M;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TvFragment extends Fragment {
    private FragmentTvBinding binding;
    private NavController navController;
    private ArrayList<Category_M> list_category;
    private ArrayList<Videos_M> list_video;
    private CateTvFragmentAdapter catetvFragmentAdapter;
    private TVFragmentAdapter tvFragmentAdapter;
    private ArrayList<Videos_M> list_cate_video;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTvBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);
        setUp();

        return rootView;
    }

    private void setUp(){
        navController = NavHostFragment.findNavController(this);

        list_category = new ArrayList<>();
        list_category.add(
                new Category_M(-1,"All", "https://d1j8r0kxyu9tj8.cloudfront.net/images/1566809284X4pyEDCj7CFMsGu.jpg", 1)
        );
        list_category.add(
                new Category_M(0,"Fun", "https://intphcm.com/data/upload/mau-poster-hinh-anh-lon-phim-mat-biec.jpg", 1)
        );
        list_category.add(
                new Category_M(1,"Thriller", "https://upload.wikimedia.org/wikipedia/vi/3/32/Poster_phim_T%C3%AAn_b%E1%BA%A1n_l%C3%A0_g%C3%AC.jpg", 1)
        );
        list_category.add(
                new Category_M(2,"Sport", "https://d1j8r0kxyu9tj8.cloudfront.net/images/1566809340Y397jnilYDd15KN.jpg", 1)
        );

        catetvFragmentAdapter = new CateTvFragmentAdapter(list_category, (MainActivity) getActivity(),
                new OnHomeItemClickListeners() {
            @Override
            public void onClick_homeItem(int position) {
                catetvFragmentAdapter.setSelected_index(position);
                catetvFragmentAdapter.notifyDataSetChanged();
                list_cate_video.clear();
                if(list_category.get(position).getCat_id() == -1){
                    list_cate_video.addAll(list_video);
                } else {
                    for(Videos_M videos_m : list_video){
                        if(videos_m.getCat_id() == list_category.get(position).getCat_id()){
                            list_cate_video.add(videos_m);
                        }
                    }
                }
                tvFragmentAdapter.setList_TV(list_cate_video);
                tvFragmentAdapter.notifyDataSetChanged();
            }
        });
        binding.rclCategoryTvFrag.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        binding.rclCategoryTvFrag.setAdapter(catetvFragmentAdapter);

        list_video = new ArrayList<>();
        String strTime = "20:15:40";
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");

        Date d = new Date();
        try {
            d = dateFormat.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        list_video.add(
                new Videos_M(1, 1, "Tenchnology 1",
                        "https://ieltsplanet.info/wp-content/uploads/2017/04/healthcare-technology-8-04-2015.jpg","des",
                        "https://www.youtube.com/watch?v=2OKYsYErfFo", 44, 4.5f, 1,
                        true, d)
        );

        list_video.add(
                new Videos_M(1, 0, "Tenchnology 12",
                        "https://ieltsplanet.info/wp-content/uploads/2017/04/healthcare-technology-8-04-2015.jpg","des",
                        "https://www.youtube.com/watch?v=2OKYsYErfFo", 44, 4.5f, 1,
                        true, d)
        );

        list_video.add(
                new Videos_M(1, 2, "Tenchnology 123",
                        "https://ieltsplanet.info/wp-content/uploads/2017/04/healthcare-technology-8-04-2015.jpg","des",
                        "https://www.youtube.com/watch?v=2OKYsYErfFo", 44, 4.5f, 1,
                        true, d)
        );


        list_video.add(
                new Videos_M(1, 2, "Tenchnology 1234",
                        "https://ieltsplanet.info/wp-content/uploads/2017/04/healthcare-technology-8-04-2015.jpg","des",
                        "https://www.youtube.com/watch?v=2OKYsYErfFo", 44, 4.5f, 1,
                        true, d)
        );
        list_cate_video = new ArrayList<>(list_video);

        ConstraintLayout.LayoutParams layoutParams_TV_item = new ConstraintLayout.LayoutParams(getActivity().getResources().
                getDisplayMetrics().widthPixels*1/3 - 40, (getActivity().getResources().getDisplayMetrics().widthPixels)*1/3*3/4 - 40);
        layoutParams_TV_item.setMargins(20, 10,20,10);
        tvFragmentAdapter = new TVFragmentAdapter(list_cate_video, layoutParams_TV_item, new OnHomeItemClickListeners() {
            @Override
            public void onClick_homeItem(int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("amount", 15);
                navController.navigate(R.id.tv_navigation);
            }
        });
        binding.rclItemTvFrag.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.rclItemTvFrag.setAdapter(tvFragmentAdapter);

        binding.sVTvFragment.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                onSearch(newText);
                if(newText.length() == 0){
                    tvFragmentAdapter.setList_TV(list_cate_video);
                    tvFragmentAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
    }

    private void onSearch(String text){
        ArrayList<Videos_M> list_search = new ArrayList<>();
        for(Videos_M i : list_cate_video){
            if(i.getVid_title().toLowerCase().contains(text.toLowerCase()))
                list_search.add(i);
        }
        if(list_search.isEmpty()) {
            if (text.length() > 0)
                Toast.makeText(getActivity(), "No TV Founds", Toast.LENGTH_SHORT).show();
        } else {
            tvFragmentAdapter.setList_TV(list_search);
            tvFragmentAdapter.notifyDataSetChanged();
        }
    }
}