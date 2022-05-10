package com.naosteam.watchvideoapp.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.naosteam.watchvideoapp.listeners.LoadTVAsyncListener;
import com.naosteam.watchvideoapp.models.Category_M;
import com.naosteam.watchvideoapp.models.Videos_M;
import com.naosteam.watchvideoapp.utils.Constant;
import com.naosteam.watchvideoapp.utils.JsonUtils;
import com.naosteam.watchvideoapp.utils.Methods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class LoadTVAsync extends AsyncTask<Void, String, Boolean> {

    private RequestBody requestBody;
    private LoadTVAsyncListener listener;
    private Methods methods;
    private ArrayList<Videos_M> list_tv_all;
    private ArrayList<Videos_M> list_tv_trending;
    private ArrayList<Category_M> list_category_tv;

    public LoadTVAsync(RequestBody requestBody, LoadTVAsyncListener listener) {
        this.requestBody = requestBody;
        this.listener = listener;
        methods= Methods.getInstance();
        list_tv_all = new ArrayList<>();
        list_tv_trending = new ArrayList<>();
        list_category_tv = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        listener.onPre();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        try{
            String api_url = Constant.SERVER_URL;
            String result = JsonUtils.okhttpPost(api_url, requestBody);
            JSONObject jsonObject = new JSONObject(result);
            boolean status = jsonObject.getString("status").equals("success");

            if(status){
                JSONArray jsonArray_all = jsonObject.getJSONArray("all");
                JSONArray jsonArray_trending = jsonObject.getJSONArray("trending");
                JSONArray jsonArray_category = jsonObject.getJSONArray("category");

                for (int i = 0; i < jsonArray_all.length(); i++){
                    JSONObject obj = jsonArray_all.getJSONObject(i);
                    list_tv_all.add(methods.getRowVideo(obj));
                }

                for (int i = 0; i < jsonArray_trending.length(); i++){
                    JSONObject obj = jsonArray_trending.getJSONObject(i);
                    list_tv_trending.add(methods.getRowVideo(obj));
                }

                for (int i = 0; i < jsonArray_category.length(); i++){
                    JSONObject obj = jsonArray_category.getJSONObject(i);
                    list_category_tv.add(methods.getRowCategory(obj));
                }
                return true;
            }else {
                Log.e(Constant.ERR_TAG, jsonObject.getString("message"));
                return false;
            }

        }catch (Exception e){
            Log.e(Constant.ERR_TAG, e.getMessage());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        listener.onEnd(aBoolean, list_tv_all, list_tv_trending, list_category_tv);
        super.onPostExecute(aBoolean);
    }
}