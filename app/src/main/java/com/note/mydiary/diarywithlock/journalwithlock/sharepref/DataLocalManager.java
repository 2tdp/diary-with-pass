package com.note.mydiary.diarywithlock.journalwithlock.sharepref;

import android.content.Context;
import android.gesture.GestureStroke;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.note.mydiary.diarywithlock.journalwithlock.model.NotifyModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.WidgetModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.DiaryModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.picture.BucketPicModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigPatternThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigPinThemeModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataLocalManager {

    private static DataLocalManager instance;
    private MySharedPreferences mySharedPreferences;

    public static void init(Context context) {
        instance = new DataLocalManager();
        instance.mySharedPreferences = new MySharedPreferences(context);
    }

    public static DataLocalManager getInstance() {

        if (instance == null) {
            instance = new DataLocalManager();
        }
        return instance;
    }

    public static void setFirstInstall(String key, boolean isFirst) {
        DataLocalManager.getInstance().mySharedPreferences.putBooleanValue(key, isFirst);
    }

    public static boolean getFirstInstall(String key) {
        return DataLocalManager.getInstance().mySharedPreferences.getBooleanValue(key);
    }

    public static void setCheck(String key, boolean volumeOn) {
        DataLocalManager.getInstance().mySharedPreferences.putBooleanValue(key, volumeOn);
    }

    public static boolean getCheck(String key) {
        return DataLocalManager.getInstance().mySharedPreferences.getBooleanValue(key);
    }

    public static void setOption(String option, String key) {
        DataLocalManager.getInstance().mySharedPreferences.putStringwithKey(key, option);
    }

    public static String getOption(String key) {
        return DataLocalManager.getInstance().mySharedPreferences.getStringwithKey(key, "");
    }

    public static void setInt(int count, String key) {
        DataLocalManager.getInstance().mySharedPreferences.putIntWithKey(key, count);
    }

    public static int getInt(String key) {
        return DataLocalManager.getInstance().mySharedPreferences.getIntWithKey(key, -1);
    }

    public static void setListTimeStamp(String key, ArrayList<String> lstTimeStamp) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.toJsonTree(lstTimeStamp).getAsJsonArray();
        String json = jsonArray.toString();

        DataLocalManager.getInstance().mySharedPreferences.putStringwithKey(key, json);
    }

    public static ArrayList<String> getListTimeStamp(String key) {
        ArrayList<String> lstTimeStamp = new ArrayList<>();
        String strJson = DataLocalManager.getInstance().mySharedPreferences.getStringwithKey(key, "");
        try {
            JSONArray jsonArray = new JSONArray(strJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                lstTimeStamp.add(jsonArray.get(i).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lstTimeStamp;
    }

    public static void setDiary(String key, DiaryModel diaryModel) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.toJsonTree(diaryModel).getAsJsonObject();
        String json = jsonObject.toString();

        DataLocalManager.getInstance().mySharedPreferences.putStringwithKey(key, json);
    }

    public static DiaryModel getDiary(String key) {
        String strJson = DataLocalManager.getInstance().mySharedPreferences.getStringwithKey(key, "");
        DiaryModel diaryModel = null;

        Gson gson = new Gson();

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(strJson);
            diaryModel = gson.fromJson(jsonObject.toString(), DiaryModel.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return diaryModel;
    }

    public static ConfigPatternThemeModel getConfigPattern(String json) {
        ConfigPatternThemeModel configPatternThemeModel = null;

        Gson gson = new Gson();

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            configPatternThemeModel = gson.fromJson(jsonObject.toString(), ConfigPatternThemeModel.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return configPatternThemeModel;
    }

    public static ConfigPinThemeModel getConfigPin(String json) {
        ConfigPinThemeModel configPinThemeModel = null;

        Gson gson = new Gson();

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            configPinThemeModel = gson.fromJson(jsonObject.toString(), ConfigPinThemeModel.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return configPinThemeModel;
    }

    public static ConfigAppThemeModel getConfigApp(String json) {
        ConfigAppThemeModel configAppThemeModel = null;

        Gson gson = new Gson();

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            configAppThemeModel = gson.fromJson(jsonObject.toString(), ConfigAppThemeModel.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return configAppThemeModel;
    }

    public static void setNotify(String key, NotifyModel notifyModel) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.toJsonTree(notifyModel).getAsJsonObject();
        String json = jsonObject.toString();

        DataLocalManager.getInstance().mySharedPreferences.putStringwithKey(key, json);
    }

    public static NotifyModel getNotify(String key) {
        String strJson = DataLocalManager.getInstance().mySharedPreferences.getStringwithKey(key, "");
        NotifyModel notifyModel = null;

        Gson gson = new Gson();

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(strJson);
            notifyModel = gson.fromJson(jsonObject.toString(), NotifyModel.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return notifyModel;
    }

    public static void setArrWidget(String key, ArrayList<WidgetModel> lstWidget) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.toJsonTree(lstWidget).getAsJsonArray();
        String json = jsonArray.toString();

        DataLocalManager.getInstance().mySharedPreferences.putStringwithKey(key, json);
    }

    public static ArrayList<WidgetModel> getArrWidget(String key) {
        ArrayList<WidgetModel> lstWidget = new ArrayList<>();
        Gson gson = new Gson();
        String strJson = DataLocalManager.getInstance().mySharedPreferences.getStringwithKey(key, "");
        try {
            JSONArray jsonArray = new JSONArray(strJson);
            JSONObject jsonObject;
            WidgetModel widget;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                widget = gson.fromJson(jsonObject.toString(), WidgetModel.class);
                lstWidget.add(widget);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lstWidget;
    }

    public static void setListGestureStroke(ArrayList<GestureStroke> lstGestureStroke, String key) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.toJsonTree(lstGestureStroke).getAsJsonArray();
        String json = jsonArray.toString();

        DataLocalManager.getInstance().mySharedPreferences.putStringwithKey(key, json);
    }

    public static ArrayList<GestureStroke> getListGestureStroke(String key) {
        Gson gson = new Gson();
        JSONObject jsonObject;
        ArrayList<GestureStroke> lstGestureStroke = new ArrayList<>();

        String strJson = DataLocalManager.getInstance().mySharedPreferences.getStringwithKey(key, "");
        try {
            JSONArray jsonArray = new JSONArray(strJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                lstGestureStroke.add(gson.fromJson(jsonObject.toString(), GestureStroke.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return lstGestureStroke;
    }

    public static void setListBucket(ArrayList<BucketPicModel> lstBucket, String key) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.toJsonTree(lstBucket).getAsJsonArray();
        String json = jsonArray.toString();

        DataLocalManager.getInstance().mySharedPreferences.putStringwithKey(key, json);
    }

    public static ArrayList<BucketPicModel> getListBucket(String key) {
        Gson gson = new Gson();
        JSONObject jsonObject;
        ArrayList<BucketPicModel> lstBucket = new ArrayList<>();

        String strJson = DataLocalManager.getInstance().mySharedPreferences.getStringwithKey(key, "");
        try {
            JSONArray jsonArray = new JSONArray(strJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                lstBucket.add(gson.fromJson(jsonObject.toString(), BucketPicModel.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return lstBucket;
    }
}
