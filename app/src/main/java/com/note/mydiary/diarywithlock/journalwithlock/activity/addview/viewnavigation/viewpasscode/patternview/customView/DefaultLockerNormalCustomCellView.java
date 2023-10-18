package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.patternview.customView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.patternview.bean.CellBean;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.patternview.config.Config;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;


/**
 *
 */

public class DefaultLockerNormalCustomCellView implements INormalCellView {

    private @ColorInt
    int normalColor;
    private @ColorInt
    int fillColor;
    private @ColorInt
    int hitColor;

    private Context context;
    private Bitmap bitmap;
    private RectF rectF;
    private String themeFolder;
    private int size;

    private float lineWidth;

    private final Paint paint;

    public DefaultLockerNormalCustomCellView() {
        this.paint = Config.createPaint();
        this.paint.setStyle(Paint.Style.FILL);
        themeFolder = "";
    }

    public int getNormalColor() {
        return normalColor;
    }

    public DefaultLockerNormalCustomCellView setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        return this;
    }

    public int getFillColor() {
        return fillColor;
    }

    public DefaultLockerNormalCustomCellView setFillColor(int fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public DefaultLockerNormalCustomCellView setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }


    public DefaultLockerNormalCustomCellView setHitColor(@ColorInt int hitColor) {
        this.hitColor = hitColor;
        return this;
    }

    public @ColorInt
    int getHitColor() {
        return hitColor;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public DefaultLockerNormalCustomCellView setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        if (rectF == null) rectF = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        return this;
    }

    public DefaultLockerNormalCustomCellView setThemeFolder(Context context, String themeFolder, int size) {
        this.context = context;
        this.themeFolder = themeFolder;
        this.size = size;
        if (rectF == null) rectF = new RectF(0, 0, size, size);
        return this;
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull CellBean cellBean) {
        final int saveCount = canvas.save();

        Log.d(Constant.TAG, "draw: " + cellBean.id);
        // draw inner circle
        if (themeFolder.equals("")) {
            if (bitmap == null) {
                this.paint.setColor(getNormalColor());
                canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius / 4f, this.paint);
            } else {
                canvas.translate(cellBean.x - bitmap.getWidth() / 2f, cellBean.y - bitmap.getHeight() / 2f);
                canvas.drawBitmap(bitmap, null, rectF, paint);
            }
        } else {
            Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(themeFolder + "/theme_pattern_unhit_" + cellBean.id + ".png"), size, size, false);

            canvas.translate(cellBean.x - size / 2f, cellBean.y - size / 2f);
            canvas.drawBitmap(bitmap, null, rectF, paint);
        }

        canvas.restoreToCount(saveCount);
    }


}
