package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.pinlockview;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The customization options for the buttons in {@link PinLockView}
 * passed to the {@link PinLockAdapter} to decorate the individual views
 * <p>
 * Created by aritraroy on 01/06/16.
 */
public class CustomizationOptionsBundle implements Serializable {

    private int textColor;
    private float textSize;
    private int buttonSize;
    private Typeface typeface;
    private Drawable buttonBackgroundDrawable;
    private Drawable drawableDel;
    private ArrayList<Bitmap> lstBitmap;
    private boolean isText;

    public CustomizationOptionsBundle() {
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getButtonSize() {
        return buttonSize;
    }

    public void setButtonSize(int buttonSize) {
        this.buttonSize = buttonSize;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public Drawable getButtonBackgroundDrawable() {
        return buttonBackgroundDrawable;
    }

    public void setButtonBackgroundDrawable(Drawable buttonBackgroundDrawable) {
        this.buttonBackgroundDrawable = buttonBackgroundDrawable;
    }

    public Drawable getDrawableDel() {
        return drawableDel;
    }

    public void setDrawableDel(Drawable drawableDel) {
        this.drawableDel = drawableDel;
    }

    public ArrayList<Bitmap> getLstBitmap() {
        return lstBitmap;
    }

    public void setLstBitmap(ArrayList<Bitmap> lstBitmap) {
        this.lstBitmap = lstBitmap;
    }

    public boolean isText() {
        return isText;
    }

    public void setText(boolean text) {
        isText = text;
    }
}
