package com.note.mydiary.diarywithlock.journalwithlock.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomSeekBar extends View {

    private Paint paint, paintProgress;
    private int progress, max, colorBackground, colorThumb, colorProgress, colorBorder;
    private float sizeThumb, sizeBg, sizePos;
    private OnSeekbarResult onSeekbarResult;

    public void setOnSeekbarResult(OnSeekbarResult onSeekbarResult) {
        this.onSeekbarResult = onSeekbarResult;
    }

    public CustomSeekBar(Context context) {
        super(context);
        init(context);
    }

    public CustomSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        sizeThumb = context.getResources().getDimension(com.intuit.sdp.R.dimen._10sdp);
        sizeBg = context.getResources().getDimension(com.intuit.sdp.R.dimen._3sdp);
        sizePos = context.getResources().getDimension(com.intuit.sdp.R.dimen._5sdp);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintProgress = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        paint.setStyle(Paint.Style.FILL);
        paintProgress.setStyle(Paint.Style.FILL);
        paintProgress.setStrokeJoin(Paint.Join.ROUND);
        paintProgress.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.clearShadowLayer();
        paint.setColor(colorBackground);
        paint.setStrokeWidth(sizeBg);
        int temp = 45;
        canvas.drawLine(sizeThumb / 2 + temp, getHeight() / 2f, getWidth() - sizeThumb / 2 - temp, getHeight() / 2f, paint);

        paintProgress.setColor(colorProgress);
        paintProgress.setStrokeWidth(sizePos);
        float p = (getWidth() - sizeThumb - 2 * temp) * progress / max + sizeThumb / 2 + temp;
        canvas.drawLine(sizeThumb / 2 + temp, getHeight() / 2f, p, getHeight() / 2f, paintProgress);

        paint.setColor(colorThumb);
        paint.setShadowLayer(sizeThumb / 8, 0, 0, Color.WHITE);
        canvas.drawCircle(p, getHeight() / 2f, sizeThumb / 2, paint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (onSeekbarResult != null) onSeekbarResult.onDown(this);
                break;
            case MotionEvent.ACTION_MOVE:
                progress = (int) ((x - sizeThumb / 2) * max / ((getWidth() - sizeThumb)));

                if (progress < 0) progress = 0;
                else if (progress > max) progress = max;
                invalidate();
                if (onSeekbarResult != null) onSeekbarResult.onMove(this, progress);
                break;
            case MotionEvent.ACTION_UP:
                if (onSeekbarResult != null) onSeekbarResult.onUp(this, progress);
                break;
        }
        return true;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public int getProgress() {
        return progress;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
        invalidate();
    }

    public int getColorBackground() {
        return colorBackground;
    }

    public void setColorBackground(int colorBackground) {
        this.colorBackground = colorBackground;
        invalidate();
    }

    public int getColorThumb() {
        return colorThumb;
    }

    public void setColorThumb(int colorThumb) {
        this.colorThumb = colorThumb;
        invalidate();
    }

    public int getColorProgress() {
        return colorProgress;
    }

    public void setColorProgress(int colorProgress) {
        this.colorProgress = colorProgress;
        invalidate();
    }

    public int getColorBorder() {
        return colorBorder;
    }

    public void setColorBorder(int colorBorder) {
        this.colorBorder = colorBorder;
        invalidate();
    }
}
