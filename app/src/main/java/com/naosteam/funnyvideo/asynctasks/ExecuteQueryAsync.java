package com.naosteam.funnyvideo.asynctasks;

import android.os.AsyncTask;

import com.naosteam.funnyvideo.listeners.ExecuteQueryAsyncListener;
import com.naosteam.funnyvideo.utils.Constant;
import com.naosteam.funnyvideo.utils.JsonUtils;

import okhttp3.RequestBody;

public class ExecuteQueryAsync extends AsyncTask<Void, String, Boolean> {

    private RequestBody requestBody;
    private ExecuteQueryAsyncListener listener;

    public ExecuteQueryAsync(RequestBody requestBody, ExecuteQueryAsyncListener listener) {
        this.requestBody = requestBody;
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try{
            String api_url = Constant.SERVER_URL;

            String result = JsonUtils.okhttpPost(api_url, requestBody);

            if(result.equals("success")){
                return true;
            }else {
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean status) {
        listener.onEnd(status);
        super.onPostExecute(status);
    }

    @Override
    protected void onPreExecute() {
        listener.onStart();
        super.onPreExecute();
    }
}
