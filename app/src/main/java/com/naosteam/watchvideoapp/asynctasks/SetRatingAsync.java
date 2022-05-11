package com.naosteam.watchvideoapp.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.naosteam.watchvideoapp.listeners.CheckRatingAsyncListener;
import com.naosteam.watchvideoapp.listeners.SetRatingAsyncListener;
import com.naosteam.watchvideoapp.utils.Constant;
import com.naosteam.watchvideoapp.utils.JsonUtils;

import org.json.JSONObject;

import okhttp3.RequestBody;

public class SetRatingAsync extends AsyncTask<Void, String, Boolean> {

    private RequestBody requestBody;
    private SetRatingAsyncListener listener;
    private float returnRate = 0;

    public SetRatingAsync(RequestBody requestBody, SetRatingAsyncListener listener) {
        this.requestBody = requestBody;
        this.listener = listener;
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
            JSONObject returnObj = new JSONObject(result);
            boolean status = returnObj.getString("status").equals("success");

            if(returnObj.getString("status").equals("success")){

                returnRate = (float) returnObj.getDouble("rate");

                return true;
            }else {
                Log.e(Constant.ERR_TAG, returnObj.getString("message"));
                return false;
            }

        }catch (Exception e){
            Log.e(Constant.ERR_TAG, e.getMessage());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        listener.onEnd(aBoolean, returnRate);
        super.onPostExecute(aBoolean);
    }
}
