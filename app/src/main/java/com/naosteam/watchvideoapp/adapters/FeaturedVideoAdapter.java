package com.naosteam.watchvideoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.listeners.OnVideoFeatureClickListener;
import com.naosteam.watchvideoapp.models.Videos_M;
import com.naosteam.watchvideoapp.utils.Constant;
import com.naosteam.watchvideoapp.utils.Methods;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FeaturedVideoAdapter extends RecyclerView.Adapter<FeaturedVideoAdapter.MyViewHolder>  {

    private ArrayList<Videos_M> arrayList_video;
    private OnVideoFeatureClickListener listener;
    private ViewGroup.LayoutParams layoutParams;
    private Methods methods;
    private Context context;

    public FeaturedVideoAdapter(Methods methods, ViewGroup.LayoutParams layoutParams, ArrayList<Videos_M> arrayList_video, OnVideoFeatureClickListener listener) {
        this.arrayList_video = arrayList_video;
        this.listener = listener;
        this.methods = methods;
        this.layoutParams = layoutParams;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType){
            case Constant.DAILYMOTION_VIDEO:
                view = inflater.inflate(R.layout.item_dailymotion_video, parent, false);
                break;
            case Constant.YOUTUBE_VIDEO:
                view = inflater.inflate(R.layout.item_youtube_video, parent, false);
                break;
            default:
                view = inflater.inflate(R.layout.item_video_fragment, parent, false);
                break;
        }

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Videos_M video = arrayList_video.get(position);
        holder.tv_name.setText(video.getVid_title());

        Picasso.get()
                .load(video.getVid_thumbnail())
                .into(holder.iv_thumbnail);


        if(video.getVid_type() == Constant.DAILYMOTION_VIDEO){
            holder.tv_duration.setText(methods.getDurationString(video.getDuration()));
        }else{
            holder.tv_time.setText(methods.getPastTimeString(video.getVid_time()));
        }


        int views = video.getVid_view();

        if(views > 1000){
            if(views > 1000000){
                if(views > 1000000000){
                    double view1 = (double)Math.round(views/1000000000 * 10d) / 10d;
                    holder.tv_view.setText(view1 + "B views");
                }else{
                    double view1 = (double)Math.round(views/1000000 * 10d) / 10d;
                    holder.tv_view.setText(view1 + "M views");
                }
            }else{
                double view1 = (double)Math.round(views/1000 * 10d) / 10d;
                holder.tv_view.setText(view1 + "K views");
            }
        }else{
            holder.tv_view.setText(views + " views");
        }

        holder.ll_item.setOnClickListener(v->{
            listener.onClick(holder.getAdapterPosition());
        });

        holder.ll_item.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemViewType(int position) {
        return arrayList_video.get(position).getVid_type();
    }

    @Override
    public int getItemCount() {
        return arrayList_video.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        LinearLayout ll_item;
        ImageView iv_thumbnail;
        TextView tv_name;
        TextView tv_time;
        TextView tv_view;
        TextView tv_duration;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            ll_item = itemView.findViewById(R.id.ll_item);
            iv_thumbnail = itemView.findViewById(R.id.iv_thumb);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_view = itemView.findViewById(R.id.tv_view);
            tv_duration = itemView.findViewById(R.id.tv_duration);
        }
    }
}
