package com.naosteam.watchvideoapp.fragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.activities.VideoPlayerActivity;
import com.naosteam.watchvideoapp.adapters.FeaturedVideoAdapter;
import com.naosteam.watchvideoapp.asynctasks.LoadSearchVideoAsync;
import com.naosteam.watchvideoapp.databinding.FragmentSearchVideoBinding;
import com.naosteam.watchvideoapp.listeners.LoadSearchVideoAsyncListener;
import com.naosteam.watchvideoapp.listeners.OnVideoFeatureClickListener;
import com.naosteam.watchvideoapp.models.Videos_M;
import com.naosteam.watchvideoapp.utils.Methods;

import java.util.ArrayList;

import okhttp3.RequestBody;


public class SearchVideoFragment extends Fragment {

    private View rootView;
    private NavController navController;
    private FragmentSearchVideoBinding binding;
    private ArrayList<Videos_M> mVideos;
    private String search_text = "";
    private String type = "";
    private int cat_id = 0;
    private int page = 0;
    private int step = 20;
    private boolean loading = true;
    private int width = 0;
    private int height = 0;
    private FeaturedVideoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchVideoBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);

        type = getArguments().getString("type");

        if(type.equals("search")){
            search_text = getArguments().getString("search_text");
            binding.tvResult.setText("Results match with \""+ search_text +"\"");
        }else if(type.equals("category")){
            cat_id = getArguments().getInt("cat_id");
            String cat_name = getArguments().getString("cat_name");
            binding.tvResult.setText("Results for \""+ cat_name +"\" category");
        }

        mVideos = new ArrayList<>();

        width = getContext().getResources().getDisplayMetrics().widthPixels;
        height = getContext().getResources().getDisplayMetrics().heightPixels;

        SetupView();

        LoadSearchVideo(false);

        return rootView;
    }

    private void LoadSearchVideo(boolean isLazy) {

        Bundle bundle = new Bundle();
        RequestBody requestBody = null;

        if(type.equals("search")){
            bundle.putString("search_text", search_text);
            bundle.putInt("page", page);
            bundle.putInt("step", step);

            requestBody = Methods.getInstance().getVideoRequestBody("LOAD_SEARCH_VIDEO", bundle);
        }else if(type.equals("category")) {
            bundle.putInt("cat_id", cat_id);
            bundle.putInt("page", page);
            bundle.putInt("step", step);

            requestBody = Methods.getInstance().getVideoRequestBody("LOAD_SEARCH_CATEGORY", bundle);
        }

        LoadSearchVideoAsync async = new LoadSearchVideoAsync(Methods.getInstance(), requestBody, new LoadSearchVideoAsyncListener() {
            @Override
            public void onStart() {
                if(isLazy){
                    binding.progressBarLazy.setVisibility(View.VISIBLE);
                }else{
                    binding.rv.setVisibility(View.GONE);
                    binding.progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onEnd(boolean status, ArrayList<Videos_M> arrayList) {
                if(getContext() != null){
                    if(Methods.getInstance().isNetworkConnected(getContext())){
                        if(status){
                            if(isLazy){
                                binding.progressBarLazy.setVisibility(View.GONE);

                                if(arrayList.isEmpty()){
                                    Toast.makeText(getContext(), "No more videos to get!", Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                binding.rv.setVisibility(View.VISIBLE);
                                binding.progressBar.setVisibility(View.GONE);

                                if(arrayList.isEmpty()){
                                    binding.tvNotFound.setVisibility(View.VISIBLE);
                                    binding.rv.setVisibility(View.GONE);
                                }
                            }

                            page++;
                            mVideos.addAll(arrayList);
                            adapter.notifyItemRangeInserted(mVideos.size() + 1, arrayList.size());
                            loading = true;

                        }else{
                            Toast.makeText(getContext(), "Something wrong happened, try again!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getContext(), "Please connect to the internet!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        async.execute();
    }

    private void SetupView() {
        int height = getContext().getResources().getDisplayMetrics().heightPixels;
        LinearLayout.LayoutParams layoutParams00 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) Math.round(height*0.15));
        binding.llTop.setLayoutParams(layoutParams00);

        binding.btnBackFloat.hide();

        binding.btnBack.setOnClickListener(v->{
            navController.navigate(R.id.searchVideoBackToVideo);
        });

        binding.btnBackFloat.setOnClickListener(v->{
            navController.navigate(R.id.searchVideoBackToVideo);
        });

        GridLayoutManager llm = new GridLayoutManager(getContext(), 2);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) Math.round(width/2*0.8));
        layoutParams.setMargins(10,20,10,20);

        adapter = new FeaturedVideoAdapter(Methods.getInstance(), layoutParams, mVideos, new OnVideoFeatureClickListener() {
            @Override
            public void onClick(int position) {
                Videos_M videos_m = mVideos.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("video", videos_m);
                Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 225);
            }
        });

        binding.rv.setItemAnimator(new DefaultItemAnimator());
        binding.rv.setLayoutManager(llm);
        binding.rv.setAdapter(adapter);

        binding.scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if(0 <= scrollY && scrollY < v.getMeasuredHeight()/3){
                    binding.btnBackFloat.hide();
                }else{
                    binding.btnBackFloat.show();
                }

                if(v.getChildAt(v.getChildCount() - 1) != null) {
                    if(loading){
                        if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                                scrollY > oldScrollY) {
                            loading = false;
                            LoadSearchVideo(true);
                        }
                    }

                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 225 && resultCode == -1){
            int orientation = this.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }

    }
}