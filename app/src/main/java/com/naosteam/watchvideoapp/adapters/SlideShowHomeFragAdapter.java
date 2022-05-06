package com.naosteam.watchvideoapp.adapters;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.listeners.SlideClickListeners;
import com.naosteam.watchvideoapp.models.Category_M;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class SlideShowHomeFragAdapter extends RecyclerView.Adapter<SlideShowHomeFragAdapter.SlideShowHomeHolder> {
    private ArrayList<Category_M> category_mArrayList;
    private SlideClickListeners listeners;

    public SlideShowHomeFragAdapter(ArrayList<Category_M> category_mArrayList, SlideClickListeners listeners) {
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
        public SlideShowHomeHolder(@NonNull View itemView) {
            super(itemView);
            imgSlide_home_frag_item = (ImageView) itemView.findViewById(R.id.imgSlide_home_frag_item);
        }

        public void bindView(int position){
            Picasso.get().load(category_mArrayList.get(position).getCat_image()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    imgSlide_home_frag_item.setBackground(new BitmapDrawable(bitmap));
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }
    }
}
