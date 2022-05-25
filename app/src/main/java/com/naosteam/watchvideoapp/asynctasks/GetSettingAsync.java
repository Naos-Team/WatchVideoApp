package com.naosteam.watchvideoapp.asynctasks;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.naosteam.watchvideoapp.listeners.ExecuteQueryAsyncListener;
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

            if(status){
                JSONArray jsonArray_setting = jsonObject.getJSONArray("setting_array");

                for (int i = 0; i < jsonArray_setting.length(); i++){
                    JSONObject obj = jsonArray_setting.getJSONObject(i);
                    Constant.ADS_KEY_BANNER = Constant.DECODE_BASE64(obj.optString("ads_key_banner"));
                    Constant.ADS_KEY_INTERSTIAL = Constant.DECODE_BASE64(obj.optString("ads_key_interstial"));
                    Constant.ADS_DISPLAY_COUNT = obj.optInt("ad_display_count");
                    Constant.ADS_KEY_OPENADS = Constant.DECODE_BASE64(obj.optString("ads_key_openads"));
                    Constant.ARR_VID_TREND = Constant.DECODE_BASE64(obj.optString("arr_Vid_trend"));
                    Constant.ARR_TV_TREND = Constant.DECODE_BASE64(obj.optString("arr_TV_trend"));
                    Constant.ARR_RADIO_TREND = Constant.DECODE_BASE64(obj.optString("arr_Radi_trend"));

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
        listener.onEnd(aBoolean);
        super.onPostExecute(aBoolean);
    }


}
