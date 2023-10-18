package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.patternview.customView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.patternview.bean.CellBean;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.patternview.config.Config;

/**
 *
 */

public class DefaultLockerHitCellView implements IHitCellView {

    private RadialGradient mRadialGradient;
    private @ColorInt
    int hitColor;
    private @ColorInt
    int errorColor;
    private @ColorInt
    int fillColor;
    private @ColorInt
    int normalColor;

    private Context context;
    private Bitmap bitmap;
    private RectF rectF;
    private String themeFolder;
    private int size;

    private float lineWidth;

    private final Paint paint;

    public DefaultLockerHitCellView() {
        this.paint = Config.createPaint();
        this.paint.setStyle(Paint.Style.FILL);
        themeFolder = "";
    }

    public @ColorInt
    int getHitColor() {
        return hitColor;
    }

    public DefaultLockerHitCellView setHitColor(@ColorInt int hitColor) {
        this.hitColor = hitColor;
        return this;
    }

    public @ColorInt
    int getErrorColor() {
        return errorColor;
    }

    public DefaultLockerHitCellView setErrorColor(@ColorInt int errorColor) {
        this.errorColor = errorColor;
        return this;
    }

    public int getNormalColor() {
        return normalColor;
    }

    public DefaultLockerHitCellView setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        return this;
    }

    public @ColorInt
    int getFillColor() {
        return fillColor;
    }

    public DefaultLockerHitCellView setFillColor(@ColorInt int fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public DefaultLockerHitCellView setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public DefaultLockerHitCellView setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        if (rectF == null) rectF = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        return this;
    }

    public DefaultLockerHitCellView setThemeFolder(Context context, String themeFolder, int size) {
        this.context = context;
        this.themeFolder = themeFolder;
        this.size = size;
        if (rectF == null) rectF = new RectF(0, 0, size, size);
        return this;
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull CellBean cellBean, boolean isError) {
        final int saveCount = canvas.save();

        // draw outer circle
//        this.paint.setColor(this.getOuterColor(isError));
//        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, this.paint);

        if (themeFolder.equals("")){
            if (bitmap == null) {
                // draw fill circle
                Paint mPaint = new Paint();
                mRadialGradient = new RadialGradient(cellBean.x, cellBean.y, cellBean.radius - this.getLineWidth() * 4f,
                        getGradientColor(isError), getGradientColor(isError), Shader.TileMode.REPEAT);
                mPaint.setShader(mRadialGradient);
                canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius - this.getLineWidth() * 4f, mPaint);

                // draw inner circle
                this.paint.setColor(this.getColor(isError));
                canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius / 4f, this.paint);
            } else {
                canvas.translate(cellBean.x - bitmap.getWidth() / 2f, cellBean.y - bitmap.getHeight() / 2f);
                canvas.drawBitmap(bitmap, null, rectF, paint);
            }
        } else {
            Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(themeFolder + "/theme_pattern_hit_" + cellBean.id + ".png"), size, size, false);

            canvas.translate(cellBean.x - size / 2f, cellBean.y - size / 2f);
            canvas.drawBitmap(bitmap, null, rectF, paint);
        }

        canvas.restoreToCount(saveCount);
    }

    private @ColorInt
    int getColor(boolean isError) {
        return isError ? this.getErrorColor() : this.getHitColor();
    }

    private @ColorInt
    int getOuterColor(boolean isError) {
        return isError ? this.getErrorColor() : this.getNormalColor();
    }


    private @ColorInt
    int getGradientColor(boolean isError) {
        return isError ? this.getErrorColor() : this.getFillColor();
    }
}
