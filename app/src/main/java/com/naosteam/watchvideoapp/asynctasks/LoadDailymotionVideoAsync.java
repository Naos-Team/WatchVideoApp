package com.naosteam.watchvideoapp.asynctasks;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.naosteam.watchvideoapp.listeners.LoadSearchVideoAsyncListener;
import com.naosteam.watchvideoapp.models.Videos_M;
import com.naosteam.watchvideoapp.utils.Constant;
import com.naosteam.watchvideoapp.utils.JsonUtils;
import com.naosteam.watchvideoapp.utils.Methods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadDailymotionVideoAsync extends AsyncTask<Void, String, Boolean> {

    private LoadSearchVideoAsyncListener listener;
    private Methods methods;
    private ArrayList<Videos_M> mVideos;
    private Bundle bundle;

    public LoadDailymotionVideoAsync(Bundle bundle, Methods methods, LoadSearchVideoAsyncListener listener) {
        this.listener = listener;
        this.methods = methods;
        this.bundle = bundle;
        mVideos = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        listener.onStart();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try{

            String api_url = "https://api.dailymotion.com/videos?" +
                    "search="+bundle.getString("search_text")+
                    "&page="+bundle.getInt("page")+1+
                    "&limit="+bundle.getInt("step")+
                    "&language=en"+
                    "&fields=id,title,views_total,thumbnail_url,duration";

            String result = JsonUtils.okhttpGET(api_url);
            JSONObject jsonObject = new JSONObject(result);

            JSONArray jsonArray = jsonObject.getJSONArray("list");

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                mVideos.add(methods.getJsonDailymotionVideo(obj));
            }

            return true;
        }catch (Exception e){
            Log.e(Constant.ERR_TAG, e.getMessage());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        listener.onEnd(aBoolean, mVideos);
        super.onPostExecute(aBoolean);
    }
}
