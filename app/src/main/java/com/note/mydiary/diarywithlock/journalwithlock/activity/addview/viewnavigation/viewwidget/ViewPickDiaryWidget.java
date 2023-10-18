package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewwidget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.ViewToolbar;

public class ViewPickDiaryWidget extends LinearLayout {

    static int w;
    ViewToolbar viewToolbar;
    RecyclerView rcv;

    public ViewPickDiaryWidget(Context context) {
        super(context);
        setOrientation(VERTICAL);
        setBackgroundColor(getResources().getColor(R.color.white));
        setClickable(true);
        setFocusable(true);
        w = getResources().getDisplayMetrics().widthPixels;
        init(context);
    }

    @SuppressLint("ResourceType")
    private void init(Context context) {
        viewToolbar = new ViewToolbar(context);
        viewToolbar.setId(1338);
        viewToolbar.getIvVip().setVisibility(GONE);
        viewToolbar.getTvSave().setVisibility(GONE);
        viewToolbar.getTvTitle().setText(getResources().getString(R.string.choose_widget));
        LayoutParams paramsToolbar = new LayoutParams(-1, -2);
        paramsToolbar.setMargins(0, (int) (11.11f * w / 100), 0, 0);
        addView(viewToolbar, paramsToolbar);

        rcv = new RecyclerView(context);
        LayoutParams paramsRcv = new LayoutParams(-1, -1);
        paramsRcv.setMargins((int) (5.556f * w / 100), (int) (5.556f * w / 100), (int) (5.556f * w / 100), (int) (5.556f * w / 100));
        addView(rcv, paramsRcv);
    }

    public ViewToolbar getViewToolbar() {
        return viewToolbar;
    }

    public RecyclerView getRcv() {
        return rcv;
    }
}
