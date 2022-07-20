package com.naosteam.funnyvideo.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.naosteam.funnyvideo.R;
import com.naosteam.funnyvideo.databinding.ActivityMainBinding;
import com.naosteam.funnyvideo.listeners.OnUpdateViewRadioPlayListener;
import com.naosteam.funnyvideo.utils.AdsManager;
import com.naosteam.funnyvideo.utils.Constant;
import com.naosteam.funnyvideo.utils.PlayerRadio;
import com.onesignal.OneSignal;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private NavHostFragment navHostFragment;
    private static BottomNavigationView bottomNavigationView;
    public static int SET_PORTRAIT_REQUEST_CODE;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(Constant.ONESIGNAL_APP_ID);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

        navController = navHostFragment.getNavController();
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        loadAds();

        PlayerRadio.setContext(MainActivity.this);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {

            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if(navDestination.getId() != R.id.baseRadioFragment){
                    PlayerRadio playerRadio = PlayerRadio.getInstance(new OnUpdateViewRadioPlayListener() {
                        @Override
                        public void onBuffering() {

                        }

                        @Override
                        public void onReady() {

                        }

                        @Override
                        public void onEnd() {

                        }
                    });
                    if(playerRadio.checkPlay()){
                        playerRadio.pauseRadio();
                    }
                }
            }
        });
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        if(getIntent() != null){
            Intent intent = getIntent();
            if (intent.getExtras() != null &&
                    intent.getExtras().getInt("choice", -1) == 0) {
                bottomNavigationView.setSelectedItemId(R.id.baseMoreFragment);
            }
        }

    }

    @Override
    public void onBackPressed() {

        if(navHostFragment.getChildFragmentManager().getBackStackEntryCount() < 1){
            showQuitDialog();
        }else{
            AdsManager.showAdmobInterAd(MainActivity.this, new AdsManager.InterAdsListener() {
                @Override
                public void onClick() {
                    MainActivity.super.onBackPressed();
                }
            });
        }


    }

    private void loadAds() {
        AdsManager.loadInterAd(this);
        AdsManager.loadAdmobBanner(this, binding.llAdview);
    }


    public static void hide_Navi(){
        bottomNavigationView.setVisibility(View.GONE);
    }

    public static void show_Navi(){
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    public static void choice_Navi(int id_case){
        bottomNavigationView.setSelectedItemId(id_case);
    }

    private void showQuitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view1 = LayoutInflater.from(this).inflate(R.layout.layout_dialog_quit, null,false);
        builder.setView(view1);

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btn_submit = view1.findViewById(R.id.btn_submit);
        Button btn_cancel = view1.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });

        alertDialog.show();

    }
}