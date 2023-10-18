package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewhome;

import android.content.Context;
import android.graphics.Color;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.ViewAnim;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

public class ViewHomeAllDiary extends RelativeLayout {

    static int w;
    ViewAnim vNoData;
    RecyclerView rcvDiary;

    public ViewHomeAllDiary(Context context) {
        super(context);
        setBackgroundColor(getResources().getColor(R.color.trans));
        setClickable(true);
        setFocusable(true);
        w = getResources().getDisplayMetrics().widthPixels;

        vNoData = new ViewAnim(context);
        vNoData.setText(getResources().getString(R.string.tap_to_start_your_first_entry));
        vNoData.setColor(getResources().getColor(R.color.orange));
        LayoutParams paramsNoData = new LayoutParams((int) (45.833f * w / 100), (int) (28.879f * w / 100));
        paramsNoData.addRule(CENTER_HORIZONTAL, TRUE);
        paramsNoData.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        addView(vNoData, paramsNoData);

        rcvDiary = new RecyclerView(context);
        addView(rcvDiary, new LayoutParams(-1, -1));
    }

    public void setParams() {
        LayoutParams params = new LayoutParams(-1, -1);
        params.setMargins(0, 65 * w / 100, 0, 25 * w / 100);
        this.setLayoutParams(params);
    }

    public void createTheme(Context context, String nameTheme) {
        if (nameTheme.equals("default")) return;

        String jsonConfig = Utils.readFromFile(context, "theme/theme_app/" + nameTheme + "/config.txt");

        ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

        if (config == null) return;

        if (!config.isBackgroundImage()) vNoData.setColor(Color.parseColor(config.getColorMain()));
        else vNoData.setColor(Color.parseColor(config.getColorIcon()));
    }

    public ViewAnim getvNoData() {
        return vNoData;
    }

    public RecyclerView getRcvDiary() {
        return rcvDiary;
    }
}
