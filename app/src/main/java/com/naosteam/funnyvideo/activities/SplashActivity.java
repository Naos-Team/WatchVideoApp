package com.naosteam.funnyvideo.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.QueryPurchasesParams;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naosteam.funnyvideo.R;
import com.naosteam.funnyvideo.asynctasks.GetSettingAsync;
import com.naosteam.funnyvideo.listeners.ExecuteQueryAsyncListener;
import com.naosteam.funnyvideo.utils.AdsManager;
import com.naosteam.funnyvideo.utils.AppOpenAdsManager;
import com.naosteam.funnyvideo.utils.Constant;
import com.naosteam.funnyvideo.utils.Methods;
import com.naosteam.funnyvideo.utils.SharedPref;

import okhttp3.RequestBody;

public class SplashActivity extends AppCompatActivity {
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    private String gg_email;
    private BillingClient billingClient;
    private SharedPreferences sharedPreferences;
    private AppOpenAdsManager appOpenAdsManager;
    private boolean isNeedLogin = false;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progress_bar_splash);
        progressBar.setIndeterminateDrawableTiled(new Wave());

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("DataLogin", Context.MODE_PRIVATE);
        gg_email = sharedPreferences.getString("gg_email", "");

        appOpenAdsManager = new AppOpenAdsManager(SplashActivity.this, new AppOpenAdsManager.OpenAdsListener() {
            @Override
            public void onClick() {

                if(isNeedLogin){
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }else{
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }



            }
        });

        Load_Async();
        checkUserSubscription();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Methods.getInstance().isNetworkConnected(SplashActivity.this)){

                    if (mAuth.getCurrentUser()!=null){
                        if (gg_email!=null){
                            CheckUser(mAuth.getCurrentUser());
                        }
                        else{
                            appOpenAdsManager.showAdIfAvailable();
                        }
                    }
                    else{

                        isNeedLogin = true;
                        appOpenAdsManager.showAdIfAvailable();
                    }
                }else{
                    Constant.ADS_KEY_BANNER = "";
                    Constant.ADS_KEY_INTERSTIAL = "";
                    Constant.ADS_DISPLAY_COUNT = 0;
                    Constant.ADS_KEY_OPENADS = "";
                    Constant.ARR_VID_TREND = "";
                    Constant.ARR_TV_TREND = "";
                    Constant.ARR_RADIO_TREND = "";

                    appOpenAdsManager.showAdIfAvailable();
                }
            }
        }, 3000);
    }

    private void checkUserSubscription() {
        billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener((billingResult, list) -> {}).build();
        final BillingClient finalBillingClient = billingClient;
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {

            }

            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {

                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
                    finalBillingClient.queryPurchasesAsync(
                            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build(),
                            (billingResult1, purchases) -> {
                                if (billingResult1.getResponseCode() == BillingClient.BillingResponseCode.OK){

                                    boolean isPremium = false;

                                    for(Purchase purchase : purchases){
                                        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && purchase.isAcknowledged()) {
                                            isPremium = true;
                                        }
                                    }

                                    SharedPref.getInstance(SplashActivity.this).setIsPremium(isPremium);

                                }else{
                                    SharedPref.getInstance(SplashActivity.this).setIsPremium(false);
                                }
                            });

                }

            }
        });
    }

    private void CheckUser(FirebaseUser user){
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    appOpenAdsManager.showAdIfAvailable();
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
                    for(String w:Constant.ARR_VID_TREND.split(":")){
                        if(w.contains("RESULT"))
                            continue;
                        else{
                            Constant.LIST_TRENDING_VID.add(Integer.parseInt(w));
                        }
                    }
                    for(String w:Constant.ARR_TV_TREND.split(":")){
                        if(w.contains("RESULT"))
                            continue;
                        else{
                            Constant.LIST_TRENDING_TV.add(Integer.parseInt(w));
                        }
                    }
                    for(String w:Constant.ARR_RADIO_TREND.split(":")){
                        if(w.contains("RESULT"))
                            continue;
                        else{
                            Constant.LIST_TRENDING_RADIO.add(Integer.parseInt(w));
                        }
                    }
//
                    SharedPref.getInstance(SplashActivity.this).setOpenAdsKey(Constant.ADS_KEY_OPENADS);

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