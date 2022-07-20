package com.naosteam.funnyvideo.asynctasks;

import android.os.AsyncTask;

import com.naosteam.funnyvideo.listeners.LoadRadioAsyncListener;
import com.naosteam.funnyvideo.models.Category_M;
import com.naosteam.funnyvideo.models.Videos_M;
import com.naosteam.funnyvideo.utils.Constant;
import com.naosteam.funnyvideo.utils.JsonUtils;
import com.naosteam.funnyvideo.utils.Methods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class LoadRadioAsync extends AsyncTask<Void, String, Boolean> {
    private RequestBody requestBody;
    private LoadRadioAsyncListener listener;
    private Methods methods;
    private ArrayList<Videos_M> arrayList_trending;
    private ArrayList<Category_M> arrayList_cats;

    public LoadRadioAsync(RequestBody requestBody, LoadRadioAsyncListener listener, Methods methods) {
        this.requestBody = requestBody;
        this.listener = listener;
        this.methods = methods;
        arrayList_cats = new ArrayList<>();
        arrayList_trending = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        listener.onStart();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            String api_url = Constant.SERVER_URL;
            String result = JsonUtils.okhttpPost(api_url, requestBody);
            JSONObject jsonObject = new JSONObject(result);
            boolean status = jsonObject.getString("status").equals("success");

            if (status) {
                JSONArray jsonArray_trending = jsonObject.getJSONArray("trending");
                JSONArray jsonArray_category = jsonObject.getJSONArray("category");

                for (int i = 0; i < jsonArray_trending.length(); i++){
                    JSONObject obj = jsonArray_trending.getJSONObject(i);
                    arrayList_trending.add(methods.getRowVideo(obj));
                }

                for (int i = 0; i < jsonArray_category.length(); i++){
                    JSONObject obj = jsonArray_category.getJSONObject(i);
                    arrayList_cats.add(methods.getRowCategory(obj));
                }

                return true;
            }
            else{
                return false;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        listener.onEnd(aBoolean, arrayList_trending, arrayList_cats);
        super.onPostExecute(aBoolean);
    }
}
