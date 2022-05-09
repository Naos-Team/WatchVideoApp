package com.naosteam.watchvideoapp.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.adapters.RadioCategoryAdapter;
import com.naosteam.watchvideoapp.adapters.RadioItemAdapter;
import com.naosteam.watchvideoapp.adapters.TVFragmentAdapter;
import com.naosteam.watchvideoapp.asynctasks.LoadRadioAsync;
import com.naosteam.watchvideoapp.databinding.FragmentRadioBinding;
import com.naosteam.watchvideoapp.listeners.LoadRadioAsyncListener;
import com.naosteam.watchvideoapp.listeners.OnRadioCatClickListeners;
import com.naosteam.watchvideoapp.listeners.OnRadioClickListeners;
import com.naosteam.watchvideoapp.models.Category_M;
import com.naosteam.watchvideoapp.models.Videos_M;
import com.naosteam.watchvideoapp.utils.Constant;
import com.naosteam.watchvideoapp.utils.Methods;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class RadioFragment extends Fragment {

    private View rootView;
    private NavController navController;
    private TextView tv_video;
    private FragmentRadioBinding binding;
    private ArrayList<Videos_M> mTrendings;
    private ArrayList<Category_M> mCats;
    private RadioCategoryAdapter categoryAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRadioBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);
        categoryAdapter = null;

        mTrendings = new ArrayList<>();
        mCats = new ArrayList<>();

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
        RequestBody requestBody = Methods.getInstance().GetRadioRequestBody("LOAD_RADIO_SCREEN", null);
        LoadRadioAsyncListener listener = new LoadRadioAsyncListener() {
            @Override
            public void onStart() {

                binding.progressCircular1.setVisibility(View.VISIBLE);
                binding.progressCircular2.setVisibility(View.VISIBLE);

            }

            @Override
            public void onEnd(boolean status, ArrayList<Videos_M> arrayList_trending, ArrayList<Category_M> arrayList_category) {
                if(getContext() != null){
                    if(Methods.getInstance().isNetworkConnected(getContext())){
                        if(status){
                            mTrendings.addAll(arrayList_trending);
                            mCats.addAll(arrayList_category);

                            binding.progressCircular1.setVisibility(View.GONE);
                            binding.progressCircular2.setVisibility(View.GONE);

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

        LoadRadioAsync async = new LoadRadioAsync(requestBody, listener, Methods.getInstance());
        async.execute();
    }

    private void updateUI() {
        binding.rclCatRadioFrag.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false));
        binding.rclRadioTrending.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams((int)Math.round(width*0.4), (int)Math.round(width*0.3));
        layoutParams.setMargins(20,20,20,20);

        ConstraintLayout.LayoutParams layoutParams1 = new ConstraintLayout.LayoutParams((int)Math.round(width), (int)Math.round(width*0.17));
        layoutParams1.setMargins(0,20,0,20);

        categoryAdapter = new RadioCategoryAdapter(layoutParams, mCats, new OnRadioCatClickListeners() {
            @Override
            public void onClick(Category_M category) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("category", category);
                navController.navigate(R.id.radio_screen_to_cat_item, bundle);
            }
        });

        binding.rclCatRadioFrag.setAdapter(categoryAdapter);

        binding.rclRadioTrending.setAdapter(new RadioItemAdapter(layoutParams1, mTrendings, new OnRadioClickListeners() {
            @Override
            public void onClick(Videos_M radio) {

                Constant.Radio_Listening = radio;
                binding.tvRadioListeningName.setText(Constant.Radio_Listening.getVid_title());
                Picasso.get().load(Constant.Radio_Listening.getVid_thumbnail()).into(binding.imvRadioListening);
            }
        }));

        binding.itemRadioListening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constant.Radio_Listening.getCat_id()!=-1)
                {
                    Bundle bundle = new Bundle();
                    //bundle.putSerializable("radio", Constant.Radio_Listening);
                    bundle.putString("from","from_radio_screen");
                    navController.navigate(R.id.radio_screen_to_radio_detail, bundle);
                }
                else{
                    Toast.makeText(getActivity(), "No Radio Playing", Toast.LENGTH_SHORT).show();
                }

            }
        });


        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                onSearch(newText);
                if(newText.length() == 0){
                    categoryAdapter.LoadList_Cat(mCats);
                    categoryAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });

    }

    private void onSearch(String text){
        ArrayList<Category_M> list_search = new ArrayList<>();
        for(Category_M i : mCats){
            if(i.getCat_name().toLowerCase().contains(text.toLowerCase()))
                list_search.add(i);
        }
        if(list_search.isEmpty()) {
            if (text.length() > 0)
                Toast.makeText(getActivity(), "No Radio Category Found", Toast.LENGTH_SHORT).show();
        } else {
            categoryAdapter.LoadList_Cat(list_search);
            categoryAdapter.notifyDataSetChanged();
        }
    }

}