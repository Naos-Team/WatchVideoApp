package com.naosteam.watchvideoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.listeners.OnCategoryHorizontalListener;
import com.naosteam.watchvideoapp.models.Category_M;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HorizontalCategoryAdapter extends RecyclerView.Adapter<HorizontalCategoryAdapter.MyViewHolder> {

    private ArrayList<Category_M> arrayList_category;
    private OnCategoryHorizontalListener listener;
    private RelativeLayout.LayoutParams layoutParams;
    private Context context;

    public HorizontalCategoryAdapter(RelativeLayout.LayoutParams layoutParams, ArrayList<Category_M> arrayList_category, OnCategoryHorizontalListener listener) {
        this.arrayList_category = arrayList_category;
        this.listener = listener;
        this.layoutParams = layoutParams;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_category_horizontal, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Category_M category = arrayList_category.get(position);

        holder.tv_name.setText(category.getCat_name());
        holder.rl_item.setOnClickListener(v->{
            listener.onClick(holder.getAdapterPosition());
        });

        holder.rl_item.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return arrayList_category.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout rl_item;
        TextView tv_name;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            rl_item = itemView.findViewById(R.id.rl_item);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }
}
