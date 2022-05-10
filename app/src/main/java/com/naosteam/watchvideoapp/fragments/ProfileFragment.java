package com.naosteam.watchvideoapp.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.naosteam.watchvideoapp.databinding.FragmentMoreBinding;
import com.naosteam.watchvideoapp.databinding.FragmentProfileBinding;
import com.naosteam.watchvideoapp.models.Users_M;
import com.squareup.picasso.Picasso;

import java.util.Map;
import java.util.Set;

public class ProfileFragment extends Fragment {
    private View rootView;
    private NavController navController;
    private FragmentProfileBinding binding;
    private DatabaseReference databaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);

        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            databaseReference = FirebaseDatabase.getInstance().getReference();
            binding.imvStart.setVisibility(View.VISIBLE);
            binding.progressStart.setVisibility(View.VISIBLE);
            LoadData();
        }

        return rootView;
    }

    private void LoadData(){

        databaseReference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        if(task.isSuccessful()){
                            binding.imvStart.setVisibility(View.GONE);
                            binding.progressStart.setVisibility(View.GONE);
                            Gson gson = new Gson();
                            JsonElement jsonElement = gson.toJsonTree(task.getResult().getValue());
                            JsonObject obj = jsonElement.getAsJsonObject();
                            Users_M user = gson.fromJson(obj.toString(), Users_M.class);

                            binding.edtName.setText(user.getUser_name());
                            binding.edtEmail.setText(user.getUser_email());
                            binding.edtPhone.setText(user.getUser_phone());
                            binding.edtAge.setText(String.valueOf(user.getUser_age()));
                            if(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null){
                                Uri uri = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
                                Picasso.get().load(uri).into(binding.imvUser);
                            }
                        }
                    }
                });

        binding.imvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.profile_to_update);
            }
        });

        binding.imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.profile_to_more);
            }
        });
    }
}