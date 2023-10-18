package com.note.mydiary.diarywithlock.journalwithlock.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;

public class DrawWidget {

    public static void drawText(Canvas canvas, Paint paint, float x, float y, String text, Typeface font, int colorRes, float sizeText) {
        canvas.save();
        paint.setTypeface(font);
        paint.setColor(colorRes);
        paint.setTextSize(sizeText);
        paint.setTextAlign(Paint.Align.CENTER);

        canvas.drawText(text, x, y, paint);
        canvas.restore();
    }

    public static void drawBitmap(Canvas canvas, Paint paint, Bitmap bitmap, float dx, float dy) {
        canvas.save();
        canvas.translate(dx, dy);
        canvas.drawBitmap(bitmap,
                null,
                new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                paint);
        canvas.restore();
    }

    public static Bitmap getBitmapEmoji(Context context, Bitmap emojiBitmap, float sizeBackground, float sizeEmoji, float dxTrans, float dyTrans) {

        Bitmap bitmap = Bitmap.createBitmap((int) sizeBackground, (int) sizeBackground, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBackground.setColor(context.getResources().getColor(R.color.gray_1));
        paintBackground.setStyle(Paint.Style.FILL);

        canvas.drawRoundRect(0, 0, sizeBackground, sizeBackground, 10, 10, paintBackground);

        canvas.translate(dxTrans, dyTrans);
        canvas.drawBitmap(emojiBitmap,
                null,
                new RectF(0, 0, sizeEmoji, sizeEmoji),
                new Paint(Paint.FILTER_BITMAP_FLAG));

        return bitmap;
    }

    public static void drawCircle(Canvas canvas, Paint paint, float cx, float cy, float radius) {
        canvas.save();
        canvas.drawCircle(cx, cy, radius, paint);
        canvas.restore();
    }

    public static void drawRec(Canvas canvas, Paint paint, RectF rectf) {
        canvas.save();
        canvas.drawRoundRect(rectf, 10, 10, paint);
        canvas.restore();
    }

    public static float[] getLocationDay(int position) {
        switch (position) {
            case 1:
                return new float[]{Constant.LIST_DX_POSITION[0], Constant.LIST_DY_POSITION[0]};
            case 2:
                return new float[]{Constant.LIST_DX_POSITION[1], Constant.LIST_DY_POSITION[0]};
            case 3:
                return new float[]{Constant.LIST_DX_POSITION[2], Constant.LIST_DY_POSITION[0]};
            case 4:
                return new float[]{Constant.LIST_DX_POSITION[3], Constant.LIST_DY_POSITION[0]};
            case 5:
                return new float[]{Constant.LIST_DX_POSITION[4], Constant.LIST_DY_POSITION[0]};
            case 6:
                return new float[]{Constant.LIST_DX_POSITION[5], Constant.LIST_DY_POSITION[0]};
            case 7:
                return new float[]{Constant.LIST_DX_POSITION[6], Constant.LIST_DY_POSITION[0]};
            case 8:
                return new float[]{Constant.LIST_DX_POSITION[0], Constant.LIST_DY_POSITION[1]};
            case 9:
                return new float[]{Constant.LIST_DX_POSITION[1], Constant.LIST_DY_POSITION[1]};
            case 10:
                return new float[]{Constant.LIST_DX_POSITION[2], Constant.LIST_DY_POSITION[1]};
            case 11:
                return new float[]{Constant.LIST_DX_POSITION[3], Constant.LIST_DY_POSITION[1]};
            case 12:
                return new float[]{Constant.LIST_DX_POSITION[4], Constant.LIST_DY_POSITION[1]};
            case 13:
                return new float[]{Constant.LIST_DX_POSITION[5], Constant.LIST_DY_POSITION[1]};
            case 14:
                return new float[]{Constant.LIST_DX_POSITION[6], Constant.LIST_DY_POSITION[1]};
            case 15:
                return new float[]{Constant.LIST_DX_POSITION[0], Constant.LIST_DY_POSITION[2]};
            case 16:
                return new float[]{Constant.LIST_DX_POSITION[1], Constant.LIST_DY_POSITION[2]};
            case 17:
                return new float[]{Constant.LIST_DX_POSITION[2], Constant.LIST_DY_POSITION[2]};
            case 18:
                return new float[]{Constant.LIST_DX_POSITION[3], Constant.LIST_DY_POSITION[2]};
            case 19:
                return new float[]{Constant.LIST_DX_POSITION[4], Constant.LIST_DY_POSITION[2]};
            case 20:
                return new float[]{Constant.LIST_DX_POSITION[5], Constant.LIST_DY_POSITION[2]};
            case 21:
                return new float[]{Constant.LIST_DX_POSITION[6], Constant.LIST_DY_POSITION[2]};
            case 22:
                return new float[]{Constant.LIST_DX_POSITION[0], Constant.LIST_DY_POSITION[3]};
            case 23:
                return new float[]{Constant.LIST_DX_POSITION[1], Constant.LIST_DY_POSITION[3]};
            case 24:
                return new float[]{Constant.LIST_DX_POSITION[2], Constant.LIST_DY_POSITION[3]};
            case 25:
                return new float[]{Constant.LIST_DX_POSITION[3], Constant.LIST_DY_POSITION[3]};
            case 26:
                return new float[]{Constant.LIST_DX_POSITION[4], Constant.LIST_DY_POSITION[3]};
            case 27:
                return new float[]{Constant.LIST_DX_POSITION[5], Constant.LIST_DY_POSITION[3]};
            case 28:
                return new float[]{Constant.LIST_DX_POSITION[6], Constant.LIST_DY_POSITION[3]};
            case 29:
                return new float[]{Constant.LIST_DX_POSITION[0], Constant.LIST_DY_POSITION[4]};
            case 30:
                return new float[]{Constant.LIST_DX_POSITION[1], Constant.LIST_DY_POSITION[4]};
            case 31:
                return new float[]{Constant.LIST_DX_POSITION[2], Constant.LIST_DY_POSITION[4]};
            case 32:
                return new float[]{Constant.LIST_DX_POSITION[3], Constant.LIST_DY_POSITION[4]};
            case 33:
                return new float[]{Constant.LIST_DX_POSITION[4], Constant.LIST_DY_POSITION[4]};
            case 34:
                return new float[]{Constant.LIST_DX_POSITION[5], Constant.LIST_DY_POSITION[4]};
            case 35:
                return new float[]{Constant.LIST_DX_POSITION[6], Constant.LIST_DY_POSITION[4]};
            case 36:
                return new float[]{Constant.LIST_DX_POSITION[0], Constant.LIST_DY_POSITION[5]};
            case 37:
                return new float[]{Constant.LIST_DX_POSITION[1], Constant.LIST_DY_POSITION[5]};
            case 38:
                return new float[]{Constant.LIST_DX_POSITION[2], Constant.LIST_DY_POSITION[5]};
            case 39:
                return new float[]{Constant.LIST_DX_POSITION[3], Constant.LIST_DY_POSITION[5]};
            case 40:
                return new float[]{Constant.LIST_DX_POSITION[4], Constant.LIST_DY_POSITION[5]};
            case 41:
                return new float[]{Constant.LIST_DX_POSITION[5], Constant.LIST_DY_POSITION[5]};
            case 42:
                return new float[]{Constant.LIST_DX_POSITION[6], Constant.LIST_DY_POSITION[5]};
        }
        return new float[]{0, 0};
    }
}
