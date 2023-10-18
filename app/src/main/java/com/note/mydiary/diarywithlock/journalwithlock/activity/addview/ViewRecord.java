package com.note.mydiary.diarywithlock.journalwithlock.activity.addview;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

public class ViewRecord extends RelativeLayout {

    TextView tvTime;

    ViewItems viewPause, viewStop;
    ViewItemsClick viewRecord;

    LinearLayout linearMain;
    public int w;

    public ViewRecord(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;

        setBackgroundColor(getResources().getColor(R.color.background));
        linearMain = new LinearLayout(context);
        linearMain.setOrientation(LinearLayout.VERTICAL);

        tvTime = new TextView(context);
        tvTime.setText(getResources().getString(R.string.time_record));
        tvTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, 5.56f * w / 100);
        tvTime.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        tvTime.setTextColor(getResources().getColor(R.color.white));
        LinearLayout.LayoutParams paramsTextTime = new LinearLayout.LayoutParams(-2, -2);
        paramsTextTime.gravity = Gravity.CENTER_HORIZONTAL;
        linearMain.addView(tvTime, paramsTextTime);

        RelativeLayout relativeLayout = new RelativeLayout(context);
        viewPause = new ViewItems(context);
        viewPause.getImg().setImageResource(R.drawable.ic_pause);
        viewPause.getTv().setText(getResources().getString(R.string.pause));
        relativeLayout.addView(viewPause);

        viewStop = new ViewItems(context);
        viewStop.getImg().setImageResource(R.drawable.ic_stop);
        viewStop.getTv().setText(getResources().getString(R.string.stop));
        LayoutParams paramsViewStop = new LayoutParams(-2, -2);
        paramsViewStop.addRule(RelativeLayout.ALIGN_PARENT_END, TRUE);
        relativeLayout.addView(viewStop, paramsViewStop);

        LinearLayout.LayoutParams paramsOptionRecord = new LinearLayout.LayoutParams(-1, -2);
        paramsOptionRecord.setMargins(0, (int) (w * 8.33f / 100), 0, 0);
        linearMain.addView(relativeLayout, paramsOptionRecord);

        LayoutParams params2 = new LayoutParams((int) (44.44f * w / 100), -2);
        params2.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        addView(linearMain, params2);

        viewRecord = new ViewItemsClick(context);
        LayoutParams paramsRecord = new LayoutParams(-2, -2);
        paramsRecord.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        addView(viewRecord, paramsRecord);

        tvTime.setVisibility(GONE);
        linearMain.setVisibility(GONE);
    }

    public class ViewItems extends LinearLayout {

        ImageView img;
        TextView tv;

        public ViewItems(Context context) {
            super(context);
            setOrientation(VERTICAL);

            img = new ImageView(context);
            img.setImageResource(R.drawable.ic_record);
            addView(img, new LayoutParams((int) (w * 13.89f / 100), (int) (w * 13.89f / 100)));

            tv = new TextView(context);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 3.33f / 100);
            tv.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
            tv.setTextColor(getResources().getColor(R.color.white));
            LayoutParams paramsText = new LayoutParams(-2, -2);
            paramsText.gravity = Gravity.CENTER_HORIZONTAL;
            paramsText.setMargins(0, w * 3 / 100, 0, 0);
            addView(tv, paramsText);
        }

        public ImageView getImg() {
            return img;
        }

        public TextView getTv() {
            return tv;
        }
    }

    public class ViewItemsClick extends LinearLayout {

        ImageView img;

        public ViewItemsClick(Context context) {
            super(context);
            setOrientation(VERTICAL);

            img = new ImageView(context);
            img.setImageResource(R.drawable.ic_record);
            addView(img, new LayoutParams(w * 25 / 100, w * 25 / 100));

            TextView tv = new TextView(context);
            tv.setText(getResources().getString(R.string.tap_to_record));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 3.33f / 100);
            tv.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
            tv.setTextColor(getResources().getColor(R.color.white));
            LayoutParams params = new LayoutParams(-2, -2);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            params.setMargins(0, w * 3 / 100, 0, 0);
            addView(tv, params);
        }

        public ImageView getImg() {
            return img;
        }
    }

    public TextView getTvTime() {
        return tvTime;
    }

    public ViewItems getViewPause() {
        return viewPause;
    }

    public ViewItems getViewStop() {
        return viewStop;
    }

    public ViewItemsClick getViewRecord() {
        return viewRecord;
    }

    public LinearLayout getLinearMain() {
        return linearMain;
    }
}
