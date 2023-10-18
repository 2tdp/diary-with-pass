package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewpickpicture;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.ViewToolbar;

public class ViewPreviewPicture extends LinearLayout {

    static int w;
    ViewToolbar viewToolbar;
    ImageView imageView;

    public ViewPreviewPicture(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        setOrientation(VERTICAL);
        setBackgroundColor(getResources().getColor(R.color.white));
        setClickable(true);
        setFocusable(true);

        viewToolbar = new ViewToolbar(context);
        viewToolbar.getTvSave().setVisibility(GONE);
        viewToolbar.getIvVip().setVisibility(GONE);
        viewToolbar.getTvCancel().setVisibility(GONE);
        viewToolbar.getTvTitle().setText(getResources().getString(R.string.gallery));
        LayoutParams paramsToolbar = new LayoutParams(-1, -2);
        paramsToolbar.setMargins(0, (int) (w * 11.125f / 100), 0, (int) (4.167f * w / 100));
        addView(viewToolbar, paramsToolbar);

        imageView = new ImageView(context);
        LayoutParams paramsImage = new LayoutParams(-1, -1);
        paramsImage.setMargins(0, (int) (5.556f * w / 100), 0, 0);
        addView(imageView, paramsImage);
    }

    public ViewToolbar getViewToolbar() {
        return viewToolbar;
    }

    public ImageView getImageView() {
        return imageView;
    }
}
