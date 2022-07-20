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
import com.naosteam.funnyvideo.listeners.OnRadioClickListeners;
import com.naosteam.funnyvideo.models.Videos_M;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RadioItemAdapter extends RecyclerView.Adapter<RadioItemAdapter.RadioItemHolder>{
    private ArrayList<Videos_M> list_radio;
    private OnRadioClickListeners listeners;
    private ViewGroup.LayoutParams layoutParams;

    public RadioItemAdapter(ViewGroup.LayoutParams layoutParams, ArrayList<Videos_M> list_radio, OnRadioClickListeners listeners) {
        this.list_radio = list_radio;
        this.listeners = listeners;
        this.layoutParams = layoutParams;
    }

    public void setList_Radio(ArrayList<Videos_M> list_radio) {
        this.list_radio = list_radio;
    }

    @NonNull
    @Override
    public RadioItemAdapter.RadioItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_radio_items, parent, false);
        return new RadioItemAdapter.RadioItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RadioItemAdapter.RadioItemHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return list_radio.size();
    }

    class RadioItemHolder extends RecyclerView.ViewHolder{
        RoundedImageView imv_radio_item;
        TextView tv_radio_item_name, tv_radio_item_view;
        ConstraintLayout layout_radio_item;
        public RadioItemHolder(@NonNull View itemView) {
            super(itemView);
            imv_radio_item = (RoundedImageView) itemView.findViewById(R.id.imv_radio_item);
            tv_radio_item_name = (TextView) itemView.findViewById(R.id.tv_radio_item_name);
            tv_radio_item_view = (TextView) itemView.findViewById(R.id.tv_view_radio_item);
            layout_radio_item = (ConstraintLayout) itemView.findViewById(R.id.layout_radio_item);
        }

        public void bindView(int position){
            Picasso.get().load(list_radio.get(position).getVid_thumbnail()).error(R.drawable.image_offline).into(imv_radio_item);
            tv_radio_item_name.setText(list_radio.get(position).getVid_title());
            tv_radio_item_view.setText(list_radio.get(position).getVid_view()+" views");
            layout_radio_item.setLayoutParams(layoutParams);
            layout_radio_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listeners.onClick(position);
                }
            });
        }
    }
}
