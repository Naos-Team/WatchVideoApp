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
import com.naosteam.watchvideoapp.models.Videos_M;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TVFragmentAdapter extends RecyclerView.Adapter<TVFragmentAdapter.TVFragHolder> {
    private ArrayList<Videos_M> list_TV;
    private OnHomeItemClickListeners listeners;
    private ViewGroup.LayoutParams layoutParams;

    public TVFragmentAdapter(ArrayList<Videos_M> list_TV, ViewGroup.LayoutParams layoutParams, OnHomeItemClickListeners listeners) {
        this.list_TV = list_TV;
        this.listeners = listeners;
        this.layoutParams = layoutParams;
    }

    public ArrayList<Videos_M> getList_TV() {
        return list_TV;
    }

    public void setList_TV(ArrayList<Videos_M> list_TV) {
        this.list_TV = list_TV;
    }

    @NonNull
    @Override
    public TVFragmentAdapter.TVFragHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_tv_fragment, parent, false);
        return new TVFragmentAdapter.TVFragHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TVFragmentAdapter.TVFragHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return list_TV.size();
    }

    class TVFragHolder extends RecyclerView.ViewHolder{
        ImageView img_TV_item_frag;
        ConstraintLayout layout_TV_item_frag;
        public TVFragHolder(@NonNull View itemView) {
            super(itemView);
            img_TV_item_frag = (ImageView) itemView.findViewById(R.id.img_TV_item_frag);
            layout_TV_item_frag = (ConstraintLayout) itemView.findViewById(R.id.layout_TV_item_frag);
        }

        public void bindView(int position){
            Picasso.get().load(list_TV.get(position).getVid_thumbnail()).into(img_TV_item_frag);
            layout_TV_item_frag.setLayoutParams(layoutParams);
            layout_TV_item_frag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listeners.onClick_homeItem(position);
                }
            });
        }
    }
}
