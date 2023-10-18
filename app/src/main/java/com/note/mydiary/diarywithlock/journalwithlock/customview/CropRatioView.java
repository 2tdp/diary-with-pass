package com.note.mydiary.diarywithlock.journalwithlock.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import com.edmodo.cropper.CropImageView;

public class CropRatioView extends CropImageView {

    private Bitmap bitmap;

    public CropRatioView(Context context) {
        super(context);
        setSize();
    }

    public CropRatioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSize();
    }

    private void setSize() {
        setAspectRatio(3, 2);
        setFixedAspectRatio(true);
        setGuidelines(2);
    }

    public void setData(Bitmap bitmap) {
        if (bitmap != null) {
            this.bitmap = bitmap;
            setImageBitmap(bitmap);
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
