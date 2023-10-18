package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewdialog;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

public class ViewYesNo extends LinearLayout {

    int w;
    TextView tvNo, tvYes;

    public ViewYesNo(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        setOrientation(HORIZONTAL);
        init(context);
    }

    private void init(Context context) {
        tvNo = new TextView(context);
        tvNo.setText(getResources().getString(R.string.no));
        tvNo.setTextColor(getResources().getColor(R.color.gray_2));
        tvNo.setTypeface(Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context));
        tvNo.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.389f * w / 100);
        tvNo.setBackgroundResource(R.drawable.border_gray_1);
        tvNo.setGravity(Gravity.CENTER);
        LayoutParams paramsTextNo = new LayoutParams(0, -1, 1);
        paramsTextNo.setMargins(0, 0, (int) (2.5f * w / 100), 0);
        addView(tvNo, paramsTextNo);

        tvYes = new TextView(context);
        tvYes.setText(getResources().getString(R.string.yes));
        tvYes.setTextColor(getResources().getColor(R.color.white));
        tvYes.setTypeface(Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context));
        tvYes.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.389f * w / 100);
        tvYes.setBackgroundResource(R.drawable.border_orange);
        tvYes.setGravity(Gravity.CENTER);
        LayoutParams paramsTextYes = new LayoutParams(0, -1, 1);
        paramsTextYes.setMargins((int) (2.5f * w / 100), 0, 0, 0);
        addView(tvYes, paramsTextYes);

        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
            String jsonConfig = Utils.readFromFile(context, "theme/theme_app/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP) + "/config.txt");

            ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);
            if (config == null) return;

            tvYes.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorMain())}, (int) (2.5f * w / 100), -1, -1));
        }
    }

    public TextView getTvNo() {
        return tvNo;
    }

    public TextView getTvYes() {
        return tvYes;
    }
}