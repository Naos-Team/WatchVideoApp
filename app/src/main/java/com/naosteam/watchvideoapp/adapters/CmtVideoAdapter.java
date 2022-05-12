package com.naosteam.watchvideoapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.activities.MainActivity;
import com.naosteam.watchvideoapp.listeners.OnCmtItemListener;
import com.naosteam.watchvideoapp.models.Comment_M;
import com.naosteam.watchvideoapp.utils.Methods;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        TextView txtTime_cmt_item, txtName_cmt_item;
        EditText txtCmt_item;
        ConstraintLayout layout_option_cmt_item;

        public CmtVideoHolder(@NonNull View itemView) {
            super(itemView);
            img_usder_cmt_item = (ImageView) itemView.findViewById(R.id.img_usder_cmt_item);
            btn_del_cmt_item = (ImageView) itemView.findViewById(R.id.btn_del_cmt_item);
            btn_edit_cmt_item = (ImageView) itemView.findViewById(R.id.btn_edit_cmt_item);
            txtName_cmt_item = (TextView) itemView.findViewById(R.id.txtName_cmt_item);
            txtTime_cmt_item = (TextView) itemView.findViewById(R.id.txtTime_cmt_item);
            txtCmt_item = (EditText) itemView.findViewById(R.id.txtCmt_item);
            layout_option_cmt_item = (ConstraintLayout) itemView.findViewById(R.id.layout_option_cmt_item);
        }

        public void bindView(int position){

            txtCmt_item.setEnabled(false);
            Date date1 = new Date();
            try {
                date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(list_cmt.get(position).getCmt_time());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            txtTime_cmt_item.setText(Methods.getInstance().getPastTimeString(date1));
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
                        btn_edit_cmt_item.setImageResource(R.drawable.ic_edit1);
                    }
                }
            });

            if(list_cmt.get(position).getUid().equals(FirebaseAuth.getInstance().getCurrentUser().toString())){
                FirebaseDatabase.getInstance().getReference().child("Users").
                        child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String user_name = (snapshot.child("user_name").getValue().toString().equals("empty"))
                                ? snapshot.child("user_name").getValue().toString() : "User";
                        txtName_cmt_item.setText(user_name);
                        Log.e("tuyen", snapshot.child("photo_url").getValue().toString());
                        if(!snapshot.child("photo_url").getValue().toString().equals("empty")) {
                            Picasso.get().load(snapshot.child("photo_url").getValue().toString()).into(img_usder_cmt_item);
                        } else {
                            img_usder_cmt_item.setImageResource(R.drawable.ic_nouser_setting);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                layout_option_cmt_item.setVisibility(View.VISIBLE);
            } else {
                FirebaseDatabase.getInstance().getReference().child("Users").
                        child(list_cmt.get(position).getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String user_name = (snapshot.child("user_name").getValue().toString().equals("empty"))
                                ? snapshot.child("user_name").getValue().toString() : "User";
                        txtName_cmt_item.setText(user_name);
                        Log.e("tuyen", snapshot.child("photo_url").getValue().toString());
                        if(!snapshot.child("photo_url").getValue().toString().equals("empty")) {
                            Picasso.get().load(snapshot.child("photo_url").getValue().toString()).into(img_usder_cmt_item);
                        } else {
                            img_usder_cmt_item.setImageResource(R.drawable.ic_nouser_setting);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                layout_option_cmt_item.setVisibility(View.GONE);
            }

            btn_edit_cmt_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(txtCmt_item.isEnabled()) {
                        for_del = true;
                        String cmt = txtCmt_item.getText().toString();
                        listener.onEdit(position, cmt);
                        listener.onPreEdit(View.VISIBLE);
                        btn_del_cmt_item.setImageResource(R.drawable.ic_del);
                        btn_edit_cmt_item.setImageResource(R.drawable.ic_edit1);
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
