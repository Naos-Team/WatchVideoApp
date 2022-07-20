package com.naosteam.funnyvideo.utils;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.naosteam.funnyvideo.activities.SplashActivity;


public class AppOpenAdsManager {

    private static AppOpenAd mAppOpenAd;

    private final Context context;
    private final OpenAdsListener listener;

    @RequiresApi(api = Build.VERSION_CODES.P)
    public AppOpenAdsManager(Context context, OpenAdsListener listener) {
        this.context = context;
        this.listener = listener;
        fetchAd();

    }

    public void fetchAd() {
        if(isAdAvailable()){
            return;
        }
        AdRequest request = getAdRequest();
        AppOpenAd.AppOpenAdLoadCallback loadCallback;
        AppOpenAd.load(
                context, SharedPref.getInstance(context).getOpenAdsKey(), request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                        mAppOpenAd = appOpenAd;
                        super.onAdLoaded(appOpenAd);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                    }
                }
        );
    };

    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    public boolean isAdAvailable() {
        if(mAppOpenAd != null){
            return true;
        }else{
            return false;
        }
    }

    public void showAdIfAvailable(){
        if(isAdAvailable()){
            FullScreenContentCallback fullScreenContentCallback =
                    new FullScreenContentCallback(){
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            mAppOpenAd = null;
                            fetchAd();
                            listener.onClick();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                        }
                    };
            mAppOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            mAppOpenAd.show((SplashActivity)context);
        }else{
            fetchAd();
            listener.onClick();
        }
    }

    public interface OpenAdsListener {
        void onClick();
    }


}