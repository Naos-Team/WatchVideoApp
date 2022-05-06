package com.naosteam.watchvideoapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Base64;

import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Methods {
    private static Methods Instance;

    private Methods(){}

    public static Methods getInstance(){
        if(Instance == null){
            Instance = new Methods();
        }
        return Instance;
    }

    public boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public String base64Encode(String input){
        byte[] encodeValue = Base64.encode(input.getBytes(), Base64.DEFAULT);
        return (new String(encodeValue)).trim();
    }

    public String base64Decode(String input) throws UnsupportedEncodingException {
        byte[] encodeValue = Base64.decode(input, Base64.DEFAULT);
        return (new String(encodeValue, "UTF-8")).trim();
    }

    public boolean checkForEncode(String string) {
        String pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(string);
        return m.find();
    }

    public RequestBody getHomeRequestBody(String method_name, Bundle bundle) {
        JsonObject postObj = new JsonObject();
        postObj.addProperty("method_name", method_name);

        switch (method_name){
            case "method_signup":
                postObj.addProperty("uid", base64Encode(bundle.getString("uid")));
                postObj.addProperty("name", base64Encode(bundle.getString("name")));
                postObj.addProperty("email", base64Encode(bundle.getString("email")));
                postObj.addProperty("date", bundle.getString("date"));
                break;
        }

        String post_data = postObj.toString();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("data", post_data);

        return builder.build();

    }
}
