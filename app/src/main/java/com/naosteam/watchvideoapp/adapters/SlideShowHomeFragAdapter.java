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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SlideShowHomeFragAdapter extends RecyclerView.Adapter<SlideShowHomeFragAdapter.SlideShowHomeHolder> {
    private ArrayList<Category_M> category_mArrayList;
    private OnHomeItemClickListeners listeners;

    public SlideShowHomeFragAdapter(ArrayList<Category_M> category_mArrayList, OnHomeItemClickListeners listeners) {
        this.category_mArrayList = category_mArrayList;
        this.listeners = listeners;
    }

    @NonNull
    @Override
    public SlideShowHomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_imgslide_home_frag_item, parent, false);
        return new SlideShowHomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideShowHomeHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return category_mArrayList.size();
    }

    class SlideShowHomeHolder extends RecyclerView.ViewHolder{
        ImageView imgSlide_home_frag_item;
        ConstraintLayout layout_item_Slide_item_frag_home;
        public SlideShowHomeHolder(@NonNull View itemView) {
            super(itemView);
            imgSlide_home_frag_item = (ImageView) itemView.findViewById(R.id.imgSlide_home_frag_item);
            layout_item_Slide_item_frag_home = (ConstraintLayout)
                    itemView.findViewById(R.id.layout_item_Slide_item_frag_home);
        }

        public void bindView(int position){
            Picasso.get().load(category_mArrayList.get(position).getCat_image()).into(imgSlide_home_frag_item);
            layout_item_Slide_item_frag_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listeners.onClick_SlideShow(position);
                }
            });
        }
    }
}
