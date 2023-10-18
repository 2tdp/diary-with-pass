package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewhome;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.ViewContent;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

public class ViewItemDiaryHome extends LinearLayout {

    static int w;
    TextView tvDateTime, tvSeeAll;
    RelativeLayout rlTop;
    ViewContent viewContent;

    public ViewItemDiaryHome(Context context) {
        super(context);
        setOrientation(VERTICAL);
        w = getResources().getDisplayMetrics().widthPixels;
        init(context);
    }

    private void init(Context context) {
        rlTop = new RelativeLayout(context);

        tvDateTime = new TextView(context);
        tvDateTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.889f * w / 100);
        tvDateTime.setTextColor(getResources().getColor(R.color.black));
        tvDateTime.setTypeface(Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context));
        RelativeLayout.LayoutParams paramsTextDateTime = new RelativeLayout.LayoutParams(-2, -2);
        paramsTextDateTime.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        rlTop.addView(tvDateTime, paramsTextDateTime);

        tvSeeAll = new TextView(context);
        tvSeeAll.setText(getResources().getString(R.string.see_all));
        tvSeeAll.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
        tvSeeAll.setTextColor(getResources().getColor(R.color.text_date));
        tvSeeAll.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        RelativeLayout.LayoutParams paramsTextSeeAll = new RelativeLayout.LayoutParams(-2, -2);
        paramsTextSeeAll.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTextSeeAll.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        rlTop.addView(tvSeeAll, paramsTextSeeAll);

        LayoutParams paramsLLTOp = new LayoutParams(-1, (int) (5.556f * w / 100));
        paramsLLTOp.setMargins(0, (int) (2.778f * w / 100), 0, 0);
        addView(rlTop, paramsLLTOp);

        viewContent = new ViewContent(context);
        addView(viewContent);
        viewContent.setParams();
    }

    public void setParams() {
        LayoutParams paramsContent = new LayoutParams(-1, -2);
        paramsContent.setMargins((int) (5.556f * w / 100), (int) (2.778f * w / 100), (int) (5.556f * w / 100), 0);
        this.setLayoutParams(paramsContent);
    }

    public void createTheme(Context context, String nameTheme) {
        if (nameTheme.equals("default")) return;

        String jsonConfig = Utils.readFromFile(context, "theme/theme_app/" + nameTheme + "/config.txt");

        ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

        if (config == null) return;

        tvDateTime.setTextColor(Color.parseColor(config.getColorIcon()));
        tvSeeAll.setTextColor(Color.parseColor(config.getColorTextDate()));
    }

    public TextView getTvDateTime() {
        return tvDateTime;
    }

    public TextView getTvSeeAll() {
        return tvSeeAll;
    }

    public RelativeLayout getRlTop() {
        return rlTop;
    }

    public ViewContent getViewContent() {
        return viewContent;
    }
}
