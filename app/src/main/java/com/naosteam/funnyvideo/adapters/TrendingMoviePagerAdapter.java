package com.naosteam.funnyvideo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.naosteam.funnyvideo.R;
import com.naosteam.funnyvideo.listeners.OnVideoFeatureClickListener;
import com.naosteam.funnyvideo.models.Videos_M;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrendingMoviePagerAdapter extends PagerAdapter {

    Context mContext;
    ArrayList<Videos_M> mList;
    OnVideoFeatureClickListener listener;

    public TrendingMoviePagerAdapter(Context mContext, ArrayList<Videos_M> mList, OnVideoFeatureClickListener listener) {
        this.mContext = mContext;
        this.mList = mList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = layoutInflater.inflate(R.layout.item_pager_trending, null);

        ImageView iv_thumb = layoutScreen.findViewById(R.id.iv_thumb);
        TextView tv_name = layoutScreen.findViewById(R.id.tv_name);
        ConstraintLayout cs_item = layoutScreen.findViewById(R.id.cs_item);

        tv_name.setText(mList.get(position).getVid_title());

        Picasso.get()
                .load(mList.get(position).getVid_thumbnail())
                .error(R.drawable.image_offline)
                .into(iv_thumb);


        cs_item.setOnClickListener(v->{
            listener.onClick(position);
        });

        container.addView(layoutScreen);

        return layoutScreen;
    }



    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
