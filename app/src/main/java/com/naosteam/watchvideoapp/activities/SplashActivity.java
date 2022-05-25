package com.naosteam.watchvideoapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.asynctasks.ExecuteQueryAsync;
import com.naosteam.watchvideoapp.asynctasks.GetSettingAsync;
import com.naosteam.watchvideoapp.listeners.ExecuteQueryAsyncListener;
import com.naosteam.watchvideoapp.utils.Constant;
import com.naosteam.watchvideoapp.utils.Methods;

import org.json.JSONObject;

import okhttp3.RequestBody;

public class SplashActivity extends AppCompatActivity {
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    private String gg_email;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progress_bar_splash);
        progressBar.setIndeterminateDrawableTiled(new WanderingCubes());

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("DataLogin", Context.MODE_PRIVATE);
        gg_email = sharedPreferences.getString("gg_email", "");

        Load_Async();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Methods.getInstance().isNetworkConnected(SplashActivity.this)){

                    if (mAuth.getCurrentUser()!=null){
                        if (gg_email!=null){
                            CheckUser(mAuth.getCurrentUser());
                        }
                        else{
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        }
                    }
                    else{
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                }else{
                    Constant.ADS_KEY_BANNER = "";
                    Constant.ADS_KEY_INTERSTIAL = "";
                    Constant.ADS_DISPLAY_COUNT = 0;
                    Constant.ADS_KEY_OPENADS = "";
                    Constant.ARR_VID_TREND = "";
                    Constant.ARR_TV_TREND = "";
                    Constant.ARR_RADIO_TREND = "";
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
            }
        }, 3000);
    }

    private void CheckUser(FirebaseUser user){
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashActivity.this, UpdateProfileGoogleActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Load_Async(){
        ExecuteQueryAsyncListener listener = new ExecuteQueryAsyncListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onEnd(boolean status) {
                if(status){

                }else{
                    Constant.ADS_KEY_BANNER = "";
                    Constant.ADS_KEY_INTERSTIAL = "";
                    Constant.ADS_DISPLAY_COUNT = 0;
                    Constant.ADS_KEY_OPENADS = "";
                    Constant.ARR_VID_TREND = "";
                    Constant.ARR_TV_TREND = "";
                    Constant.ARR_RADIO_TREND = "";
                }
            }

        };

        RequestBody requestBody = Methods.getInstance().getSettingRequestBody("GET_SETTING",null);
        GetSettingAsync async = new GetSettingAsync(requestBody, listener);
        async.execute();
    }
}