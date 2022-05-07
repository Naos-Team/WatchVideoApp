package com.naosteam.watchvideoapp.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.naosteam.watchvideoapp.listeners.LoadVideoAsyncListener;
import com.naosteam.watchvideoapp.models.Category_M;
import com.naosteam.watchvideoapp.models.Videos_M;
import com.naosteam.watchvideoapp.utils.Constant;
import com.naosteam.watchvideoapp.utils.JsonUtils;
import com.naosteam.watchvideoapp.utils.Methods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class LoadVideoAsync extends AsyncTask<Void, String, Boolean> {

    private RequestBody requestBody;
    private LoadVideoAsyncListener listener;
    private Methods methods;
    private ArrayList<Videos_M> arrayList_mostview;
    private ArrayList<Videos_M> arrayList_latest;
    private ArrayList<Videos_M> arrayList_trending;
    private ArrayList<Videos_M> arrayList_toprate;
    private ArrayList<Category_M> arrayList_category;

    public LoadVideoAsync(RequestBody requestBody, LoadVideoAsyncListener listener, Methods methods) {
        this.requestBody = requestBody;
        this.listener = listener;
        this.methods = methods;
        arrayList_latest = new ArrayList<>();
        arrayList_mostview = new ArrayList<>();
        arrayList_trending = new ArrayList<>();
        arrayList_toprate = new ArrayList<>();
        arrayList_category = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        listener.onStart();
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
                JSONArray jsonArray_mostview = jsonObject.getJSONArray("mostview");
                JSONArray jsonArray_trending = jsonObject.getJSONArray("trending");
                JSONArray jsonArray_latest = jsonObject.getJSONArray("latest");
                JSONArray jsonArray_toprate = jsonObject.getJSONArray("toprate");
                JSONArray jsonArray_category = jsonObject.getJSONArray("category");

                for (int i = 0; i < jsonArray_mostview.length(); i++){
                    JSONObject obj = jsonArray_mostview.getJSONObject(i);
                    arrayList_mostview.add(methods.getRowVideo(obj));
                }

                for (int i = 0; i < jsonArray_trending.length(); i++){
                    JSONObject obj = jsonArray_trending.getJSONObject(i);
                    arrayList_trending.add(methods.getRowVideo(obj));
                }

                for (int i = 0; i < jsonArray_latest.length(); i++){
                    JSONObject obj = jsonArray_latest.getJSONObject(i);
                    arrayList_latest.add(methods.getRowVideo(obj));
                }

                for (int i = 0; i < jsonArray_toprate.length(); i++){
                    JSONObject obj = jsonArray_toprate.getJSONObject(i);
                    arrayList_toprate.add(methods.getRowVideo(obj));
                }

                for (int i = 0; i < jsonArray_category.length(); i++){
                    JSONObject obj = jsonArray_category.getJSONObject(i);
                    arrayList_category.add(methods.getRowCategory(obj));
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
        listener.onEnd(aBoolean, arrayList_trending, arrayList_mostview, arrayList_latest, arrayList_toprate, arrayList_category);
        super.onPostExecute(aBoolean);
    }
}
