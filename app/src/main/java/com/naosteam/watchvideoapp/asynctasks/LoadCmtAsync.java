package com.naosteam.watchvideoapp.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.naosteam.watchvideoapp.listeners.LoadCmtListener;
import com.naosteam.watchvideoapp.models.Comment_M;
import com.naosteam.watchvideoapp.utils.Constant;
import com.naosteam.watchvideoapp.utils.JsonUtils;
import com.naosteam.watchvideoapp.utils.Methods;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class LoadCmtAsync extends AsyncTask<Void, Void, Boolean> {
    private RequestBody requestBody;
    private ArrayList<Comment_M> list_cmt;
    private LoadCmtListener listener;
    private Methods methods;

    public LoadCmtAsync(RequestBody requestBody, LoadCmtListener listener) {
        this.requestBody = requestBody;
        this.list_cmt = new ArrayList<>();
        this.listener = listener;
        this.methods = Methods.getInstance();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onPre();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try{
            String api_url = Constant.SERVER_URL;
            String result = JsonUtils.okhttpPost(api_url, requestBody);
            JSONObject jsonObject = new JSONObject(result);
            boolean status = jsonObject.getString("status").equals("success");

            if(status){
                JSONArray jsonArray_all = jsonObject.getJSONArray("list");

                for (int i = 0; i < jsonArray_all.length(); i++){
                    JSONObject obj = jsonArray_all.getJSONObject(i);
                    list_cmt.add(methods.getCommentVideo(obj));
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
        super.onPostExecute(aBoolean);
        listener.onEnd(aBoolean, list_cmt);
    }
}
