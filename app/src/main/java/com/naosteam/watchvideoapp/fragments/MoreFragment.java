package com.naosteam.watchvideoapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.activities.LoginActivity;
import com.naosteam.watchvideoapp.databinding.FragmentMoreBinding;
import com.naosteam.watchvideoapp.databinding.FragmentRadioBinding;
import com.naosteam.watchvideoapp.models.Users_M;
import com.naosteam.watchvideoapp.utils.Constant;
import com.squareup.picasso.Picasso;

public class MoreFragment extends Fragment {
    private View rootView;
    private NavController navController;
    private FragmentMoreBinding binding;
    private DatabaseReference databaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMoreBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);

        LoadUI();

        LogOut();

        return rootView;
    }

    private void LoadUI(){
        if (FirebaseAuth.getInstance().getCurrentUser()==null){
            binding.tvNameUser.setText("You haven't logged in");
            binding.tvEmailUser.setText("Join us");
        }
        else{
            databaseReference = FirebaseDatabase.getInstance().getReference();
            binding.imvStart.setVisibility(View.VISIBLE);
            binding.progressStart.setVisibility(View.VISIBLE);
            LoadData();
        }

        binding.constraintlayout19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FirebaseAuth.getInstance().getCurrentUser()==null){
                    Toast.makeText(getContext(), "You haven't logged in. Let's join us!", Toast.LENGTH_SHORT).show();
                }
                else{
                    navController.navigate(R.id.more_open_profile);
                }

            }
        });
    }

    private void LoadData() {


        databaseReference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            binding.imvStart.setVisibility(View.GONE);
                            binding.progressStart.setVisibility(View.GONE);
                            Gson gson = new Gson();
                            JsonElement jsonElement = gson.toJsonTree(task.getResult().getValue());
                            JsonObject obj = jsonElement.getAsJsonObject();
                            Users_M user = gson.fromJson(obj.toString(), Users_M.class);

                            binding.tvNameUser.setText(user.getUser_name());
                            binding.tvEmailUser.setText(user.getUser_email());
                            if(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null){
                                Uri uri = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
                                Picasso.get().load(uri).into(binding.imvUser);
                            }
                        }
                    }
                });
    }

    private void LogOut(){
        binding.constraintlayout25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                    FirebaseAuth.getInstance().signOut();
                    if (Constant.ggclient!=null){
                        Constant.ggclient.signOut();
                    }

                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getContext(), "You haven't logged in. Let's join us!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}