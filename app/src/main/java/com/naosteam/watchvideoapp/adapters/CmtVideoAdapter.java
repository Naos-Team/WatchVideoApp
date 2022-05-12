package com.naosteam.watchvideoapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.activities.MainActivity;
import com.naosteam.watchvideoapp.listeners.OnCmtItemListener;
import com.naosteam.watchvideoapp.models.Comment_M;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CmtVideoAdapter extends RecyclerView.Adapter<CmtVideoAdapter.CmtVideoHolder> {
    private ArrayList<Comment_M> list_cmt;
    private OnCmtItemListener listener;
    private MainActivity activity;
    private boolean for_del = true;

    public CmtVideoAdapter(ArrayList<Comment_M> list_cmt, MainActivity activity, OnCmtItemListener listener) {
        this.list_cmt = list_cmt;
        this.listener = listener;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CmtVideoAdapter.CmtVideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_cmt_video, parent, false);
        return new CmtVideoAdapter.CmtVideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CmtVideoAdapter.CmtVideoHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return list_cmt.size();
    }

    class CmtVideoHolder extends RecyclerView.ViewHolder{
        ImageView img_usder_cmt_item, btn_del_cmt_item, btn_edit_cmt_item;
        EditText txtCmt_item;
        ConstraintLayout layout_option_cmt_item;

        public CmtVideoHolder(@NonNull View itemView) {
            super(itemView);
            img_usder_cmt_item = (ImageView) itemView.findViewById(R.id.img_usder_cmt_item);
            btn_del_cmt_item = (ImageView) itemView.findViewById(R.id.btn_del_cmt_item);
            btn_edit_cmt_item = (ImageView) itemView.findViewById(R.id.btn_edit_cmt_item);
            txtCmt_item = (EditText) itemView.findViewById(R.id.txtCmt_item);
            layout_option_cmt_item = (ConstraintLayout) itemView.findViewById(R.id.layout_option_cmt_item);
        }

        public void bindView(int position){

            txtCmt_item.setEnabled(false);
//            if(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null){
//                Uri uri = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
//                Picasso.get().load(uri).into(img_usder_cmt_item);
//            }
            txtCmt_item.setText(list_cmt.get(position).getCmt_text());
            btn_del_cmt_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(for_del){
                        listener.onDel(position);
                    } else {
                        for_del = true;
                        listener.onPreEdit(View.VISIBLE);
                        txtCmt_item.setEnabled(false);
                        btn_del_cmt_item.setImageResource(R.drawable.ic_del);
                        btn_edit_cmt_item.setImageResource(R.drawable.ic_edit);
                    }
                }
            });

//            if(list_cmt.get(position).getUid().equals(FirebaseAuth.getInstance().getCurrentUser())){
            if(list_cmt.get(position).getUid().equals("213123")){
                layout_option_cmt_item.setVisibility(View.VISIBLE);
            } else {
                layout_option_cmt_item.setVisibility(View.GONE);
            }

            btn_edit_cmt_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(txtCmt_item.isEnabled()) {
                        for_del = true;
                        String cmt = txtCmt_item.getText().toString();
                        listener.onEdit(position, cmt);
                        txtCmt_item.setEnabled(false);
                        btn_del_cmt_item.setImageResource(R.drawable.ic_del);
                        btn_edit_cmt_item.setImageResource(R.drawable.ic_edit);
                    } else {
                        for_del = false;
                        txtCmt_item.setEnabled(true);
                        btn_del_cmt_item.setImageResource(R.drawable.ic_close);
                        btn_edit_cmt_item.setImageResource(R.drawable.ic_save);
                        listener.onPreEdit(View.GONE);
                    }
                }
            });
        }
    }
}
