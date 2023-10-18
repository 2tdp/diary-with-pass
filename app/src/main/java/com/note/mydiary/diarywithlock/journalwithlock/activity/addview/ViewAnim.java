package com.note.mydiary.diarywithlock.journalwithlock.activity.addview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

public class ViewAnim extends View {

    private Paint paint;
    private Path p1, p2, p3;
    private String text;
    private float size;

    private ValueAnimator animator;
    private float tranY;
    private int a1 = 255, a2 = 155, a3 = 20;
    private int or1 = 1, or2 = 1, or3 = 1;
    private int step;

    public ViewAnim(Context context) {
        super(context);
        init();
    }

    public ViewAnim(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewAnim(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        size = dpToPx(13);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setTextSize(size);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(size / 8);
        text = "abc abc abc abc";

        animator = ValueAnimator.ofFloat(1);
        animator.addUpdateListener(animation -> {
            float p = (Float) animation.getAnimatedValue();
            tranY = (size / 2) * p;
            step++;
            if (step % 3 == 0) {
                a1 += or1 * 10;
                if (a1 > 255) {
                    a1 = 255;
                    or1 = -1;
                } else if (a1 < 20) {
                    a1 = 20;
                    or1 = 1;
                }
                a2 += or2 * 10;
                if (a2 > 255) {
                    a2 = 255;
                    or2 = -1;
                } else if (a2 < 20) {
                    a2 = 20;
                    or2 = 1;
                }
                a3 += or3 * 10;
                if (a3 > 255) {
                    a3 = 255;
                    or3 = -1;
                } else if (a3 < 20) {
                    a3 = 20;
                    or3 = 1;
                }
            }
            invalidate();
        });
        animator.setDuration(1600);
        animator.setInterpolator(new DecelerateInterpolator(1f));
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.start();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE) {
            if (!animator.isRunning())
                animator.start();
        } else {
            if (animator.isRunning())
                animator.cancel();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (p1 == null) {
            float s = size * 5 / 10;
            float fist = dpToPx(45);
            p1 = new Path();
            p1.moveTo(getWidth() / 2f - s, fist - s);
            p1.lineTo(getWidth() / 2f, fist);
            p1.lineTo(getWidth() / 2f + s, fist - s);

            fist += dpToPx(20);
            p2 = new Path();
            p2.moveTo(getWidth() / 2f - s, fist - s);
            p2.lineTo(getWidth() / 2f, fist);
            p2.lineTo(getWidth() / 2f + s, fist - s);

            fist += dpToPx(20);
            p3 = new Path();
            p3.moveTo(getWidth() / 2f - s, fist - s);
            p3.lineTo(getWidth() / 2f, fist);
            p3.lineTo(getWidth() / 2f + s, fist - s);
        }
        canvas.save();
        canvas.translate(0, tranY);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(255);
        canvas.drawText(text, getWidth() / 2f, dpToPx(18), paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAlpha(a1);
        canvas.drawPath(p1, paint);
        paint.setAlpha(a2);
        canvas.drawPath(p2, paint);
        paint.setAlpha(a3);
        canvas.drawPath(p3, paint);
        canvas.restore();
    }

    private float dpToPx(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public void setColor(int color) {
        this.paint.setColor(color);
        invalidate();
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }
}
