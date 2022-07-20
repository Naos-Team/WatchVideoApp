package com.naosteam.funnyvideo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.naosteam.funnyvideo.R;
import com.naosteam.funnyvideo.activities.MainActivity;
import com.naosteam.funnyvideo.listeners.OnHomeItemClickListeners;
import com.naosteam.funnyvideo.models.Category_M;

import java.util.ArrayList;

public class CateTvFragmentAdapter extends RecyclerView.Adapter<CateTvFragmentAdapter.CateTvHolder> {
    private ArrayList<Category_M> list_category;
    private OnHomeItemClickListeners listeners;
    private MainActivity activity;
    private int selected_index = 0;

    public int getSelected_index() {
        return selected_index;
    }

    public void setSelected_index(int selected_index) {
        this.selected_index = selected_index;
    }

    public CateTvFragmentAdapter(ArrayList<Category_M> list_category, MainActivity activity, OnHomeItemClickListeners listeners) {
        this.list_category = list_category;
        this.listeners = listeners;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CateTvFragmentAdapter.CateTvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.category_tv_fragment_item, parent, false);
        return new CateTvFragmentAdapter.CateTvHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CateTvFragmentAdapter.CateTvHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return list_category.size();
    }


    public class CateTvHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout_cat_tv_frag_item;
        TextView txt_name_cat_tv_frag_item;
        public CateTvHolder(@NonNull View itemView) {
            super(itemView);
            layout_cat_tv_frag_item = (ConstraintLayout)
                    itemView.findViewById(R.id.layout_cat_tv_frag_item);
            txt_name_cat_tv_frag_item = (TextView)
                    itemView.findViewById(R.id.txt_name_cat_tv_frag_item);
        }

        public void bindView(int position){
            if(position == selected_index){
                txt_name_cat_tv_frag_item.setTextColor(activity.getResources().getColor(R.color.darkGreen));
            } else {
                txt_name_cat_tv_frag_item.setTextColor(activity.getResources().getColor(R.color.neonGreen));
            }
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(40, 2, 40, 2);
            layout_cat_tv_frag_item.setLayoutParams(layoutParams);
            layout_cat_tv_frag_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listeners.onClick_homeItem(position);
                }
            });
            txt_name_cat_tv_frag_item.setText(list_category.get(position).getCat_name());
        }
    }
}
