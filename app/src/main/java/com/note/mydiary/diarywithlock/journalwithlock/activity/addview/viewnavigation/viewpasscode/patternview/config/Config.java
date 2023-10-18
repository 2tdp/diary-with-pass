package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.patternview.config;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.TypedValue;

import androidx.annotation.ColorInt;


public class Config {
    private static final String DEFAULT_NORMAL_COLOR = "#FFAF7D";
    private static final String DEFAULT_HIT_COLOR = "#FFAF7D";
    private static final String DEFAULT_ERROR_COLOR = "#E24B4B";
    private static final String DEFAULT_FILL_COLOR = "#FFEFEB";
    private static final float DEFAULT_LINE_WIDTH = 0.5f;
    private static final int DEFAULT_DELAY_TIME = 500;//ms
    private static final boolean DEFAULT_ENABLE_AUTO_CLEAN = true;

    public static @ColorInt
    int getDefaultNormalColor() {
        return Color.parseColor(DEFAULT_NORMAL_COLOR);
    }

    public static @ColorInt
    int getDefaultHitColor() {
        return Color.parseColor(DEFAULT_HIT_COLOR);
    }

    public static @ColorInt
    int getDefaultErrorColor() {
        return Color.parseColor(DEFAULT_ERROR_COLOR);
    }

    public static @ColorInt
    int getDefaultFillColor() {
        return Color.parseColor(DEFAULT_FILL_COLOR);
    }

    public static float getDefaultLineWidth(Resources resources) {
        return convertDpToPx(DEFAULT_LINE_WIDTH, resources);
    }

    public static int getDefaultDelayTime() {
        return DEFAULT_DELAY_TIME;
    }

    public static boolean getDefaultEnableAutoClean() {
        return DEFAULT_ENABLE_AUTO_CLEAN;
    }

    public static Paint createPaint() {
        final Paint paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        return paint;
    }

    private static float convertDpToPx(float dp, Resources resources) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }
}