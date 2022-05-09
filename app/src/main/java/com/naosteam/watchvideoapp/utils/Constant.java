package com.naosteam.watchvideoapp.utils;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.naosteam.watchvideoapp.models.Videos_M;

public class Constant {
    public static final String SERVER_URL = "https://musicfreeworld.com/naosteam/watchvideoapp/";
    public static final String ERR_TAG = "ERROR";
    public static Videos_M Radio_Listening = new Videos_M(-1,-1,"Radio Name","", "","", 0,0,0,0,false,null );
    public static GoogleSignInClient ggclient;
}
