package com.naosteam.watchvideoapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;
import com.naosteam.watchvideoapp.asynctasks.CheckFavAsync;
import com.naosteam.watchvideoapp.listeners.CheckFavAsyncListener;
import com.naosteam.watchvideoapp.listeners.CheckFavListener;
import com.naosteam.watchvideoapp.models.Category_M;
import com.naosteam.watchvideoapp.models.Users_M;
import com.naosteam.watchvideoapp.models.Videos_M;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public void checkVideoFav(Context context, int vid_id, CheckFavListener listener) {
        if(FirebaseAuth.getInstance().getCurrentUser() != null){

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            Bundle bundle = new Bundle();
            bundle.putInt("vid_id", vid_id);
            bundle.putString("uid", uid);
            RequestBody requestBody = getHomeRequestBody("CHECK_IS_FAV", bundle);

            CheckFavAsync async = new CheckFavAsync(requestBody, new CheckFavAsyncListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onEnd(boolean status, boolean isFav) {
                    if(context != null){
                        if(isNetworkConnected(context)){
                            if(status){
                                listener.isFav(isFav);
                            }else{
                                // call when query fail
                                listener.onFailure();
                            }
                        }else{
                            listener.onFailure();
                            Toast.makeText(context, "Please check internet connection!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }, this);

            async.execute();


        }else{
            listener.onFailure();
            Toast.makeText(context, "Please login first!", Toast.LENGTH_SHORT).show();
        }
    }

    public Videos_M getRowVideo(JSONObject obj) throws Exception {
        int vid_id =  obj.getInt("vid_id");
        int cat_id = obj.getInt("cat_id");
        String vid_title = checkForEncode(obj.getString("vid_title"))
                ? base64Decode(obj.getString("vid_title"))
                : obj.getString("vid_title") ;
        String vid_url = checkForEncode(obj.getString("vid_url"))
                ? base64Decode(obj.getString("vid_url"))
                : obj.getString("vid_url");
        String vid_thumbnail = checkForEncode(obj.getString("vid_thumbnail"))
                ? base64Decode(obj.getString("vid_thumbnail")) : obj.getString("vid_thumbnail");
        String vid_description = checkForEncode(obj.getString("vid_description"))
                ? base64Decode(obj.getString("vid_description"))
                : obj.getString("vid_description");
        int vid_view = obj.getInt("vid_view");
        int vid_duration = obj.getInt("vid_duration");
        float vid_avg_rate = Float.parseFloat(obj.getString("vid_avg_rate"));
        int vid_type = obj.getInt("vid_type");
        boolean vid_is_premium = obj.getInt("vid_is_premium") == 1;

        String date_string = obj.getString("vid_time");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date vid_time = sdf.parse(date_string);

        return new Videos_M(vid_id, cat_id, vid_title, vid_thumbnail, vid_description, vid_url, vid_view, vid_duration, vid_avg_rate, vid_type, vid_is_premium, vid_time);
    }

    public Category_M getRowCategory(JSONObject obj) throws Exception{
        int cat_id = obj.getInt("cat_id");
        String cat_name = checkForEncode(obj.getString("cat_name"))
                ? base64Decode(obj.getString("cat_name"))
                : obj.getString("cat_name");
        String cat_image = checkForEncode(obj.getString("cat_image"))
                ? base64Decode(obj.getString("cat_image"))
                : obj.getString("cat_image");
        int cat_type = obj.getInt("cat_type");

        return new Category_M(cat_id, cat_name, cat_image, cat_type);
    }

    public Users_M getUser(JSONObject obj) throws Exception {
        String uid = obj.getString("uid");
        String user_name = obj.getString("user_name");
        String user_email = obj.getString("user_email");
        String user_phone = obj.getString("user_phone");
        int user_age = obj.getInt("user_age");
        boolean user_status = obj.getInt("user_status")==1;
        return new Users_M(uid, user_name, user_email, user_phone, user_age);
    }

    public RequestBody getHomeRequestBody(String method_name, Bundle bundle) {
        JsonObject postObj = new JsonObject();
        postObj.addProperty("method_name", method_name);

        switch (method_name){
            case "METHOD_SIGNUP":
                postObj.addProperty("uid", base64Encode(bundle.getString("uid")));
                postObj.addProperty("name", base64Encode(bundle.getString("name")));
                postObj.addProperty("email", base64Encode(bundle.getString("email")));
                postObj.addProperty("phone", base64Encode(bundle.getString("phone")));
                postObj.addProperty("password", base64Encode(bundle.getString("password")));
                postObj.addProperty("age", bundle.getInt("age"));
                break;
        }

        String post_data = postObj.toString();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("data", post_data);

        return builder.build();

    }

    public RequestBody getVideoRequestBody(String method_name, Bundle bundle) {
        JsonObject postObj = new JsonObject();
        postObj.addProperty("method_name", method_name);

        switch (method_name){
            case "LOAD_SEARCH_VIDEO":
                //TODO: encode it again
                postObj.addProperty("search_text", bundle.getString("search_text"));
                postObj.addProperty("page", bundle.getInt("page"));
                postObj.addProperty("step", bundle.getInt("step"));
                break;
        }

        String post_data = postObj.toString();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("data", post_data);

        return builder.build();

    }

    public RequestBody getLoginRequestBody(String method_name, Bundle bundle) {
        JsonObject postObj = new JsonObject();
        postObj.addProperty("method_name", method_name);

        switch (method_name){
            case "METHOD_SIGNUP":
                postObj.addProperty("uid", base64Encode(bundle.getString("uid")));
                postObj.addProperty("name", base64Encode(bundle.getString("name")));
                postObj.addProperty("email", base64Encode(bundle.getString("email")));
                postObj.addProperty("phone", base64Encode(bundle.getString("phone")));
                postObj.addProperty("age", bundle.getInt("age"));
                break;
            case "METHOD_UPDATE_PROFILE":
                postObj.addProperty("uid", base64Encode(bundle.getString("uid")));
                postObj.addProperty("name", base64Encode(bundle.getString("name")));
                postObj.addProperty("email", base64Encode(bundle.getString("email")));
                postObj.addProperty("phone", base64Encode(bundle.getString("phone")));
                postObj.addProperty("age", bundle.getInt("age"));
        }

        String post_data = postObj.toString();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("data", post_data);

        return builder.build();

    }

    public RequestBody GetRadioRequestBody(String method_name, Bundle bundle) {
        JsonObject postObj = new JsonObject();
        postObj.addProperty("method_name", method_name);

        switch (method_name){
            case "LOAD_RADIOS_OF_CATEGORY":
                //TODO: encode it again
                postObj.addProperty("cat_id", bundle.getInt("cat_id"));
                break;
            case "LOAD_RADIO_SCREEN":
                break;
        }

        String post_data = postObj.toString();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("data", post_data);

        return builder.build();

    }
}
