package com.naosteam.funnyvideo.asynctasks;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.naosteam.funnyvideo.listeners.LoadYoutubeSearchAsyncListener;
import com.naosteam.funnyvideo.models.Videos_M;
import com.naosteam.funnyvideo.utils.Constant;
import com.naosteam.funnyvideo.utils.JsonUtils;
import com.naosteam.funnyvideo.utils.Methods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadYoutubeVideoAsync extends AsyncTask<Void, String, Boolean> {

    private LoadYoutubeSearchAsyncListener listener;
    private ArrayList<Videos_M> mVideos;
    private Bundle bundle;
    private String newPageToken = "";

    public LoadYoutubeVideoAsync(Bundle bundle, LoadYoutubeSearchAsyncListener listener) {
        this.listener = listener;
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

            String api_url = "";
            String nextPageToken = bundle.getString("page");

            if(nextPageToken.equals("")){
                api_url = "https://www.googleapis.com/youtube/v3/search?part=snippet" +
                        "&key=" + Constant.YOUTUBE_API_KEY +
                        "&order=relevance" +
                        "&type=video"+
                        "&maxResults=" + bundle.getInt("step") +
                        "&q=" + bundle.getString("search_text");
            }else{
                api_url = "https://www.googleapis.com/youtube/v3/search?part=snippet" +
                        "&key=" + Constant.YOUTUBE_API_KEY +
                        "&order=date" +
                        "&type=video"+
                        "&maxResults=" + bundle.getInt("step") +
                        "&&pageToken=" + nextPageToken +
                        "&q=" + bundle.getString("search_text");
            }

            String result = JsonUtils.okhttpGET(api_url);
            JSONObject jsonObject = new JSONObject(result);

            newPageToken = jsonObject.getString("nextPageToken");
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            String mIds = "";

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject obj = jsonArray.getJSONObject(i);

                Videos_M videos_m = Methods.getInstance().getJsonYoutubeVideo(obj);

                if(i == jsonArray.length()){
                    mIds += videos_m.getVid_url();
                }else{
                    mIds += videos_m.getVid_url() + ",";
                }

                mVideos.add(videos_m);
            }

            String api_get_statistics = "https://www.googleapis.com/youtube/v3/videos?part=statistics" +
                    "&id=" + mIds +
                    "&key=" + Constant.YOUTUBE_API_KEY;

            String result_statistics = JsonUtils.okhttpGET(api_get_statistics);
            JSONObject jsonObj_statistics = new JSONObject(result_statistics);
            JSONArray jsonArray_statistics = jsonObj_statistics.getJSONArray("items");

            for (int i = 0; i < jsonArray_statistics.length(); i++){
                JSONObject obj = jsonArray_statistics.getJSONObject(i);

                JSONObject statistics = new JSONObject(obj.getString("statistics"));

                long viewCount = statistics.getLong("viewCount");

                mVideos.get(i).setVid_view((int) viewCount);
            }

            return true;
        }catch (Exception e){
            Log.e(Constant.ERR_TAG, e.getMessage());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        listener.onEnd(aBoolean, newPageToken, mVideos);
        super.onPostExecute(aBoolean);
    }
}
