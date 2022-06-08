package com.naosteam.watchvideoapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.databinding.FragmentUpdateProfileBinding;
import com.naosteam.watchvideoapp.models.Users_M;
import com.naosteam.watchvideoapp.utils.AdsManager;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UpdateProfileFragment extends Fragment {

    private static final int RESULT_OK = -1;
    private FragmentUpdateProfileBinding binding;
    private NavController navController;
    private View rootView;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateProfileBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        sharedPreferences = getContext().getSharedPreferences("DataLogin", Context.MODE_PRIVATE);
        gg_email = sharedPreferences.getString("gg_email", "");
        isFirstPhoto = sharedPreferences.getBoolean("isFirstPhoto1", true);

        user = (Users_M) getArguments().getSerializable("user_more");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.imvTemp.setVisibility(View.GONE);
                binding.prTemp.setVisibility(View.GONE);
            }
        },3500);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(getActivity(), R.id.edt_name1, RegexTemplate.NOT_EMPTY, R.string.invalid_signup_name);
        awesomeValidation.addValidation(getActivity(), R.id.edt_phone1, RegexTemplate.TELEPHONE, R.string.invalid_signup_phone);
        awesomeValidation.addValidation(getActivity(), R.id.edt_email1, Patterns.EMAIL_ADDRESS, R.string.invalid_signup_email);
        awesomeValidation.addValidation(getActivity(), R.id.edt_age1, Range.closed(0,100), R.string.invalid_age);


        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            databaseReference = FirebaseDatabase.getInstance().getReference();
            binding.imvStart1.setVisibility(View.VISIBLE);
            binding.progressStart1.setVisibility(View.VISIBLE);

            LoadData();
        }

        return rootView;
    }

    private void LoadData(){
        binding.imvStart1.setVisibility(View.GONE);
        binding.progressStart1.setVisibility(View.GONE);

        if (user!=null){
            binding.edtName1.setText(user.getUser_name());
            binding.edtEmail1.setText(user.getUser_email());
            binding.edtPhone1.setText(user.getUser_phone());
            binding.edtAge1.setText(String.valueOf(user.getUser_age()));
            binding.imvAddAva.setVisibility(View.GONE);
            Picasso.get().load(user.getPhoto_url()).into(binding.imvUser);
        }

        binding.imvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()){
                    Update_PF();
                }
                binding.imvEdit.setVisibility(View.VISIBLE);
                binding.imvSave.setVisibility(View.GONE);
                binding.edtAge1.setEnabled(false);
                binding.edtEmail1.setEnabled(false);
                binding.edtName1.setEnabled(false);
                binding.edtPhone1.setEnabled(false);
                binding.imvAddAva.setVisibility(View.GONE);
            }
        });

        binding.imvAddAva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, 111);
            }
        });

        binding.imvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imvSave.setVisibility(View.VISIBLE);
                binding.imvEdit.setVisibility(View.GONE);
                binding.edtPhone1.setEnabled(true);
                binding.edtAge1.setEnabled(true);
                binding.edtEmail1.setEnabled(false);
                binding.edtName1.setEnabled(true);
                binding.imvAddAva.setVisibility(View.VISIBLE);

            }
        });

        binding.imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdsManager.showAdmobInterAd(getActivity(), new AdsManager.InterAdsListener() {
                    @Override
                    public void onClick() {
                        navController.navigate(R.id.update_to_more);
                    }
                });

//                Intent intent = new Intent(UpdateProfileGoogleActivity.this, MainActivity.class);
//                intent.putExtra("choice", 0);
//                startActivity(intent);
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
                Update_Add_Data(User);
            }
            else{
                User.put("photo_url", "empty");
                user = new Users_M(FirebaseAuth.getInstance().getCurrentUser().getUid(), name, email, phone, age, "empty");
                Update_Add_Data(User);
            }
        }
        else{
            if(url!=null){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isFirstPhoto1", false);
                editor.apply();
                fileRef.putFile(url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                User.put("photo_url", uri.toString());
                                user = new Users_M(FirebaseAuth.getInstance().getCurrentUser().getUid(), name, email, phone, age, uri.toString());
                                Update_Add_Data(User);


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
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isFirstPhoto1", false);
                editor.apply();
                User.put("photo_url", "empty");
                user = new Users_M(FirebaseAuth.getInstance().getCurrentUser().getUid(), name, email, phone, age, "empty");
                Update_Add_Data(User);
            }
        }

    }

    private void Update_Add_Data(HashMap User){
        databaseReference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(User).
                addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                        } else {
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK && data != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstPhoto1", false);
            editor.apply();
            url = data.getData();
            binding.imvUser.setImageURI(url);

        }
    }
}