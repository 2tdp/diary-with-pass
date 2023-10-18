package com.note.mydiary.diarywithlock.journalwithlock.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;

public class CustomSeekbarDownload extends View {

    private Paint paintPr, paintStroke, paint_text;
    private Path path;
    private RectF rectF;
    private RectF rectF2;
    private float padding;
    private int progress;
    private float[] floatsRadius;
    private float[] floatsRadius2;

    public CustomSeekbarDownload(Context context) {
        super(context);
        init();
    }

    private void init() {
        int w = getContext().getResources().getDisplayMetrics().widthPixels;

        path = new Path();

        paintPr = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintPr.setStyle(Paint.Style.FILL);

        paint_text = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_text.setStyle(Paint.Style.FILL);

        paintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintStroke.setStyle(Paint.Style.STROKE);

        rectF = new RectF();
        rectF2 = new RectF();

        progress = 100;
        padding = w / 100f;
        Log.d(Constant.TAG, "init: " + padding);
        float radius = w * 4f / 100;
        floatsRadius = new float[]{radius, radius, radius, radius, radius, radius, radius, radius};
        floatsRadius2 = new float[]{radius * 2 / 3, radius * 2 / 3, radius * 2 / 3, radius * 2 / 3, radius * 2 / 3, radius * 2 / 3, radius * 2 / 3, radius * 2 / 3};

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rectF.set(padding, padding, getWidth() - padding, getHeight() - padding);
        path.reset();
        path.addRoundRect(rectF, floatsRadius, Path.Direction.CW);
        canvas.drawPath(path, paintStroke);
        if (progress > 0)
            rectF2.set(padding * 2.2f, padding * 2.2f, getWidth() - progress * rectF.width() / 100, getHeight() - padding * 2.2f);
        else
            rectF2.set(padding * 2.2f, padding * 2.2f, getWidth() - progress * rectF.width() / 100 - padding * 2.2f, getHeight() - padding * 2.2f);
        path.reset();
        path.addRoundRect(rectF2, floatsRadius2, Path.Direction.CW);
        canvas.drawPath(path, paintPr);

        drawCenter(canvas, paint_text, 100 - progress + "%");
    }

    public void setProgress(int progress) {
        this.progress = 100 - progress;
        invalidate();
    }

    private void drawCenter(Canvas canvas, Paint paint, String text) {
        Rect r = new Rect();
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.setTextAlign(Paint.Align.LEFT);
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }

    public void setColorStroke(int colorStroke) {
        paintStroke.setColor(colorStroke);
        invalidate();
    }

    public void setColorPro(int colorPro) {
        paintPr.setColor(colorPro);
        invalidate();
    }

    public void setColorText(int colorText) {
        paint_text.setColor(colorText);
    }

    public void setTextSize(float textSize) {
        paint_text.setTextSize(textSize);
        invalidate();
    }

    public void setSizeStroke(float stroke) {
        paintStroke.setStrokeWidth(stroke);
    }
}
