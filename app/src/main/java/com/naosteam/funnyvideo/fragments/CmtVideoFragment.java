package com.naosteam.funnyvideo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.naosteam.funnyvideo.R;
import com.naosteam.funnyvideo.activities.MainActivity;
import com.naosteam.funnyvideo.adapters.CmtVideoAdapter;
import com.naosteam.funnyvideo.asynctasks.ExecuteQueryAsync;
import com.naosteam.funnyvideo.asynctasks.LoadCmtAsync;
import com.naosteam.funnyvideo.databinding.FragmentCmtVideoBinding;
import com.naosteam.funnyvideo.listeners.ExecuteQueryAsyncListener;
import com.naosteam.funnyvideo.listeners.LoadCmtListener;
import com.naosteam.funnyvideo.listeners.OnCmtItemListener;
import com.naosteam.funnyvideo.models.Comment_M;
import com.naosteam.funnyvideo.models.Videos_M;
import com.naosteam.funnyvideo.utils.Methods;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.RequestBody;

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

        binding.swiperCmtFrag.setColorSchemeColors(getResources().getColor(R.color.neonGreen));
        binding.swiperCmtFrag.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadCmt();
                binding.swiperCmtFrag.setRefreshing(false);
            }
        });

        binding.txtTitleCmtVideoFrag.setText(videos_m.getVid_title());

        binding.btnSendCmtVideoFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.txtCmtVideoFrag.getText().toString().equals("")){
                    return;
                }
                if(FirebaseAuth.getInstance().getCurrentUser() == null){
                   Toast.makeText(getActivity(), "Please login to comment!", Toast.LENGTH_SHORT).show();
                } else {
                   DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                   Date date =new Date();
                   try {
                       date = dateFormat.parse(dateFormat.format(new Date().getTime()));
                   } catch (ParseException e) {
                       e.printStackTrace();
                   }
                   Comment_M cmt = new Comment_M(-1, videos_m.getVid_id(),
                           FirebaseAuth.getInstance().getCurrentUser().getUid(),
                           dateFormat.format(date),
                           binding.txtCmtVideoFrag.getText().toString());
                   binding.txtCmtVideoFrag.setText("");
                   insert(cmt);
                }
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
                        binding.txtCmtVideoFrag.setVisibility(visibility);
                        binding.btnSendCmtVideoFrag.setVisibility(visibility);
                    }

                    @Override
                    public void onEdit(int position, String cmt) {
                        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(binding.btnSendCmtVideoFrag.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        array_cmt.get(position).setCmt_text(cmt);
                        update(array_cmt.get(position));
                    }

                    @Override
                    public void onDel(int position) {
                        del(array_cmt.get(position).getCmt_id());
                    }
                });
        binding.rclCmtVideoFrag.setAdapter(adapter);
        binding.rclCmtVideoFrag.setItemAnimator(new DefaultItemAnimator());
        binding.rclCmtVideoFrag.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void loadCmt(){
        LoadCmtListener listener_load = new LoadCmtListener() {
            @Override
            public void onPre() {
                binding.prgCmtVideoFrag.setVisibility(View.VISIBLE);
                binding.imgTempCmtVideoFrag.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEnd(Boolean is_done, ArrayList<Comment_M> list_cmt) {
                binding.prgCmtVideoFrag.setVisibility(View.GONE);
                binding.imgTempCmtVideoFrag.setVisibility(View.GONE);
                if(getContext() != null) {
                    if (Methods.getInstance().isNetworkConnected(getContext())) {
                        if (is_done) {
                            array_cmt.clear();
                            array_cmt.addAll(list_cmt);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "Something wrong happened, try again!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Please connect to the internet!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        Bundle bundle = new Bundle();
        bundle.putInt("vid_id", videos_m.getVid_id());
        RequestBody requestBody = Methods.getInstance().GetCmtRequestBody("GET_CMT_DATA", bundle);
        LoadCmtAsync async = new LoadCmtAsync(requestBody, listener_load);
        async.execute();
    }

    private void insert(Comment_M cmt){
        ExecuteQueryAsyncListener listener = new ExecuteQueryAsyncListener() {
            @Override
            public void onStart() {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(binding.btnSendCmtVideoFrag.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                binding.prgCmtVideoFrag.setVisibility(View.VISIBLE);
                binding.imgTempCmtVideoFrag.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEnd(boolean status) {
                if(getContext() != null){
                    if(Methods.getInstance().isNetworkConnected(getContext())){
                        loadCmt();
                        if(status){
                        }else{
                            Toast.makeText(getContext(), "Something wrong happened, try again!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getContext(), "Please connect to the internet!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        };

        Bundle bundle = new Bundle();
        bundle.putString("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        bundle.putInt("vid_id", cmt.getVid_id());
        bundle.putString("cmt_time", cmt.getCmt_time());
        bundle.putString("cmt_text", cmt.getCmt_text());

        RequestBody requestBody = Methods.getInstance().GetCmtRequestBody("INSERT_CMT",bundle);
        ExecuteQueryAsync async = new ExecuteQueryAsync(requestBody, listener);
        async.execute();

    }

    private void update(Comment_M cmt){
        ExecuteQueryAsyncListener listener = new ExecuteQueryAsyncListener() {
            @Override
            public void onStart() {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(binding.btnSendCmtVideoFrag.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                binding.prgCmtVideoFrag.setVisibility(View.VISIBLE);
                binding.imgTempCmtVideoFrag.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEnd(boolean status) {
                if(getContext() != null){
                    if(Methods.getInstance().isNetworkConnected(getContext())){
                        loadCmt();
                        if(status){
                        }else{
                            Toast.makeText(getContext(), "Something wrong happened, try again!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getContext(), "Please connect to the internet!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        };

        Bundle bundle = new Bundle();
        bundle.putInt("cmt_id", cmt.getCmt_id());
        bundle.putString("cmt_text", cmt.getCmt_text());
        RequestBody requestBody = Methods.getInstance().GetCmtRequestBody("UPDATE_CMT",bundle);
        ExecuteQueryAsync async = new ExecuteQueryAsync(requestBody, listener);
        async.execute();
    }

    private void del(int cmt_id){
        ExecuteQueryAsyncListener listener = new ExecuteQueryAsyncListener() {
            @Override
            public void onStart() {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(binding.btnSendCmtVideoFrag.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                binding.prgCmtVideoFrag.setVisibility(View.VISIBLE);
                binding.imgTempCmtVideoFrag.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEnd(boolean status) {
                if(getContext() != null){
                    if(Methods.getInstance().isNetworkConnected(getContext())){
                        loadCmt();
                        if(status){
                        }else{
                            Toast.makeText(getContext(), "Something wrong happened, try again!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getContext(), "Please connect to the internet!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        };

        Bundle bundle = new Bundle();
        bundle.putInt("cmt_id", cmt_id);
        RequestBody requestBody = Methods.getInstance().GetCmtRequestBody("DEL_CMT",bundle);
        ExecuteQueryAsync async = new ExecuteQueryAsync(requestBody, listener);
        async.execute();
    }
}