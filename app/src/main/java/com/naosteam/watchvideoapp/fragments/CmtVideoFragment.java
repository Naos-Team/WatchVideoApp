package com.naosteam.watchvideoapp.fragments;

import android.opengl.Visibility;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.activities.MainActivity;
import com.naosteam.watchvideoapp.adapters.CmtVideoAdapter;
import com.naosteam.watchvideoapp.databinding.FragmentCmtVideoBinding;
import com.naosteam.watchvideoapp.listeners.OnCmtItemListener;
import com.naosteam.watchvideoapp.models.Comment_M;
import com.naosteam.watchvideoapp.models.Videos_M;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CmtVideoFragment extends Fragment {
    private View rootView;
    private FragmentCmtVideoBinding binding;
    private NavController navController;
    private ArrayList<Comment_M> array_cmt;
    private CmtVideoAdapter adapter;
    private Videos_M videos_m;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCmtVideoBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);
        videos_m = ((Videos_M) getArguments().getSerializable("video"));
        setUp();
        loadCmt();
        return rootView;
    }

    private void setUp(){
        array_cmt = new ArrayList<>();

        binding.txtTitleCmtVideoFrag.setText(videos_m.getVid_title());

        binding.btnSendCmtVideoFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               if(FirebaseAuth.getInstance().getCurrentUser() == null){
//                   Toast.makeText(getActivity(), "Please", Toast.LENGTH_SHORT).show();
//               } else {
                   DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                   Date date =new Date();
                   try {
                       date = dateFormat.parse(dateFormat.format(new Date().getTime()));
                   } catch (ParseException e) {
                       e.printStackTrace();
                   }
                   Comment_M cmt = new Comment_M(-1, videos_m.getVid_id(),
//                           FirebaseAuth.getInstance().getCurrentUser().toString(),
                           "213123",
                           date.toString(),
                           binding.txtCmtVideoFrag.getText().toString());
                   insert(cmt);
//               }
            }
        });

        binding.btnBackVideoFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("is_home", getArguments().getBoolean("is_home"));
                bundle.putSerializable("video", getArguments().getSerializable("video"));
                navController.navigate(R.id.cmt_to_detail_Frag, bundle);
            }
        });

        adapter = new CmtVideoAdapter(array_cmt, (MainActivity) getActivity(),
                new OnCmtItemListener() {
                    @Override
                    public void onPreEdit(int visibility){
                        binding.layoutSendCmtVideoFrag.setVisibility(visibility);
                    }

                    @Override
                    public void onEdit(int position, String cmt) {
                        array_cmt.get(position).setCmt_text(cmt);
                        update(array_cmt.get(position), position);
                    }

                    @Override
                    public void onDel(int position) {
                        del(array_cmt.get(position).getCmt_id(), position);
                    }
                });
        binding.rclCmtVideoFrag.setAdapter(adapter);
        binding.rclCmtVideoFrag.setItemAnimator(new DefaultItemAnimator());
        binding.rclCmtVideoFrag.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void loadCmt(){
        for(int i = 0; i < 5; ++i){
            Comment_M cmt = new Comment_M(i, videos_m.getVid_id(),
//                           FirebaseAuth.getInstance().getCurrentUser().toString(),
                    "123" + i,
                    "TEE",
                    "TEST" + i);
            insert(cmt);
        }
    }

    private void insert(Comment_M cmt){
        array_cmt.add(cmt);
        adapter.notifyItemInserted(array_cmt.size());
    }

    private void update(Comment_M cmt, int position){
        binding.layoutSendCmtVideoFrag.setVisibility(View.VISIBLE);
        adapter.notifyItemChanged(position);
    }

    private void del(int cmt_id, int position){
        for(int i = 0; i < array_cmt.size(); ++i){
            if(array_cmt.get(i).getCmt_id() == cmt_id)
                array_cmt.remove(i);
        }
        adapter.notifyItemRemoved(position);
    }
}