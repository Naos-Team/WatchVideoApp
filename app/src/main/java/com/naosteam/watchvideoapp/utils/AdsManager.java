package com.naosteam.watchvideoapp.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class AdsManager {
    public static int adCounter = 1;
    public static int adDisplayCounter = 3;
    public static boolean isEnableBanner = true;
    public static boolean isEnableInterstitial = true;
    public static InterstitialAd mInterstitialAd;
    public interface InterAdsListener{
        void onClick();
    }

    public static void loadAdmobBanner(Context context, LinearLayout linearLayout){
        if(isEnableInterstitial){
            AdView adView = new AdView(context);
            Bundle extras = new Bundle();
            extras.putString("npa", "1");
            AdRequest adRequest = new AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                    .build();
            //adView.setAdUnitId(Constants.ad_banner_id);
            adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
            adView.setAdSize(AdSize.BANNER);
            linearLayout.addView(adView);
            adView.loadAd(adRequest);
        }
    }

    //TODO: interAds
    public static void showAdmobInterAd(final Activity context, InterAdsListener listener) {
        if (adCounter == adDisplayCounter && mInterstitialAd != null && isEnableInterstitial) {
            adCounter = 1;
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    loadInterAd(context);
                    listener.onClick();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    mInterstitialAd = null;
                }
            });
            mInterstitialAd.show((Activity) context);
        } else {
            adCounter++;
            listener.onClick();
        }
    }

    public static void loadInterAd(Context context) {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, "ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                mInterstitialAd = null;
            }
        });
    }
}