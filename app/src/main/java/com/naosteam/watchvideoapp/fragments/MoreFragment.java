package com.naosteam.watchvideoapp.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.activities.LoginActivity;
import com.naosteam.watchvideoapp.activities.SignUpActivity;
import com.naosteam.watchvideoapp.databinding.FragmentMoreBinding;
import com.naosteam.watchvideoapp.models.Users_M;
import com.naosteam.watchvideoapp.utils.Constant;
import com.squareup.picasso.Picasso;

import java.io.File;

public class MoreFragment extends Fragment {
    private View rootView;
    private NavController navController;
    private FragmentMoreBinding binding;
    private DatabaseReference databaseReference;
    private static Users_M user;
    private static boolean firstt_time = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMoreBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);

        LoadUI();

        if(user!=null && firstt_time==false){
            binding.tvNameUser.setText(user.getUser_name());
            binding.tvEmailUser.setText(user.getUser_email());
            Picasso.get().load(user.getPhoto_url()).into(binding.imvUser);
        }

        binding.imvStart.setVisibility(View.GONE);
        binding.progressStart.setVisibility(View.GONE);
        return rootView;
    }

    private void LoadUI(){

        binding.swiperMoreFrag.setColorSchemeColors(getResources().getColor(R.color.neonGreen));
        binding.swiperMoreFrag.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (FirebaseAuth.getInstance().getCurrentUser()==null){
                    binding.tvNameUser.setText("You haven't logged in");
                    binding.tvEmailUser.setText("Join us");
                    binding.imvStart.setVisibility(View.GONE);
                    binding.progressStart.setVisibility(View.GONE);
                }
                else{
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    LoadData();
                }
                binding.swiperMoreFrag.setRefreshing(false);
            }
        });

        if(firstt_time){
            binding.imvStart.setVisibility(View.VISIBLE);
            binding.progressStart.setVisibility(View.VISIBLE);

            if (FirebaseAuth.getInstance().getCurrentUser()==null){
                binding.tvNameUser.setText("You haven't logged in");
                binding.tvEmailUser.setText("Join us");
                binding.imvStart.setVisibility(View.GONE);
                binding.progressStart.setVisibility(View.GONE);
            }
            else{
                databaseReference = FirebaseDatabase.getInstance().getReference();
                LoadData();
            }
            firstt_time = false;
        }

        binding.constraintlayout19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FirebaseAuth.getInstance().getCurrentUser()==null){
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
                else{
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user_more", user);
                    navController.navigate(R.id.more_to_update, bundle);

                }

            }
        });

        LogOut();


        binding.constraintlayout17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FirebaseAuth.getInstance().getCurrentUser()==null){
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        binding.constraintlayout21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    navController.navigate(R.id.more_to_privacy);
            }
        });

        binding.constraintlayout20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FirebaseAuth.getInstance().getCurrentUser()==null){
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
                else{
                    navController.navigate(R.id.more_to_favorite);
                }

            }
        });

        binding.constraintlayout23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String body = "Download This App";
                String sub = "http://play.google.com";
                intent.putExtra(Intent.EXTRA_TEXT,body);
                intent.putExtra(Intent.EXTRA_TEXT,sub);
                startActivity(Intent.createChooser(intent, "Share using"));
            }
        });

        binding.constraintlayout22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Uri uri = Uri.parse("market://details?id="+ getContext().getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                catch(ActivityNotFoundException e){
                    Uri uri = Uri.parse("http://play.google.com/store/apps/details?id="+getContext().getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        binding.constraintlayout24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File cache = getContext().getCacheDir();
                File appDir = new File(cache.getParent());
                if (appDir.exists()) {
                    String[] children = appDir.list();
                    for (String s : children) {
                        if (!s.equals("lib")) {
                            deleteDir(new File(appDir, s));
                            Toast.makeText(getContext(), "Clear cache data successfully!", Toast.LENGTH_SHORT).show();
                            Log.i("EEEEEERRRRRROOOOOOORRRR", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                        }
                    }
                }
            }
        });


    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            int i = 0;
            while (i < children.length) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
                i++;
            }
        }
        assert dir != null;
        return dir.delete();
    }

    private void LoadData() {

        databaseReference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {

                            Gson gson = new Gson();
                            JsonElement jsonElement = gson.toJsonTree(task.getResult().getValue());
                            JsonObject obj = jsonElement.getAsJsonObject();
                            user = gson.fromJson(obj.toString(), Users_M.class);

                            binding.tvNameUser.setText(user.getUser_name());
                            binding.tvEmailUser.setText(user.getUser_email());
                            Picasso.get().load(user.getPhoto_url()).into(binding.imvUser);
                        }
                        binding.imvStart.setVisibility(View.GONE);
                        binding.progressStart.setVisibility(View.GONE);
                    }
                });
    }

    private void LogOut(){
        binding.constraintlayout25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn_yes, btn_no;
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view1 = LayoutInflater.from(getContext()).inflate(R.layout.logout_alert_layout, null,false);
                builder.setView(view1);
                builder.setCancelable(false);

                btn_yes = view1.findViewById(R.id.btn_yes);
                btn_no = view1.findViewById(R.id.btn_no);

                final AlertDialog alertDialog = builder.create();

                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        FirebaseAuth.getInstance().signOut();
                        if (Constant.ggclient!=null){
                            Constant.ggclient.signOut();
                        }
                        Constant.Radio_Listening = null;
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                });

                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                    alertDialog.show();
                }
                else{
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}