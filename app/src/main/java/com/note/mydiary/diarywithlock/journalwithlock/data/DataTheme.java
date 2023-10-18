package com.note.mydiary.diarywithlock.journalwithlock.data;

import android.content.Context;

import com.google.gson.Gson;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.theme.JsonReader;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DataTheme {

    public static ArrayList<ThemeModel> getThemeApp(Context context) {
        ArrayList<ThemeModel> lstTheme = new ArrayList<>();

        if (Utils.getConnectionType(context) == 1 || Utils.getConnectionType(context) == 2) {
            try {
                JSONArray json = JsonReader.readJsonFromUrl(Constant.URL_THEME_APP);

                for (int i = 0; i < json.length(); i++) {
                    Gson gson = new Gson();
                    ThemeModel theme = gson.fromJson(json.get(i).toString(), ThemeModel.class);
                    theme.setUrl(theme.getUrl().replace("www.dropbox", "dl.dropboxusercontent"));
                    theme.setPreview(theme.getPreview().replace("www.dropbox", "dl.dropboxusercontent"));
                    theme.setSelected(theme.getName().equals(DataLocalManager.getOption(Constant.THEME_APP)));
                    theme.setOnl(!checkThemeApp(context, theme.getName()));

                    lstTheme.add(theme);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        } else {
            File[] filesApp = getTotalFile(context, "theme_app/theme_app/");
            if (filesApp != null && filesApp.length != 0)
                for (File f : filesApp) {
                    ThemeModel themeModel = new ThemeModel(
                            f.getName(),
                            "",
                            f.getPath() + "/preview.png", "free", checkThemeApp(context, f.getName()),
                            f.getName().equals(DataLocalManager.getOption(Constant.THEME_APP)));

                    String jsonConfig = Utils.readFromFile(context, "theme/theme_app/theme_app/" + themeModel.getName() + "/config.txt");

                    ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

                    if (config != null) themeModel.setColor(config.getColorMain());

                    lstTheme.add(themeModel);
                }
        }

        return lstTheme;
    }

    public static ArrayList<ThemeModel> getThemeLock(Context context) {
        ArrayList<ThemeModel> lstTheme = new ArrayList<>();

        if (Utils.getConnectionType(context) == 1 || Utils.getConnectionType(context) == 2) {
            try {
                JSONArray json = JsonReader.readJsonFromUrl(Constant.URL_THEME_PASSCODE);

                for (int i = 0; i < json.length(); i++) {
                    Gson gson = new Gson();
                    ThemeModel theme = gson.fromJson(json.get(i).toString(), ThemeModel.class);
                    theme.setUrl(theme.getUrl().replace("www.dropbox", "dl.dropboxusercontent"));
                    theme.setPreview(theme.getPreview().replace("www.dropbox", "dl.dropboxusercontent"));
                    theme.setSelected(theme.getName().equals(DataLocalManager.getOption(Constant.THEME_LOCK)));
                    theme.setOnl(!checkTheme(context, theme.getName()));

                    lstTheme.add(theme);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        } else {
            File[] filesPattern = getTotalFile(context, "theme_passcode/theme_pattern");
            File[] filesPincode = getTotalFile(context, "theme_passcode/theme_pincode");
            if (filesPattern.length != 0)
                for (File f : filesPattern)
                    lstTheme.add(new ThemeModel(
                            f.getName(),
                            "",
                            f.getPath() + "/preview.png", "free", checkTheme(context, f.getName()),
                            f.getName().equals(DataLocalManager.getOption(Constant.THEME_LOCK))));

            if (filesPincode.length != 0)
                for (File f : filesPincode)
                    lstTheme.add(new ThemeModel(
                            f.getName(),
                            "",
                            f.getPath() + "/preview.png", "free", checkTheme(context, f.getName()),
                            f.getName().equals(DataLocalManager.getOption(Constant.THEME_LOCK))));
        }

        return lstTheme;
    }

    public static File[] getTotalFile(Context context, String nameFolder) {
        File directory = new File(Utils.getStore(context) + "/theme/" + nameFolder);
        return directory.listFiles();
    }

    public static boolean checkTheme(Context context, String nameTheme) {
        File file = new File(Utils.getStore(context) + "/" + Constant.THEME_PASSCODE_FOLDER + nameTheme.substring(0, 13) + "/" + nameTheme);

        return file.exists();
    }

    public static boolean checkThemeApp(Context context, String nameTheme) {
        File file = new File(Utils.getStore(context) + "/" + Constant.THEME_APP_FOLDER + nameTheme.substring(0, 9) + "/" + nameTheme);

        return file.exists();
    }
}
