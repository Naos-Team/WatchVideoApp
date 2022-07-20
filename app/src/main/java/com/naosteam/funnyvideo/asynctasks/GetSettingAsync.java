package com.naosteam.funnyvideo.asynctasks;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.naosteam.funnyvideo.listeners.ExecuteQueryAsyncListener;
import com.naosteam.funnyvideo.utils.Constant;
import com.naosteam.funnyvideo.utils.JsonUtils;

import org.json.JSONObject;

import okhttp3.RequestBody;

public class GetSettingAsync extends AsyncTask<Void, String, Boolean> {
    private RequestBody requestBody;
    private ExecuteQueryAsyncListener listener;

    public GetSettingAsync(RequestBody requestBody, ExecuteQueryAsyncListener listener) {
        this.requestBody = requestBody;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        listener.onStart();
        super.onPreExecute();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected Boolean doInBackground(Void... voids) {
        try{
            String api_url = Constant.SERVER_URL;
            String result = JsonUtils.okhttpPost(api_url, requestBody);
            JSONObject jsonObject = new JSONObject(result);
            boolean status = jsonObject.getString("status").equals("success");

            JSONObject setting_obj = new JSONObject(jsonObject.getString("setting_array"));

            if(status){

                Constant.ADS_KEY_BANNER = Constant.DECODE_BASE64(setting_obj.optString("ads_key_banner"));
                Constant.ADS_KEY_INTERSTIAL = Constant.DECODE_BASE64(setting_obj.optString("ads_key_interstial"));
                Constant.ADS_DISPLAY_COUNT = setting_obj.optInt("ad_display_count");
                Constant.ADS_KEY_OPENADS = Constant.DECODE_BASE64(setting_obj.optString("ads_key_openads"));
                Constant.ARR_VID_TREND = setting_obj.optString("arr_Vid_trend");
                Constant.ARR_TV_TREND = setting_obj.optString("arr_TV_trend");
                Constant.ARR_RADIO_TREND = setting_obj.optString("arr_Radi_trend");
                Constant.ONESIGNAL_APP_ID = Constant.DECODE_BASE64(setting_obj.optString("onesignal_app_id"));



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
        listener.onEnd(aBoolean);
        super.onPostExecute(aBoolean);
    }


}
