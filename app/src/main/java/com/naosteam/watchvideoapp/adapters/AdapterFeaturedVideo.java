package com.naosteam.watchvideoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.listeners.OnVideoFeatureClickListener;
import com.naosteam.watchvideoapp.models.Videos_M;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AdapterFeaturedVideo extends RecyclerView.Adapter<AdapterFeaturedVideo.MyViewHolder>  {

    private ArrayList<Videos_M> arrayList_video;
    private OnVideoFeatureClickListener listener;
    private Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_video_fragment, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Videos_M video = arrayList_video.get(position);
        holder.tv_name.setText(video.getVid_title());

        Picasso.get()
                .load(video.getVid_thumbnail())
                .into(holder.iv_thumbnail);

        Date currentDate = new Date();
        Date postDate = video.getVid_time();

        long diffInTime = currentDate.getTime() - postDate.getTime();
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInTime);
        long diffInHour = TimeUnit.MILLISECONDS.toHours(diffInTime);
        long diffInYear = TimeUnit.MILLISECONDS.toDays(diffInTime) / 365l;
        long diffInMonth = TimeUnit.MILLISECONDS.toDays(diffInTime) / 30l;
        long diffInDay = TimeUnit.MILLISECONDS.toDays(diffInTime);


        if (diffInYear < 1) {
            if (diffInMonth < 1) {
                if (diffInDay < 1) {
                    if (diffInHour < 1) {
                        if (diffInMinutes < 1) {
                            holder.tv_time.setText("Just now");
                        } else {
                            holder.tv_time.setText(diffInMinutes + " minutes ago");
                        }
                    } else {
                        holder.tv_time.setText(diffInHour + " hours ago");
                    }
                } else {
                    holder.tv_time.setText(diffInDay + " days ago");
                }
            } else {
                holder.tv_time.setText(diffInMonth + " months ago");
            }
        } else {
            holder.tv_time.setText(diffInYear + " years ago");
        }

        int views = video.getVid_view();

        if(views > 1000){
            if(views > 1000000){
                if(views > 1000000000){
                    String view1 = String.format("%.1g%n", views/1000000000);
                    holder.tv_view.setText(view1 + "B views");
                }else{
                    String view1 = String.format("%.1g%n", views/1000000);
                    holder.tv_view.setText(view1 + "M views");
                }
            }else{
                String view1 = String.format("%.1g%n", views/1000);
                holder.tv_view.setText(view1 + "K views");
            }
        }else{
            holder.tv_view.setText(views + " views");
        }

        holder.ll_item.setOnClickListener(v->{
            listener.onClick(holder.getAdapterPosition());
        });
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

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            ll_item = itemView.findViewById(R.id.ll_item);
            iv_thumbnail = itemView.findViewById(R.id.iv_thumb);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_view = itemView.findViewById(R.id.tv_view);
        }
    }
}
