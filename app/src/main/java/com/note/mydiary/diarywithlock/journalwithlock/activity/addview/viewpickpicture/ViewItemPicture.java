package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewpickpicture;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.makeramen.roundedimageview.RoundedImageView;
import com.note.mydiary.diarywithlock.journalwithlock.R;

public class ViewItemPicture extends RelativeLayout {

    static int w;
    RoundedImageView iv;
    ImageView ivRemove;

    public ViewItemPicture(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        setClickable(true);
        setFocusable(true);

        iv = new RoundedImageView(context);
        iv.setPadding(w / 100, w / 100, w / 100, w / 100);
        iv.setCornerRadius(5.55f * w / 100f);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(iv, -1, -1);

        ivRemove = new ImageView(context);
        ivRemove.setImageResource(R.drawable.ic_remove);
        LayoutParams paramsImageRemove = new LayoutParams((int) (3.889f * w / 100), (int) (3.889f * w / 100));
        paramsImageRemove.addRule(ALIGN_PARENT_END, TRUE);
        paramsImageRemove.setMargins(0, (int) (1.11f * w / 100), (int) (1.11f * w / 100), 0);
        addView(ivRemove, paramsImageRemove);
    }

    public void setParam(){
        setLayoutParams(new LayoutParams(-1, (int) (41.667f * w / 100)));
    }

    public RoundedImageView getIv() {
        return iv;
    }

    public ImageView getIvRemove() {
        return ivRemove;
    }
}
