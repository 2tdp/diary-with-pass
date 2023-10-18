package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewhome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.ViewAnim;
import com.note.mydiary.diarywithlock.journalwithlock.customview.CustomCalendar.CustomCalendarView;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

import java.util.Calendar;

public class ViewHomeDiaryInDay extends RelativeLayout {

    static int w;
    CustomCalendarView calendarView;
    TextView tv;
    ViewAnim vNoData;
    RecyclerView rcvDiary;

    public ViewHomeDiaryInDay(Context context) {
        super(context);
        setBackgroundColor(getResources().getColor(R.color.white));
        setClickable(true);
        setFocusable(true);
        w = getResources().getDisplayMetrics().widthPixels;

        init(context);
    }

    @SuppressLint("ResourceType")
    private void init(Context context) {
        calendarView = new CustomCalendarView(context);
        calendarView.setId(3456);
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        calendarView.setShowOverflowDate(false);
//        calendarView.refreshCalendar(Calendar.getInstance(Constant.LOCALE_DEFAULT));
        LayoutParams paramsCalendar = new LayoutParams(-1, (int) (78.5f * w / 100));
        paramsCalendar.setMargins((int) (5.56f * w / 100), (int) (5.56f * w / 100), (int) (5.56f * w / 100), 0);
        paramsCalendar.addRule(ALIGN_PARENT_TOP, TRUE);

        addView(calendarView, paramsCalendar);

        tv = new TextView(context);
        tv.setText(getResources().getString(R.string.recent_memories));
        tv.setId(3457);
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 4.44f * w / 100);
        tv.setTypeface(Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context));
        LayoutParams paramsText = new LayoutParams(-2, -2);
        paramsText.addRule(BELOW, calendarView.getId());
        paramsText.setMargins((int) (5.556f * w / 100), (int) (2.778f * w / 100), 0, 0);
        addView(tv, paramsText);

        vNoData = new ViewAnim(context);
        vNoData.setText(getResources().getString(R.string.tap_to_start_your_first_entry));
        vNoData.setColor(getResources().getColor(R.color.orange));
        LayoutParams paramsNoData = new LayoutParams((int) (45.833f * w / 100), (int) (28.879f * w / 100));
        paramsNoData.addRule(CENTER_HORIZONTAL, TRUE);
        paramsNoData.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        addView(vNoData, paramsNoData);

        rcvDiary = new RecyclerView(context);
        LayoutParams paramsRcv = new LayoutParams(-1, -1);
        paramsRcv.addRule(BELOW, tv.getId());
        paramsRcv.setMargins((int) (5.556f * w / 100), (int) (2.778f * w / 100), (int) (5.556f * w / 100), (int) (2.278f * w / 100));
        addView(rcvDiary, paramsRcv);
    }

    public void setParams() {
        LayoutParams params = new LayoutParams(-1, -1);
        params.setMargins(0, (int) (20f * w / 100), 0, 25 * w / 100);
        this.setLayoutParams(params);
    }

    public void createTheme(Context context, String nameTheme) {
        if (nameTheme.equals("default")) return;

        String jsonConfig = Utils.readFromFile(context, "theme/theme_app/" + nameTheme + "/config.txt");

        ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

        if (config == null) return;

        setBackgroundColor(Color.parseColor("#00000000"));

        if (!config.isBackgroundImage())
            calendarView.setThemeBackgroundColor(
                    Color.parseColor("#00000000"),
                    Color.parseColor(config.getColorTextDate()),
                    Color.parseColor(config.getColorIcon()),
                    Utils.createBackground(new int[]{Color.parseColor(config.getColorMain())}, (int) (1.5f * w / 100), -1, -1),
                    Utils.createBackground(new int[]{Color.parseColor(config.getColorDiaryCalendar())}, (int) (5f * w / 100), -1, -1));
        else
            calendarView.setThemeBackgroundColor(
                    Color.parseColor("#00000000"),
                    Color.parseColor(config.getColorIcon()),
                    Color.parseColor(config.getColorIconMore()),
                    Utils.createBackground(new int[]{Color.parseColor(config.getColorIcon())}, (int) (1.5f * w / 100), -1, -1),
                    Utils.createBackground(new int[]{Color.parseColor(config.getColorIcon())}, (int) (5f * w / 100), -1, -1));

        if (!config.isBackgroundImage()) vNoData.setColor(Color.parseColor(config.getColorMain()));
        else vNoData.setColor(Color.parseColor(config.getColorIcon()));

        tv.setTextColor(Color.parseColor(config.getColorIcon()));
    }

    public ViewAnim getvNoData() {
        return vNoData;
    }

    public CustomCalendarView getCalendarView() {
        return calendarView;
    }

    public RecyclerView getRcvDiary() {
        return rcvDiary;
    }
}
