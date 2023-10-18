package com.note.mydiary.diarywithlock.journalwithlock.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

import java.io.File;
import java.util.ArrayList;

public class DataThemePincode {

    public static ArrayList<Bitmap> getTheme(Context context, String folder) {
        ArrayList<Bitmap> lstThem = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            lstThem.add(Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888));
        }
        Bitmap bmZero = null, bmDel = null;

        String path = Utils.getStore(context) + "/theme/theme_passcode/" + folder.substring(0, 13) + "/" + folder;
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().equals("theme_pincode_11.png"))
                    bmDel = BitmapFactory.decodeFile(file.getPath());
                else if (file.getName().equals("theme_pincode_10.png"))
                    bmZero = BitmapFactory.decodeFile(file.getPath());
                else if (!file.getName().equals("background.png")
                        && !file.getName().equals("ic_lock.png")
                        && !file.getName().equals("config.txt")
                        && !file.getName().equals("preview.png")
                        && !file.getName().contains(".ttf"))

                    lstThem.set(Integer.parseInt(file.getName().replace("theme_pincode_", "").replace(".png", "")) - 1,
                            BitmapFactory.decodeFile(file.getPath()));
            }
            if (bmZero != null) lstThem.add(bmZero);
            if (bmDel != null) lstThem.add(bmDel);
        }
        return lstThem;
    }
}
