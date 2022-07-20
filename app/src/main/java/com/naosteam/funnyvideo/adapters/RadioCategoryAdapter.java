package com.naosteam.funnyvideo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.naosteam.funnyvideo.R;
import com.naosteam.funnyvideo.listeners.OnRadioCatClickListeners;
import com.naosteam.funnyvideo.models.Category_M;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RadioCategoryAdapter extends RecyclerView.Adapter<RadioCategoryAdapter.RadioCategoryHolder>{
    private ArrayList<Category_M> list_cat;
    private OnRadioCatClickListeners listeners;
    private ViewGroup.LayoutParams layoutParams;

    public RadioCategoryAdapter(ViewGroup.LayoutParams layoutParams, ArrayList<Category_M> list_cat, OnRadioCatClickListeners listeners) {
        this.list_cat = list_cat;
        this.listeners = listeners;
        this.layoutParams = layoutParams;
    }

    public void LoadList_Cat(ArrayList<Category_M>  list_cats){
        this.list_cat = list_cats;
    }

    @NonNull
    @Override
    public RadioCategoryAdapter.RadioCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_radio_category, parent, false);
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
        RoundedImageView imv_radio_cat;
        TextView tv_radio_cat_name;
        ConstraintLayout layout_radio_cat_item;
        public RadioCategoryHolder(@NonNull View itemView) {
            super(itemView);
            imv_radio_cat = (RoundedImageView) itemView.findViewById(R.id.imv_radio_cat);
            tv_radio_cat_name = (TextView) itemView.findViewById(R.id.tv_radio_cat_name);
            layout_radio_cat_item = (ConstraintLayout) itemView.findViewById(R.id.layout_radio_cat_item);
        }

        public void bindView(int position){
            list_cat.get(position).getCat_image();
            Picasso.get().load(list_cat.get(position).getCat_image()).error(R.drawable.image_offline).into(imv_radio_cat);
            tv_radio_cat_name.setText(list_cat.get(position).getCat_name());
            layout_radio_cat_item.setLayoutParams(layoutParams);
            layout_radio_cat_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listeners.onClick(list_cat.get(position));
                }
            });
        }
    }
}
