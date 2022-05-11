package com.naosteam.watchvideoapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Range;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.asynctasks.ExecuteQueryAsync;
import com.naosteam.watchvideoapp.databinding.ActivityUpdateProfileBinding;
import com.naosteam.watchvideoapp.listeners.ExecuteQueryAsyncListener;
import com.naosteam.watchvideoapp.models.Users_M;
import com.naosteam.watchvideoapp.utils.Constant;
import com.naosteam.watchvideoapp.utils.Methods;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import okhttp3.RequestBody;

public class UpdateProfileActivity extends AppCompatActivity {
    private View rootView;
    private ActivityUpdateProfileBinding binding;
    private DatabaseReference databaseReference;
    private AwesomeValidation awesomeValidation;
    private static GoogleSignInAccount gg_account;
    private static String gg_email;
    private SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("DataLogin", Context.MODE_PRIVATE);
        gg_email = sharedPreferences.getString("gg_email", "");

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(UpdateProfileActivity.this, R.id.edt_name1, RegexTemplate.NOT_EMPTY, R.string.invalid_signup_name);
        awesomeValidation.addValidation(UpdateProfileActivity.this, R.id.edt_phone1, RegexTemplate.TELEPHONE, R.string.invalid_signup_phone);
        awesomeValidation.addValidation(UpdateProfileActivity.this, R.id.edt_email1, Patterns.EMAIL_ADDRESS, R.string.invalid_signup_email);
        awesomeValidation.addValidation(UpdateProfileActivity.this, R.id.edt_age1, Range.closed(0,100), R.string.invalid_age);

        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            databaseReference = FirebaseDatabase.getInstance().getReference();
            binding.imvStart1.setVisibility(View.VISIBLE);
            binding.progressStart1.setVisibility(View.VISIBLE);

            LoadData();
        }
    }

    private void LoadData(){
        databaseReference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
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
                }
                else{
                    binding.imvBack.setVisibility(View.GONE);
                    binding.imvStart1.setVisibility(View.GONE);
                    binding.progressStart1.setVisibility(View.GONE);
                    binding.edtName1.setHint("Your name");
                    binding.edtEmail1.setText(gg_email);
                    binding.edtEmail1.setEnabled(false);
                    binding.edtPhone1.setHint("Your phone");
                    binding.edtAge1.setHint("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

    private void Update_PF() {

        String email = binding.edtEmail1.getText().toString().trim();
        String name = binding.edtName1.getText().toString().trim();
        String phone = binding.edtPhone1.getText().toString().trim();
        int age = Integer.parseInt(binding.edtAge1.getText().toString());

        Users_M user = new Users_M(FirebaseAuth.getInstance().getUid(), name, email, phone, age);
        HashMap User = new HashMap();
        User.put("user_name", name);
        User.put("user_email", email);
        User.put("user_phone", phone);
        User.put("user_age", age);
        databaseReference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    databaseReference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(User).
                            addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        binding.imvStart1.setVisibility(View.VISIBLE);
                                        binding.progressStart1.setVisibility(View.VISIBLE);
                                        Toast.makeText(UpdateProfileActivity.this, "Successfully. Your info has been updated.", Toast.LENGTH_LONG).show();
                                        onBackPressed();
                                    } else {
                                        Toast.makeText(UpdateProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                CallAsync(FirebaseAuth.getInstance().getCurrentUser());
                                startActivity(new Intent(UpdateProfileActivity.this, MainActivity.class));
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
        private void CallAsync(FirebaseUser user){
            ExecuteQueryAsyncListener listener = new ExecuteQueryAsyncListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onEnd(boolean status) {
                    if(status){
                    }else{
                        Toast.makeText(UpdateProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }

            };

            Bundle bundle = new Bundle();
            bundle.putString("uid", user.getUid());

            RequestBody requestBody = Methods.getInstance().getLoginRequestBody("METHOD_SIGNUP",bundle);
            ExecuteQueryAsync async = new ExecuteQueryAsync(requestBody, listener);
            async.execute();
        }


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