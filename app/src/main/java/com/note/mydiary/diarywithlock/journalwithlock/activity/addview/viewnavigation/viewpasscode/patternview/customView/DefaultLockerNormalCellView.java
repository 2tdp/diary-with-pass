package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.patternview.customView;

import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.patternview.bean.CellBean;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.patternview.config.Config;


/**
 */

public class DefaultLockerNormalCellView implements INormalCellView {

    private @ColorInt
    int normalColor;
    private @ColorInt
    int fillColor;

    private float lineWidth;

    private Paint paint;

    public DefaultLockerNormalCellView() {
        this.paint = Config.createPaint();
        this.paint.setStyle(Paint.Style.FILL);
    }

    public int getNormalColor() {
        return normalColor;
    }

    public DefaultLockerNormalCellView setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        return this;
    }

    public int getFillColor() {
        return fillColor;
    }

    public DefaultLockerNormalCellView setFillColor(int fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public DefaultLockerNormalCellView setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull CellBean cellBean) {
        final int saveCount = canvas.save();

        // draw outer circle
        this.paint.setColor(this.getNormalColor());
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, this.paint);

        // draw fill circle
        this.paint.setColor(this.getFillColor());
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius - this.getLineWidth(), this.paint);

        canvas.restoreToCount(saveCount);
    }
}
