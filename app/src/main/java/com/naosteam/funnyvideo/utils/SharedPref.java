package com.naosteam.funnyvideo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.naosteam.funnyvideo.models.Category_M;
import com.naosteam.funnyvideo.models.Videos_M;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

public class SharedPref {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static String TEMP_VIDEO_LIST = "TEMP_VIDEO_LIST";
    private static String TEMP_RADIO_LIST = "TEMP_RADIO_LIST";
    private static String TEMP_TV_LIST = "TEMP_TV_LIST";
    private static String TEMP_CATE_LIST = "TEMP_CATE_LIST";
    private static String OPEN_ADS_ID = "OPEN_ADS_ID";
    private Context context;

    private static SharedPref Instance;

    private SharedPref(Context context){
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public static SharedPref getInstance(Context context){
        if(Instance == null){
            Instance = new SharedPref(context);
        }
        return Instance;
    }

    public ArrayList<Videos_M> getTempVideoList(int type){

        switch (type){
            case Constant.VIDEO:

                String json_video = sharedPreferences.getString(TEMP_VIDEO_LIST, "");
                try {
                    if(!json_video.equals("")){
                        Type video_type = new TypeToken<ArrayList<Videos_M>>(){}.getType();
                        ArrayList<Videos_M> arrayList_temp = new Gson().fromJson(json_video, video_type);
                        return arrayList_temp;
                    }else{
                        return getDefaultVideos();
                    }
                }catch (Exception e) {
                    return getDefaultVideos();
                }

            case Constant.TV:
                String json_tv = sharedPreferences.getString(TEMP_TV_LIST, "");
                try {
                    if(!json_tv.equals("")){
                        Type video_type = new TypeToken<ArrayList<Videos_M>>(){}.getType();
                        ArrayList<Videos_M> arrayList_temp = new Gson().fromJson(json_tv, video_type);
                        return arrayList_temp;
                    }else{
                        return getDefaultTVs();
                    }
                }catch (Exception e) {
                    return getDefaultTVs();
                }
            case Constant.RADIO:
                String json_radio = sharedPreferences.getString(TEMP_RADIO_LIST, "");
                try {
                    if(!json_radio.equals("")){
                        Type video_type = new TypeToken<ArrayList<Videos_M>>(){}.getType();
                        ArrayList<Videos_M> arrayList_temp = new Gson().fromJson(json_radio, video_type);
                        return arrayList_temp;
                    }else{
                        return getDefaultRadios();
                    }
                }catch (Exception e) {
                    return getDefaultRadios();
                }

            default:
                return getDefaultRadios();
        }

    }

    public ArrayList<Category_M> getTempCategoryList(int type){

        switch (type){
            case Constant.VIDEO:

                return getDefaultVideoCategory();

            case Constant.TV:
                return getDefaultTVCategory();

            default:
                return getDefaultRadioCategory();
        }

    }

    public void setOpenAdsKey(String key){
        editor.putString(OPEN_ADS_ID, key);
        editor.commit();
    }

    public String getOpenAdsKey(){
        return sharedPreferences.getString(OPEN_ADS_ID, "");
    }

    public void setTempVideoList(int type, ArrayList<Videos_M> arrayList){

        switch (type){
            case Constant.VIDEO:

                try{
                    String json_video = new Gson().toJson(arrayList);
                    editor.putString(TEMP_VIDEO_LIST, json_video);
                }catch (Exception e){
                    editor.putString(TEMP_VIDEO_LIST, "");
                }

                break;
            case Constant.TV:

                try{
                    String json_tv = new Gson().toJson(arrayList);
                    editor.putString(TEMP_TV_LIST, json_tv);
                }catch (Exception e){
                    editor.putString(TEMP_TV_LIST, "");
                }

                break;
            case Constant.RADIO:

                try{
                    String json_radio = new Gson().toJson(arrayList);
                    editor.putString(TEMP_RADIO_LIST, json_radio);
                }catch (Exception e){
                    editor.putString(TEMP_RADIO_LIST, "");
                }

                break;
        }

        editor.commit();
    }

    private ArrayList<Videos_M> getDefaultVideos(){
        ArrayList<Videos_M> array_first = new ArrayList<>();
        array_first.add(new Videos_M(1, 1, "Spider Man No Way Home",
                "empty",
                "description", "temp", 23000, 0, 0,Constant.VIDEO, false, new Date()));
        array_first.add(new Videos_M(2, 1, "Moon Knight",
                "empty",
                "description", "temp", 23000, 0, 0,Constant.VIDEO, false, new Date()));
        array_first.add(new Videos_M(3, 1, "Iron Man 3",
                "empty",
                "description", "temp", 23000, 0, 0,Constant.VIDEO, false, new Date()));
        array_first.add(new Videos_M(4, 1, "The Avengers: ENDGAME",
                "empty",
                "description", "temp", 23000, 0, 0,Constant.VIDEO, false, new Date()));
        array_first.add(new Videos_M(5, 1, "Doctor Strange 2 Multiverse of Madness",
                "empty",
                "description", "temp", 23000, 0, 0,Constant.VIDEO, false, new Date()));
        array_first.add(new Videos_M(6, 1, "Venom",
                "empty",
                "description", "temp", 23000, 0, 0,Constant.VIDEO, false, new Date()));
        return array_first;
    }

    private ArrayList<Videos_M> getDefaultTVs(){
        ArrayList<Videos_M> array_first = new ArrayList<>();
        array_first.add(new Videos_M(1, 1, "VTV1",
                "empty",
                "description", "temp", 23000, 0, 0,Constant.VIDEO, false, new Date()));
        array_first.add(new Videos_M(2, 1, "VTV2",
                "empty",
                "description", "temp", 23000, 0, 0,Constant.VIDEO, false, new Date()));
        array_first.add(new Videos_M(3, 1, "VTV3",
                "empty",
                "description", "temp", 23000, 0, 0,Constant.VIDEO, false, new Date()));
        array_first.add(new Videos_M(4, 1, "Discovery",
                "empty",
                "description", "temp", 23000, 0, 0,Constant.VIDEO, false, new Date()));
        array_first.add(new Videos_M(5, 1, "Disney Channel",
                "empty",
                "description", "temp", 23000, 0, 0,Constant.VIDEO, false, new Date()));
        array_first.add(new Videos_M(6, 1, "K+",
                "empty",
                "description", "temp", 23000, 0, 0,Constant.VIDEO, false, new Date()));
        return array_first;
    }

    private ArrayList<Videos_M> getDefaultRadios(){
        ArrayList<Videos_M> array_first = new ArrayList<>();
        array_first.add(new Videos_M(1, 1, "Country Hít - HitsRadui",
                "empty",
                "description", "temp", 23000, 0, 0,Constant.VIDEO, false, new Date()));
        array_first.add(new Videos_M(2, 1, "181.fm - Kickin'Country",
                "empty",
                "description", "temp", 23000, 0, 0,Constant.VIDEO, false, new Date()));
        array_first.add(new Videos_M(3, 1, "HPR2 Today's Classic Country",
                "empty",
                "description", "temp", 23000, 0, 0,Constant.VIDEO, false, new Date()));
        array_first.add(new Videos_M(4, 1, "KUTX 98.9 FM",
                "empty",
                "description", "temp", 23000, 0, 0,Constant.VIDEO, false, new Date()));
        array_first.add(new Videos_M(5, 1, "HPR1 Traditional Classic Country",
                "empty",
                "description", "temp", 23000, 0, 0,Constant.VIDEO, false, new Date()));
        array_first.add(new Videos_M(6, 1, "Dixie Radio Stockholm",
                "empty",
                "description", "temp", 23000, 0, 0,Constant.VIDEO, false, new Date()));
        return array_first;
    }

    private ArrayList<Category_M> getDefaultVideoCategory(){
        ArrayList<Category_M> arrayList_first = new ArrayList<>();
        arrayList_first.add(new Category_M(1, "Funny", "empty", Constant.VIDEO));
        arrayList_first.add(new Category_M(2, "Movies", "empty", Constant.VIDEO));
        arrayList_first.add(new Category_M(3, "Cartoon", "empty", Constant.VIDEO));
        arrayList_first.add(new Category_M(4, "News", "empty", Constant.VIDEO));
        arrayList_first.add(new Category_M(5, "Sport", "empty", Constant.VIDEO));

        return arrayList_first;
    }

    private ArrayList<Category_M> getDefaultTVCategory(){
        ArrayList<Category_M> arrayList_first = new ArrayList<>();
        arrayList_first.add(new Category_M(1, "Funny", "empty", Constant.TV));
        arrayList_first.add(new Category_M(2, "Movies", "empty", Constant.TV));
        arrayList_first.add(new Category_M(3, "Cartoon", "empty", Constant.TV));
        arrayList_first.add(new Category_M(4, "News", "empty", Constant.TV));
        arrayList_first.add(new Category_M(5, "Sport", "empty", Constant.TV));

        return arrayList_first;
    }

    private ArrayList<Category_M> getDefaultRadioCategory(){
        ArrayList<Category_M> arrayList_first = new ArrayList<>();
        arrayList_first.add(new Category_M(1, "Funny", "empty", Constant.RADIO));
        arrayList_first.add(new Category_M(2, "Movies", "empty", Constant.RADIO));
        arrayList_first.add(new Category_M(3, "Cartoon", "empty", Constant.RADIO));
        arrayList_first.add(new Category_M(4, "News", "empty", Constant.RADIO));
        arrayList_first.add(new Category_M(5, "Sport", "empty", Constant.RADIO));

        return arrayList_first;
    }

}
