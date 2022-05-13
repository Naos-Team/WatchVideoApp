package com.naosteam.watchvideoapp.utils;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.naosteam.watchvideoapp.models.Videos_M;

public class Constant {
    public static final String SERVER_URL = "https://musicfreeworld.com/naosteam/watchvideoapp/";
    public static final String ERR_TAG = "ERROR";
    public static final int DAILYMOTION_VIDEO = 33;
    public static final int YOUTUBE_VIDEO = 44;
    public static Videos_M Radio_Listening = new Videos_M(-1,-1,"Radio Name","", "","", 0,0,0.0f,0,false,null );
    public static GoogleSignInClient ggclient;
    public static String YOUTUBE_API_KEY = "AIzaSyB0Z7jPCd2k_sLpaym5t_a67uiVNw-J468";
}
