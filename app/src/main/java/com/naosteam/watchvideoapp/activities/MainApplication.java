package com.naosteam.watchvideoapp.activities;

import android.app.Application;

import com.naosteam.watchvideoapp.utils.Constant;
import com.onesignal.OneSignal;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(Constant.ONESIGNAL_APP_ID);
    }
}