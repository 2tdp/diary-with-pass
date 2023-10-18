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

public class ViewDialogTextDiary extends LinearLayout {

    int w;
    TextView tvTitle, tvContent;
    ViewYesNo viewYesNo;

    public ViewDialogTextDiary(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        setOrientation(VERTICAL);
        setBackgroundResource(R.drawable.boder_dialog);
        setPadding(0, (int) (4.72f * w / 100), 0, (int) (4.72f * w / 100));
        init(context);
    }

    private void init(Context context) {
        tvTitle = new TextView(context);
        tvTitle.setText(getResources().getString(R.string.save_your_diary));
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setTextColor(getResources().getColor(R.color.black));
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.89f * w / 100);
        tvTitle.setTypeface(Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context));
        LayoutParams paramsTitle = new LayoutParams(-2, -2);
        paramsTitle.gravity = Gravity.CENTER_HORIZONTAL;
        addView(tvTitle, paramsTitle);

        tvContent = new TextView(context);
        tvContent.setText(getResources().getString(R.string.would_you_like_to_save_this_diary));
        tvContent.setGravity(Gravity.CENTER);
        tvContent.setTextColor(getResources().getColor(R.color.black));
        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
        tvContent.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        LayoutParams paramsContent = new LayoutParams(-2, -2);
        paramsContent.gravity = Gravity.CENTER_HORIZONTAL;
        addView(tvContent, paramsContent);

        viewYesNo = new ViewYesNo(context);
        LayoutParams paramsYesNo = new LayoutParams(-1, (int) (11.11f * w / 100));
        paramsYesNo.setMargins((int) (5.83f * w / 100), (int) (5.83f * w / 100), (int) (5.83f * w / 100), 0);
        addView(viewYesNo, paramsYesNo);

        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")){
            String jsonConfig = Utils.readFromFile(context, "theme/theme_app/theme_app" + DataLocalManager.getOption(Constant.THEME_APP) + "/config.txt");

            ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);
            if (config == null) return;

            tvTitle.setTextColor(Color.parseColor(config.getColorIcon()));
            tvContent.setTextColor(Color.parseColor(config.getColorIcon()));
        }
    }

    public ViewYesNo getViewYesNo() {
        return viewYesNo;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public TextView getTvContent() {
        return tvContent;
    }
}
