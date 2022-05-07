package com.naosteam.watchvideoapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.listeners.OnHomeItemClickListeners;
import com.naosteam.watchvideoapp.models.Category_M;
import com.naosteam.watchvideoapp.models.Videos_M;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RadioCategoryAdapter extends RecyclerView.Adapter<RadioCategoryAdapter.RadioCategoryHolder>{
    private ArrayList<Category_M> list_cat;
    private OnHomeItemClickListeners listeners;
    private ViewGroup.LayoutParams layoutParams;

    public RadioCategoryAdapter(ArrayList<Category_M> list_cat, ViewGroup.LayoutParams layoutParams, OnHomeItemClickListeners listeners) {
        this.list_cat = list_cat;
        this.listeners = listeners;
        this.layoutParams = layoutParams;
    }

    @NonNull
    @Override
    public RadioCategoryAdapter.RadioCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_tv_fragment, parent, false);
        return new RadioCategoryAdapter.RadioCategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RadioCategoryAdapter.RadioCategoryHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return list_cat.size();
    }

    class RadioCategoryHolder extends RecyclerView.ViewHolder{
        ImageView img_TV_item_frag;
        ConstraintLayout layout_TV_item_frag;
        public RadioCategoryHolder(@NonNull View itemView) {
            super(itemView);
            img_TV_item_frag = (ImageView) itemView.findViewById(R.id.img_TV_item_frag);
            layout_TV_item_frag = (ConstraintLayout) itemView.findViewById(R.id.layout_TV_item_frag);
        }

        public void bindView(int position){
//            Picasso.get().load(list_cat.get(position).getVid_thumbnail()).into(img_TV_item_frag);
//            layout_TV_item_frag.setLayoutParams(layoutParams);
//            layout_TV_item_frag.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    listeners.onClick_SlideShow(position);
//                }
//            });
        }
    }
}
