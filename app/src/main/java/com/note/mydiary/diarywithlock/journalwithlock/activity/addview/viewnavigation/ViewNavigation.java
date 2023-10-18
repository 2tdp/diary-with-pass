package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.ViewToolbar;

public class ViewNavigation extends LinearLayout {

    private final ViewToolbar viewToolbar;
    private final ViewMenuNavigation viewMenuNavigation;

    public ViewNavigation(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(getResources().getColor(R.color.white));

        int w = getResources().getDisplayMetrics().widthPixels;

        LayoutParams paramsToolbar = new LayoutParams(-1, -2);
        paramsToolbar.setMargins(0, w * 13 / 100, 0, 0);
        viewToolbar = new ViewToolbar(context);
        viewToolbar.getTvSave().setVisibility(GONE);
        addView(viewToolbar, paramsToolbar);

        ScrollView sv = new ScrollView(context);
        sv.setFillViewport(true);
        addView(sv, -1, -1);
        viewMenuNavigation = new ViewMenuNavigation(context);
        sv.addView(viewMenuNavigation, -1, -2);
    }

    public ViewToolbar getViewToolbar() {
        return viewToolbar;
    }

    public ViewMenuNavigation getViewMenu() {
        return viewMenuNavigation;
    }
}
