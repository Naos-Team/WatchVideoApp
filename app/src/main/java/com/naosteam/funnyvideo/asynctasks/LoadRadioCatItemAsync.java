package com.naosteam.funnyvideo.asynctasks;

import android.os.AsyncTask;

import com.naosteam.funnyvideo.listeners.LoadRadioCatItemAsyncListener;
import com.naosteam.funnyvideo.models.Videos_M;
import com.naosteam.funnyvideo.utils.Constant;
import com.naosteam.funnyvideo.utils.JsonUtils;
import com.naosteam.funnyvideo.utils.Methods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class LoadRadioCatItemAsync extends AsyncTask<Void, String, Boolean> {
    private RequestBody requestBody;
    private LoadRadioCatItemAsyncListener listener;
    private Methods methods;
    private ArrayList<Videos_M> arrayList_radios;


    public LoadRadioCatItemAsync(RequestBody requestBody, LoadRadioCatItemAsyncListener listener, Methods methods) {
        this.requestBody = requestBody;
        this.listener = listener;
        this.methods = methods;
        arrayList_radios = new ArrayList<>();
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
                JSONArray jsonArray_radios = jsonObject.getJSONArray("cat_radios");

                for (int i = 0; i < jsonArray_radios.length(); i++){
                    JSONObject obj = jsonArray_radios.getJSONObject(i);
                    arrayList_radios.add(methods.getRowVideo(obj));
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
        listener.onEnd(aBoolean, arrayList_radios);
        super.onPostExecute(aBoolean);
    }
}
