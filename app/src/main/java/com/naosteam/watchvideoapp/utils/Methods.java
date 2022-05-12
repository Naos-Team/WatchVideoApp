package com.naosteam.watchvideoapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;
import com.google.type.DateTime;
import com.google.type.DateTimeOrBuilder;
import com.naosteam.watchvideoapp.asynctasks.CheckFavAsync;
import com.naosteam.watchvideoapp.asynctasks.CheckRatingAsync;
import com.naosteam.watchvideoapp.asynctasks.ExecuteQueryAsync;
import com.naosteam.watchvideoapp.asynctasks.SetRatingAsync;
import com.naosteam.watchvideoapp.listeners.CheckFavAsyncListener;
import com.naosteam.watchvideoapp.listeners.CheckFavListener;
import com.naosteam.watchvideoapp.listeners.CheckRatingAsyncListener;
import com.naosteam.watchvideoapp.listeners.CheckRatingListener;
import com.naosteam.watchvideoapp.listeners.ExecuteQueryAsyncListener;
import com.naosteam.watchvideoapp.listeners.SetFavListener;
import com.naosteam.watchvideoapp.listeners.SetRatingAsyncListener;
import com.naosteam.watchvideoapp.listeners.SetRatingListener;
import com.naosteam.watchvideoapp.models.Category_M;
import com.naosteam.watchvideoapp.models.Comment_M;
import com.naosteam.watchvideoapp.models.Users_M;
import com.naosteam.watchvideoapp.models.Videos_M;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
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
            RequestBody requestBody = getVideoRequestBody("CHECK_IS_FAV", bundle);

            CheckFavAsync async = new CheckFavAsync(requestBody, new CheckFavAsyncListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onEnd(boolean status, boolean isFav) {
                    if(context != null){
                        if(isNetworkConnected(context)){
                            listener.onComplete(status, isFav);
                        }else{
                            listener.onComplete(false, false);
                            Toast.makeText(context, "Please check internet connection!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }, this);

            async.execute();
        }else{
            listener.onComplete(false, false);
        }
    }

    public void checkRating(Context context, int vid_id, CheckRatingListener listener) {
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            Bundle bundle = new Bundle();
            bundle.putInt("vid_id", vid_id);
            bundle.putString("uid", uid);
            RequestBody requestBody = getVideoRequestBody("CHECK_RATING", bundle);

            CheckRatingAsync async = new CheckRatingAsync(requestBody, new CheckRatingAsyncListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onEnd(boolean status, double rate) {
                    if(context != null){
                        if(isNetworkConnected(context)){
                            listener.onComplete(status, rate);
                        }else{
                            listener.onComplete(false, 0);
                            Toast.makeText(context, "Please check internet connection!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }, this);

            async.execute();
        }else{
            listener.onComplete(false, 0);
        }
    }

    public void setFavState(Context context, int vid_id, boolean isFav, SetFavListener listener){
        if(FirebaseAuth.getInstance().getCurrentUser() != null){

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            Bundle bundle = new Bundle();
            bundle.putString("uid", uid);
            bundle.putInt("vid_id", vid_id);
            bundle.putInt("is_fav", isFav?1:0);

            RequestBody requestBody = getVideoRequestBody("SET_FAV_VIDEO", bundle);

            ExecuteQueryAsync async = new ExecuteQueryAsync(requestBody, new ExecuteQueryAsyncListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onEnd(boolean status) {
                    if(context != null){
                        if(isNetworkConnected(context)){
//                            if(!status){
//                                Toast.makeText(context, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
//                            }
                            listener.onComplete(status);

                        }else{
                            listener.onComplete(false);
                            Toast.makeText(context, "Please check internet connection!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            async.execute();

        }else{
            Toast.makeText(context, "Please login first!", Toast.LENGTH_SHORT).show();
            listener.onComplete(false);
        }
    }

    public void setRating(Context context, int vid_id, double rate, SetRatingListener listener){
        if(FirebaseAuth.getInstance().getCurrentUser() != null){

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            Bundle bundle = new Bundle();
            bundle.putString("uid", uid);
            bundle.putInt("vid_id", vid_id);
            bundle.putInt("rate", (int)rate);

            RequestBody requestBody = getVideoRequestBody("SET_RATING", bundle);

            SetRatingAsync async = new SetRatingAsync(requestBody, new SetRatingAsyncListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onEnd(boolean status, float returnRate) {
                    if(context != null){
                        if(isNetworkConnected(context)){
//                            if(!status){
//                                Toast.makeText(context, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show();
//                            }
                            listener.onComplete(status, returnRate);
                        }else{
                            listener.onComplete(false, returnRate);
                            Toast.makeText(context, "Please check internet connection!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            });
            async.execute();

        }else{
            Toast.makeText(context, "Please login first!", Toast.LENGTH_SHORT).show();
            listener.onComplete(false, 0);
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

    public Comment_M getCommentVideo(JSONObject obj) throws Exception{
        int cmt_id = obj.getInt("cmt_id");
        int vid_id =  obj.getInt("vid_id");
        String uid = checkForEncode(obj.getString("uid"))
                ? base64Decode(obj.getString("uid"))
                : obj.getString("uid") ;
        String cmt_time = checkForEncode(obj.getString("cmt_time"))
                ? base64Decode(obj.getString("cmt_time"))
                : obj.getString("cmt_time");
        String cmt_text = checkForEncode(obj.getString("cmt_text"))
                ? base64Decode(obj.getString("cmt_text")) : obj.getString("cmt_text");

        return new Comment_M(cmt_id, vid_id, uid, cmt_time, cmt_text);
    }

    public Videos_M getDailymotionVideo(JSONObject obj) throws Exception {
        String vid_title = obj.getString("title");
        String vid_url = obj.getString("id");
        String vid_thumbnail = obj.getString("thumbnail_url");
        int vid_view = obj.getInt("views_total");
        int duration = obj.getInt("duration");

        return new Videos_M(-1, -1, vid_title, vid_thumbnail, "", vid_url, vid_view, duration, -1, Constant.DAILYMOTION_VIDEO, false, new Date());
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
        String photo_url = obj.getString("photo_url");
        int user_age = obj.getInt("user_age");
        boolean user_status = obj.getInt("user_status")==1;
        return new Users_M(uid, user_name, user_email, user_phone, user_age, photo_url);
    }

    public RequestBody getHomeRequestBody(String method_name, Bundle bundle) {
        JsonObject postObj = new JsonObject();
        postObj.addProperty("method_name", method_name);

        switch (method_name){
            case "CHECK_IS_FAV":
                postObj.addProperty("uid", base64Encode(bundle.getString("uid")));
                postObj.addProperty("vid_id", bundle.getInt("vid_id"));

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

            case "LOAD_SEARCH_CATEGORY":
                postObj.addProperty("cat_id", bundle.getInt("cat_id"));
                postObj.addProperty("page", bundle.getInt("page"));
                postObj.addProperty("step", bundle.getInt("step"));
                break;
            case "CHECK_IS_FAV":
            case "CHECK_RATING":
                postObj.addProperty("uid", base64Encode(bundle.getString("uid")));
                postObj.addProperty("vid_id", bundle.getInt("vid_id"));
                break;

            case "SET_FAV_VIDEO":

                postObj.addProperty("uid", base64Encode(bundle.getString("uid")));
                postObj.addProperty("vid_id", bundle.getInt("vid_id"));
                postObj.addProperty("is_fav", bundle.getInt("is_fav"));

                break;

            case "GET_FAV_DATA":
                postObj.addProperty("vid_type", bundle.getInt("vid_type"));
                postObj.addProperty("uid", base64Encode(bundle.getString("uid")));
                break;

            case "SET_RATING":

                postObj.addProperty("uid", base64Encode(bundle.getString("uid")));
                postObj.addProperty("vid_id", bundle.getInt("vid_id"));
                postObj.addProperty("rate", bundle.getInt("rate"));

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
                break;
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

    public RequestBody GetTVRequestBody(String method_name, Bundle bundle) {
        JsonObject postObj = new JsonObject();
        postObj.addProperty("method_name", method_name);

        switch (method_name){
            case "LOAD_TV_SCREEN":
                break;
        }

        String post_data = postObj.toString();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("data", post_data);

        return builder.build();

    }

    public RequestBody GetCmtRequestBody(String method_name, Bundle bundle){
        JsonObject postObj = new JsonObject();
        postObj.addProperty("method_name", method_name);

        JsonObject cmt_obj = new JsonObject();
        switch (method_name){
            case "INSERT_CMT":
                cmt_obj.addProperty("uid", base64Encode(bundle.getString("uid")));
                cmt_obj.addProperty("cmt_time", bundle.getString("cmt_time"));
                cmt_obj.addProperty("cmt_text", base64Encode(bundle.getString("cmt_text")));
                cmt_obj.addProperty("vid_id", bundle.getInt("vid_id"));
                break;
            case "GET_CMT_DATA":
                postObj.addProperty("vid_id", bundle.getInt("vid_id"));
                break;
            case "UPDATE_CMT":
                postObj.addProperty("cmt_text", base64Encode(bundle.getString("cmt_text")));
            case "DEL_CMT":
                postObj.addProperty("cmt_id", bundle.getInt("cmt_id"));
                break;
        }

        String post_data = postObj.toString();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        String cmt_data = cmt_obj.toString();
        builder.addFormDataPart("cmt", cmt_data);

        builder.addFormDataPart("data", post_data);

        return builder.build();
    }

    public String getPastTimeString(Date postDate) {

        Date currentDate = new Date();
        long diffInTime = currentDate.getTime() - postDate.getTime();
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInTime);
        long diffInHour = TimeUnit.MILLISECONDS.toHours(diffInTime);
        long diffInYear = TimeUnit.MILLISECONDS.toDays(diffInTime) / 365l;
        long diffInMonth = TimeUnit.MILLISECONDS.toDays(diffInTime) / 30l;
        long diffInDay = TimeUnit.MILLISECONDS.toDays(diffInTime);

        if (diffInYear < 1) {
            if (diffInMonth < 1) {
                if (diffInDay < 1) {
                    if (diffInHour < 1) {
                        if (diffInMinutes < 1) {
                            return "Just now";
                        } else {
                            return diffInMinutes + " minutes ago";
                        }
                    } else {
                        return diffInHour + " hours ago";
                    }
                } else {
                    return diffInDay + " days ago";
                }
            } else {
                return diffInMonth + " months ago";
            }
        } else {
            return diffInYear + " years ago";
        }
    }

    public String getDurationString(int sec) {
        int hours = sec / 3600;
        int minutes = (sec % 3600) / 60;
        int seconds = sec % 60;

        String timeString = "";

        if(hours >= 1){
            timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }else{
            timeString = String.format("%02d:%02d", minutes, seconds);
        }

        return timeString;

    }
}
