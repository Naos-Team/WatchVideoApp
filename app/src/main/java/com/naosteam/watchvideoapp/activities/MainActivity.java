package com.naosteam.watchvideoapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.naosteam.watchvideoapp.R;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private static BottomNavigationView bottomNavigationView;
    public static int SET_PORTRAIT_REQUEST_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

        navController = navHostFragment.getNavController();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

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
}