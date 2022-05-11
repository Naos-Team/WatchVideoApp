package com.naosteam.watchvideoapp.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
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
import com.naosteam.watchvideoapp.asynctasks.ExecuteQueryAsync;
import com.naosteam.watchvideoapp.databinding.FragmentProfileUpdateBinding;
import com.naosteam.watchvideoapp.listeners.ExecuteQueryAsyncListener;
import com.naosteam.watchvideoapp.models.Users_M;
import com.naosteam.watchvideoapp.utils.Methods;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import okhttp3.RequestBody;

public class ProfileUpdateFragment extends Fragment {
    private View rootView;
    private NavController navController;
    private FragmentProfileUpdateBinding binding;
    private DatabaseReference databaseReference;
    private AwesomeValidation awesomeValidation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileUpdateBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation((Activity) getContext(), R.id.edt_name1, RegexTemplate.NOT_EMPTY, R.string.invalid_signup_name);
        awesomeValidation.addValidation((Activity) getContext(), R.id.edt_phone1, RegexTemplate.TELEPHONE, R.string.invalid_signup_phone);
        awesomeValidation.addValidation((Activity) getContext(), R.id.edt_email1, Patterns.EMAIL_ADDRESS, R.string.invalid_signup_email);
        awesomeValidation.addValidation((Activity) getContext(), R.id.edt_age1, RegexTemplate.NOT_EMPTY, R.string.invalid_age);

        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            databaseReference = FirebaseDatabase.getInstance().getReference();
            binding.imvStart1.setVisibility(View.VISIBLE);
            binding.progressStart1.setVisibility(View.VISIBLE);

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
                            binding.imvStart1.setVisibility(View.GONE);
                            binding.progressStart1.setVisibility(View.GONE);
                            Gson gson = new Gson();
                            JsonElement jsonElement = gson.toJsonTree(task.getResult().getValue());
                            JsonObject obj = jsonElement.getAsJsonObject();
                            Users_M user = gson.fromJson(obj.toString(), Users_M.class);

                            binding.edtName1.setText(user.getUser_name());
                            binding.edtEmail1.setText(user.getUser_email());
                            binding.edtPhone1.setText(user.getUser_phone());
                            binding.edtAge1.setText(String.valueOf(user.getUser_age()));
                            if(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null){
                                Uri uri = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
                                Picasso.get().load(uri).into(binding.imvUser);
                            }
                        }
                    }
                });

        binding.imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navController.navigate(R.id.update_open_profile);
            }
        });

        binding.imvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()){
                    Update_PF();
                }

            }
        });
    }

    private void Update_PF(){

        String email = binding.edtEmail1.getText().toString().trim();
        String name = binding.edtName1.getText().toString().trim();
        String phone = binding.edtPhone1.getText().toString().trim();
        int age =  Integer.parseInt(binding.edtAge1.getText().toString());

        HashMap User = new HashMap();
        User.put("user_name", name);
        User.put("user_email", email);
        User.put("user_phone", phone);
        User.put("user_age", age);
        databaseReference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(User).
                addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            binding.imvStart1.setVisibility(View.VISIBLE);
                            binding.progressStart1.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(), "Successfully. Your info has been updated.", Toast.LENGTH_LONG).show();
//                            navController.navigate(R.id.update_open_profile);
                        }
                        else{
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

//        ExecuteQueryAsyncListener listener = new ExecuteQueryAsyncListener() {
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onEnd(boolean status) {
//
//                if(status){
//                    HashMap User = new HashMap();
//                    User.put("user_name", name);
//                    User.put("user_email", email);
//                    User.put("user_phone", phone);
//                    User.put("user_age", age);
//                    databaseReference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(User).
//                            addOnCompleteListener(new OnCompleteListener() {
//                                @Override
//                                public void onComplete(@NonNull Task task) {
//                                    if(task.isSuccessful()){
//                                        binding.imvStart1.setVisibility(View.VISIBLE);
//                                        binding.progressStart1.setVisibility(View.VISIBLE);
//                                        Toast.makeText(getContext(), "Successfully. Your info has been updated.", Toast.LENGTH_LONG).show();
//                                        navController.navigate(R.id.update_open_profile);
//                                    }
//                                    else{
//                                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                }else{
//                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        };
//
//        Bundle bundle = new Bundle();
//        bundle.putString("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
//        bundle.putString("name", name);
//        bundle.putString("email",email);
//        bundle.putInt("age", age);
//        bundle.putString("phone", phone);
//
//
//        RequestBody requestBody = Methods.getInstance().getLoginRequestBody("METHOD_UPDATE_PROFILE",bundle);
//
//        ExecuteQueryAsync async = new ExecuteQueryAsync(requestBody, listener);
//        async.execute();

    }
}