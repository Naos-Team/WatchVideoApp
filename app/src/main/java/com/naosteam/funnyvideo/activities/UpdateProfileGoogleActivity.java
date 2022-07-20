package com.naosteam.funnyvideo.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.naosteam.funnyvideo.R;
import com.naosteam.funnyvideo.asynctasks.ExecuteQueryAsync;
import com.naosteam.funnyvideo.databinding.ActivityUpdateProfileBinding;
import com.naosteam.funnyvideo.listeners.ExecuteQueryAsyncListener;
import com.naosteam.funnyvideo.models.Users_M;
import com.naosteam.funnyvideo.utils.AdsManager;
import com.naosteam.funnyvideo.utils.Methods;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import okhttp3.RequestBody;

public class UpdateProfileGoogleActivity extends AppCompatActivity {
    private View rootView;
    private ActivityUpdateProfileBinding binding;
    private DatabaseReference databaseReference;
    private AwesomeValidation awesomeValidation;
    private static GoogleSignInAccount gg_account;
    private static String gg_email;
    private SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private static Uri url, url_gg;
    private static Users_M user;
    private Boolean isFirstPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        sharedPreferences = getSharedPreferences("DataLogin", Context.MODE_PRIVATE);
        gg_email = sharedPreferences.getString("gg_email", "");
        isFirstPhoto = sharedPreferences.getBoolean("isFirstPhoto", true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.imvTemp.setVisibility(View.GONE);
                binding.prTemp.setVisibility(View.GONE);
            }
        },3500);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(UpdateProfileGoogleActivity.this, R.id.edt_name1, RegexTemplate.NOT_EMPTY, R.string.invalid_signup_name);
        awesomeValidation.addValidation(UpdateProfileGoogleActivity.this, R.id.edt_phone1, RegexTemplate.TELEPHONE, R.string.invalid_signup_phone);
        awesomeValidation.addValidation(UpdateProfileGoogleActivity.this, R.id.edt_email1, Patterns.EMAIL_ADDRESS, R.string.invalid_signup_email);
        awesomeValidation.addValidation(UpdateProfileGoogleActivity.this, R.id.edt_age1, Range.closed(0,100), R.string.invalid_age);

        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            databaseReference = FirebaseDatabase.getInstance().getReference();
            binding.imvStart1.setVisibility(View.VISIBLE);
            binding.progressStart1.setVisibility(View.VISIBLE);

            LoadData();
        }
    }

    private void LoadData(){
        binding.imvStart1.setVisibility(View.GONE);
        binding.progressStart1.setVisibility(View.GONE);

        binding.edtAge1.setEnabled(true);
        binding.edtEmail1.setEnabled(false);
        binding.edtName1.setEnabled(true);
        binding.edtPhone1.setEnabled(true);
        binding.imvSave.setVisibility(View.VISIBLE);
        binding.imvEdit.setVisibility(View.GONE);
        binding.edtAge1.setEnabled(true);
        binding.imvBack.setVisibility(View.GONE);
        binding.edtName1.setHint("Your name");
        binding.edtEmail1.setText(gg_email);
        binding.edtEmail1.setEnabled(false);
        binding.edtPhone1.setHint("Your phone");
        binding.edtAge1.setHint("0");
        if(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null){
                url_gg = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
                Picasso.get().load(url_gg).into(binding.imvUser);
            }

        binding.imvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()){
                    Update_PF();
                }
                binding.imvAddAva.setVisibility(View.GONE);
                AdsManager.showAdmobInterAd(UpdateProfileGoogleActivity.this, new AdsManager.InterAdsListener() {
                    @Override
                    public void onClick() {
                        Intent intent = new Intent(UpdateProfileGoogleActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

            }
        });

        binding.imvAddAva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, 2);
            }
        });

    }

    private void Update_PF() {
        String email = binding.edtEmail1.getText().toString().trim();
        String name = binding.edtName1.getText().toString().trim();
        String phone = binding.edtPhone1.getText().toString().trim();
        int age = Integer.parseInt(binding.edtAge1.getText().toString());

        HashMap User = new HashMap();
        User.put("user_name", name);
        User.put("user_email", email);
        User.put("user_phone", phone);
        User.put("user_age", age);

        StorageReference fileRef = storageReference.child("Image").child(mAuth.getCurrentUser().getUid());

        if (isFirstPhoto){
            if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null) {
                url_gg = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
                User.put("photo_url", url_gg.toString());
                user = new Users_M(FirebaseAuth.getInstance().getCurrentUser().getUid(), name, email, phone, age, url_gg.toString());
                Update_Add_Data(user);
            }
            else{
                User.put("photo_url", "empty");
                user = new Users_M(FirebaseAuth.getInstance().getCurrentUser().getUid(), name, email, phone, age, "empty");
                Update_Add_Data(user);
            }
        }
        else{
            if(url!=null){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isFirstPhoto", false);
                editor.apply();
                fileRef.putFile(url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                User.put("photo_url", uri.toString());
                                user = new Users_M(FirebaseAuth.getInstance().getCurrentUser().getUid(), name, email, phone, age, uri.toString());
                                Update_Add_Data(user);


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
            }
            else{
                User.put("photo_url", "empty");
                user = new Users_M(FirebaseAuth.getInstance().getCurrentUser().getUid(), name, email, phone, age, "empty");
                Update_Add_Data(user);
            }
        }

    }

    private void Update_Add_Data(Users_M user){
        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                CallAsync(FirebaseAuth.getInstance().getCurrentUser());
                                AdsManager.showAdmobInterAd(UpdateProfileGoogleActivity.this, new AdsManager.InterAdsListener() {
                                    @Override
                                    public void onClick() {
                                        startActivity(new Intent(UpdateProfileGoogleActivity.this, MainActivity.class));
                                    }
                                });

                            }
                        }}
                ).addOnFailureListener(new OnFailureListener() {
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

    private void CallAsync(FirebaseUser user){
        ExecuteQueryAsyncListener listener = new ExecuteQueryAsyncListener() {
            @Override
            public void onStart() {}

            @Override
            public void onEnd(boolean status) {
                if(status){
                }else{
                    Toast.makeText(UpdateProfileGoogleActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

        };

        Bundle bundle = new Bundle();
        bundle.putString("uid", user.getUid());

        RequestBody requestBody = Methods.getInstance().getLoginRequestBody("METHOD_SIGNUP",bundle);
        ExecuteQueryAsync async = new ExecuteQueryAsync(requestBody, listener);
        async.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstPhoto", false);
            editor.apply();
            url = data.getData();
            binding.imvUser.setImageURI(url);

        }
    }

}