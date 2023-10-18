package com.note.mydiary.diarywithlock.journalwithlock.utils;

import android.content.Context;
import android.widget.ImageView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.utils.designvector.VectorChildFinder;
import com.note.mydiary.diarywithlock.journalwithlock.utils.designvector.VectorDrawableCompat;

public class UtilsTheme {

    public static void changeIconSelectVip(Context context, ImageView imageView, int colorSelect, int colorUnSelect, boolean isSelect) {
        VectorChildFinder icSelectVip, icUnSelectVip;
        VectorDrawableCompat.VFullPath circleOutside, circleInside;

        if (isSelect) {
            icSelectVip = new VectorChildFinder(context, R.drawable.ic_selected, imageView);

            circleInside = icSelectVip.findPathByName("circleInside");
            circleOutside = icSelectVip.findPathByName("circleOutside");

            circleOutside.setStrokeWidth(1);
            circleOutside.setStrokeColor(colorSelect);
            circleInside.setFillColor(colorSelect);
        } else {
            icUnSelectVip = new VectorChildFinder(context, R.drawable.ic_un_selected, imageView);

            circleOutside = icUnSelectVip.findPathByName("circleOutside");
            circleOutside.setStrokeColor(colorUnSelect);
        }
    }

    public static void changeIconSelectDiary(Context context, ImageView imageView, int colorSelect, int colorUnSelect, int colorTick, boolean isSelect) {
        VectorChildFinder icSelectVip, icUnSelectVip;
        VectorDrawableCompat.VFullPath circleOutside, circleInside, tick;

        if (isSelect) {
            icSelectVip = new VectorChildFinder(context, R.drawable.ic_tick_del, imageView);

            circleInside = icSelectVip.findPathByName("circleInside");
            circleOutside = icSelectVip.findPathByName("circleOutside");
            tick = icSelectVip.findPathByName("tick");

            circleOutside.setStrokeWidth(1);
            circleOutside.setStrokeColor(colorSelect);
            circleInside.setFillColor(colorSelect);
            tick.setFillColor(colorTick);
        } else {
            icUnSelectVip = new VectorChildFinder(context, R.drawable.ic_un_tick_del, imageView);

            circleOutside = icUnSelectVip.findPathByName("circleOutside");
            circleOutside.setStrokeWidth(1);
            circleOutside.setStrokeColor(colorUnSelect);
        }
    }

    public static void changeIcon(Context context, String namePath, int quantityPath, int resPath, ImageView imageView, int colorBackground, int color) {
        VectorChildFinder vectorChild = null;
        VectorDrawableCompat.VFullPath path;

        for (int i = 0; i < quantityPath; i++) {
            if (vectorChild == null)
                vectorChild = new VectorChildFinder(context, resPath, imageView);
            path = vectorChild.findPathByName(namePath + (i + 1));
            if (i == 0) path.setFillColor(colorBackground);
            else path.setFillColor(color);
        }
    }

    public static void changeIconMore(Context context, String namePath, int quantityPath, int resPath, ImageView imageView, int colorBackground, int color){
        VectorChildFinder vectorChild = null;
        VectorDrawableCompat.VFullPath path;

        for (int i = 0; i < quantityPath; i++) {
            if (vectorChild == null)
                vectorChild = new VectorChildFinder(context, resPath, imageView);
            path = vectorChild.findPathByName(namePath + (i + 1));
            path.setStrokeWidth(2);
            if (i == 0) path.setStrokeColor(colorBackground);
            else path.setStrokeColor(color);
        }
    }

    public static void changeIconCalendar(Context context, String namePath, int quantityPath, int resPath, ImageView imageView, int colorBackground, int color) {
        VectorChildFinder vectorChild = null;
        VectorDrawableCompat.VFullPath path;

        for (int i = 0; i < quantityPath; i++) {
            if (vectorChild == null)
                vectorChild = new VectorChildFinder(context, resPath, imageView);
            path = vectorChild.findPathByName(namePath + (i + 1));
            path.setStrokeWidth(1.5f);
            if (i == 0) path.setStrokeColor(colorBackground);
            else path.setStrokeColor(color);
        }
    }
}
