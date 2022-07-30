package com.naosteam.funnyvideo.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.naosteam.funnyvideo.models.Videos_M;

import java.util.ArrayList;
import java.util.Base64;

public class Constant {
    public static final String SERVER_URL = "https://musicfreeworld.com/naosteam/watchvideoapp123/";
//    public static final String SERVER_URL = "http://192.168.0.166:8100/watchvideoapp/";
    public static String ONESIGNAL_APP_ID = "8c488b3c-c5ca-49ea-ad7d-f3f86c5eb786";
    public static final String ERR_TAG = "ERROR";
    public static final int DAILYMOTION_VIDEO = 33;
    public static final int YOUTUBE_VIDEO = 44;
    public static final int VIDEO = 1;
    public static final int TV = 2;
    public static final int RADIO = 3;
    public static final int SUCCESS = 568;
    public static final int ERR_SERVER = 345;
    public static final int ERR_INTERNET = 676;
    public static Videos_M Radio_Listening = new Videos_M(-1,-1,"Radio Name","", "","", 0,0,0.0f,0,false,null );
    public static GoogleSignInClient ggclient;
//    public static String YOUTUBE_API_KEY = "AIzaSyB0Z7jPCd2k_sLpaym5t_a67uiVNw-J468";
    public static String YOUTUBE_API_KEY = "AIzaSyBKoSLgVwejU4AscaFDThiWnOa0aAt_Tzw";
    public static String ADS_KEY_BANNER = "";
    public static String ADS_KEY_INTERSTIAL = "";
    public static int ADS_DISPLAY_COUNT = 0;
    public static String ADS_KEY_OPENADS = "";
    public static String ARR_VID_TREND = "";
    public static String ARR_TV_TREND = "";
    public static String ARR_RADIO_TREND = "";
    public static ArrayList<Integer> LIST_TRENDING_VID = new ArrayList<>();
    public static ArrayList<Integer> LIST_TRENDING_TV = new ArrayList<>();
    public static ArrayList<Integer> LIST_TRENDING_RADIO = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String DECODE_BASE64(String s){
        try{
            byte[] decodedBytes = Base64.getDecoder().decode(s);
            String decodedString = new String(decodedBytes);
            return decodedString;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String ENCODE_BASE64 (String s){
        try {
            String originalInput = "test input";
            String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
            return encodedString;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
