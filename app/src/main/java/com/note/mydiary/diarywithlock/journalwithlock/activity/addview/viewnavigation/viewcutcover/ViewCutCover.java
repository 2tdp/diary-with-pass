package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewcutcover;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.ViewToolbar;
import com.note.mydiary.diarywithlock.journalwithlock.customview.CropRatioView;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

public class ViewCutCover extends RelativeLayout {

    static int w;
    ViewToolbar viewToolbar;
    CropRatioView cropRatioView;
    TextView tv;

    public ViewCutCover(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        setBackgroundColor(getResources().getColor(R.color.white));
        setClickable(true);
        setFocusable(true);
        init(context);
    }

    @SuppressLint("ResourceType")
    private void init(Context context) {
        viewToolbar = new ViewToolbar(context);
        viewToolbar.setId(1221);
        viewToolbar.getIvVip().setVisibility(GONE);
        viewToolbar.getTvSave().setVisibility(GONE);
        viewToolbar.getTvCancel().setVisibility(GONE);
        viewToolbar.getTvTitle().setText(getResources().getString(R.string.crop));
        LinearLayout.LayoutParams paramsToolbar = new LinearLayout.LayoutParams(-1, -2);
        paramsToolbar.setMargins(0, (int) (w * 11.11f / 100), 0, 0);
        addView(viewToolbar, paramsToolbar);

        tv = new TextView(context);
        tv.setId(1222);
        tv.setText(getResources().getString(R.string.confirm));
        tv.setPadding(2 * w / 100, 2 * w / 100, 2 * w / 100, 2 * w / 100);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(getResources().getColor(R.color.white));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.889f * w / 100);
        tv.setTypeface(Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context));
        tv.setBackgroundResource(R.drawable.border_orange);
        LayoutParams paramsTextClick = new LayoutParams(-1, -2);
        paramsTextClick.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        paramsTextClick.setMargins((int) (22.22f * w / 100), 0, (int) (22.22f * w / 100), (int) (5.556f * w / 100));
        addView(tv, paramsTextClick);

        cropRatioView = new CropRatioView(context);
        LayoutParams paramsCrop = new LayoutParams(-1, -1);
        paramsCrop.addRule(CENTER_IN_PARENT, TRUE);
        paramsCrop.addRule(ABOVE, tv.getId());
        paramsCrop.addRule(BELOW, viewToolbar.getId());
        paramsCrop.setMargins(0, (int) (8.33f * w / 100), 0, (int) (8.33f * w / 100));
        addView(cropRatioView, paramsCrop);
    }

    public void createThemeApp(Context context, String nameTheme) {
        String jsonConfig = Utils.readFromFile(context, "theme/theme_app/" + nameTheme + "/config.txt");

        ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

        if (config == null) return;

        tv.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorMain())}, (int) (2.5f * w / 100), -1, -1));
    }

    public ViewToolbar getViewToolbar() {
        return viewToolbar;
    }

    public CropRatioView getCropRatioView() {
        return cropRatioView;
    }

    public TextView getTv() {
        return tv;
    }
}
