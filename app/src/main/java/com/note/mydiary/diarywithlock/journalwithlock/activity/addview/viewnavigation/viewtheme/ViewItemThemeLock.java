package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewtheme;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.note.mydiary.diarywithlock.journalwithlock.R;

public class ViewItemThemeLock extends RelativeLayout {

    static int w;
    ImageView iv, ivNotSuitable, ivChoose, ivRule;

    public ViewItemThemeLock(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        setBackgroundColor(getResources().getColor(R.color.white));

        LayoutParams paramsImage = new LayoutParams(-1, -1);
        iv = new ImageView(context);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(iv, paramsImage);

        ivNotSuitable = new ImageView(context);
        ivNotSuitable.setVisibility(GONE);
        ivNotSuitable.setImageResource(R.drawable.ic_not_suitable);
        ivNotSuitable.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(ivNotSuitable, paramsImage);

        ivChoose = new ImageView(context);
        ivChoose.setImageResource(R.drawable.ic_choose);
        LayoutParams paramsImageChoose = new LayoutParams((int) (6.667f * w / 100), (int) (6.667f * w / 100));
        paramsImageChoose.addRule(ALIGN_PARENT_END, TRUE);
        addView(ivChoose, paramsImageChoose);

        ivRule = new ImageView(context);
        LayoutParams paramsImageRule = new LayoutParams((int) (16.667f * w / 100), (int) (16.667f * w / 100));
        paramsImageRule.addRule(ALIGN_PARENT_END, TRUE);
        addView(ivRule, paramsImageRule);
    }

    public ImageView getIv() {
        return iv;
    }

    public ImageView getIvNotSuitable() {
        return ivNotSuitable;
    }

    public ImageView getIvChoose() {
        return ivChoose;
    }

    public ImageView getIvRule() {
        return ivRule;
    }
}
