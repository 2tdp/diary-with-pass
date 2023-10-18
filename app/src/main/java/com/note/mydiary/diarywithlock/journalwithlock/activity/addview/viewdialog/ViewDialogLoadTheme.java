package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewdialog;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.customview.CustomSeekbarDownload;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

public class ViewDialogLoadTheme extends LinearLayout {

    static int w;
    CustomSeekbarDownload customSeekbarDownload;
    ImageView iv;
    TextView tv;

    public ViewDialogLoadTheme(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        setBackgroundResource(R.drawable.boder_dialog);
        setOrientation(VERTICAL);
        setPadding((int) (5.556f * w / 100), (int) (5.556f * w / 100), (int) (5.556f * w / 100), 0);

        iv = new ImageView(context);
        LayoutParams paramsImage = new LayoutParams((int) (77.78f * w / 100), (int) (111.11f * w / 100));
        paramsImage.gravity = Gravity.CENTER_HORIZONTAL;
        paramsImage.setMargins(0, (int) (3.389f * w / 100), 0, 0);
        addView(iv, paramsImage);

        tv = new TextView(context);
        tv.setText(getResources().getString(R.string.download));
        tv.setPadding(2 * w / 100, 2 * w / 100, 2 * w / 100, 2 * w / 100);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(getResources().getColor(R.color.white));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.889f * w / 100);
        tv.setTypeface(Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context));
        tv.setBackgroundResource(R.drawable.border_orange);
        LayoutParams paramsTextClick = new LayoutParams(-1, -2);
        paramsTextClick.gravity = Gravity.CENTER_HORIZONTAL;
        paramsTextClick.setMargins((int) (5.556f * w / 100), (int) (5.556f * w / 100), (int) (5.556f * w / 100), (int) (5.556f * w / 100));
        addView(tv, paramsTextClick);

        customSeekbarDownload = new CustomSeekbarDownload(context);
        customSeekbarDownload.setVisibility(GONE);
        customSeekbarDownload.setColorStroke(getResources().getColor(R.color.selectedDay));
        customSeekbarDownload.setColorPro(getResources().getColor(R.color.selectedDay));
        customSeekbarDownload.setColorText(getResources().getColor(R.color.white));
        customSeekbarDownload.setTextSize(3.389f * w / 100);
        customSeekbarDownload.setSizeStroke(w / 100f);
        customSeekbarDownload.setProgress(0);
        LayoutParams paramsDownload = new LayoutParams(-1, (int) (11.11f * w / 100));
        paramsDownload.setMargins((int) (5.556f * w / 100), (int) (5.556f * w / 100), (int) (5.556f * w / 100), (int) (5.556f * w / 100));
        addView(customSeekbarDownload, paramsDownload);

        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
            String jsonConfig = Utils.readFromFile(context, "theme/theme_app/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP) + "/config.txt");

            ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

            if (config == null) return;

            tv.setTextColor(Color.parseColor(config.getColorIcon()));
            tv.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorMain())}, (int) (2.5f * w / 100), -1, -1));
        }
    }

    public CustomSeekbarDownload getCustomSeekbarDownload() {
        return customSeekbarDownload;
    }

    public ImageView getIv() {
        return iv;
    }

    public TextView getTv() {
        return tv;
    }
}
