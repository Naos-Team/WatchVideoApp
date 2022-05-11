package com.naosteam.watchvideoapp.fragments;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.adapters.RadioCategoryAdapter;
import com.naosteam.watchvideoapp.adapters.RadioItemAdapter;
import com.naosteam.watchvideoapp.asynctasks.LoadRadioAsync;
import com.naosteam.watchvideoapp.asynctasks.LoadRadioCatItemAsync;
import com.naosteam.watchvideoapp.databinding.FragmentRadioBinding;
import com.naosteam.watchvideoapp.databinding.FragmentRadioCatItemBinding;
import com.naosteam.watchvideoapp.listeners.LoadRadioAsyncListener;
import com.naosteam.watchvideoapp.listeners.LoadRadioCatItemAsyncListener;
import com.naosteam.watchvideoapp.listeners.OnRadioClickListeners;
import com.naosteam.watchvideoapp.models.Category_M;
import com.naosteam.watchvideoapp.models.Videos_M;
import com.naosteam.watchvideoapp.utils.Constant;
import com.naosteam.watchvideoapp.utils.Methods;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class RadioCatItemFragment extends Fragment {

    private View rootView;
    private NavController navController;
    private TextView tv_cat_name;
    private RoundedImageView imv_cat;
    private FragmentRadioCatItemBinding binding;
    private ArrayList<Videos_M> mRadios;
    private Category_M cat;
    private RadioItemAdapter radioItemAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRadioCatItemBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);
        radioItemAdapter = null;
        mRadios = new ArrayList<>();

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
            cat = (Category_M) getArguments().getSerializable("category");

            Picasso.get().load(cat.getCat_image()).into(binding.imvCatRadio);
            binding.tvCatName.setText(cat.getCat_name());
            Bundle bundle = new Bundle();
            bundle.putInt("cat_id",cat.getCat_id());

            Picasso.get().load(cat.getCat_image()).into(binding.imvBackground);

        RequestBody requestBody = Methods.getInstance().GetRadioRequestBody("LOAD_RADIOS_OF_CATEGORY", bundle);
        LoadRadioCatItemAsyncListener listener = new LoadRadioCatItemAsyncListener() {
            @Override
            public void onStart() {
                binding.progressCircular.setVisibility(View.VISIBLE);

            }

            @Override
            public void onEnd(boolean status, ArrayList<Videos_M> arrayList_radios) {
                if(getContext() != null){
                    if(Methods.getInstance().isNetworkConnected(getContext())){
                        if(status){
                            mRadios.addAll(arrayList_radios);

                            binding.progressCircular.setVisibility(View.GONE);

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

        LoadRadioCatItemAsync async = new LoadRadioCatItemAsync(requestBody, listener, Methods.getInstance());
        async.execute();
    }

    private void updateUI() {
        binding.rclCatRadioItem.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        ConstraintLayout.LayoutParams layoutParams= new ConstraintLayout.LayoutParams((int)Math.round(width), (int)Math.round(width*0.17));
        layoutParams.setMargins(0,20,0,20);

        binding.imvBackToRadio.setOnClickListener(v->{
            navController.navigate(R.id.radio_cat_item_to_radio_screen);
        });

        radioItemAdapter = new RadioItemAdapter(layoutParams, mRadios, new OnRadioClickListeners() {
            @Override
            public void onClick(int position) {

                Constant.Radio_Listening = mRadios.get(position);
                binding.tvRadioListeningName.setText(Constant.Radio_Listening.getVid_title());
                Picasso.get().load(Constant.Radio_Listening.getVid_thumbnail()).into(binding.imvRadioListening);
            }
        });


        binding.rclCatRadioItem.setAdapter(radioItemAdapter);

        binding.itemRadioListening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constant.Radio_Listening.getCat_id()!=-1)
                {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("radio", Constant.Radio_Listening );
                    bundle.putString("from", "from_cat_item");
                    bundle.putSerializable("category", cat);
                    navController.navigate(R.id.radio_cat_item_to_radio_detail, bundle);
                }
                else{
                    Toast.makeText(getActivity(), "No Radio Playing", Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.searchViewCatitem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                onSearch(newText);
                if(newText.length() == 0){
                    radioItemAdapter.setList_Radio(mRadios);
                    radioItemAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
    }

    private void onSearch(String text){
        ArrayList<Videos_M> list_search = new ArrayList<>();
        for(Videos_M i : mRadios){
            if(i.getVid_title().toLowerCase().contains(text.toLowerCase()))
                list_search.add(i);
        }
        if(list_search.isEmpty()) {
            if (text.length() > 0)
                Toast.makeText(getActivity(), "No Radio Found", Toast.LENGTH_SHORT).show();
        } else {
            radioItemAdapter.setList_Radio(list_search);
            radioItemAdapter.notifyDataSetChanged();
        }
    }

}