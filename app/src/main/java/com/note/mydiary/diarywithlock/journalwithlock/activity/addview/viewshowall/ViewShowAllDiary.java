package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewshowall;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.ViewToolbar;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

public class ViewShowAllDiary extends LinearLayout {

    static int w;
    ViewToolbar viewToolbar;
    RecyclerView rcv;

    public ViewShowAllDiary(Context context) {
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
        RelativeLayout.LayoutParams paramsToolbar = new RelativeLayout.LayoutParams(-1, -2);
        paramsToolbar.setMargins(0, (int) (11.11f * w / 100), 0, 0);
        addView(viewToolbar, paramsToolbar);

        rcv = new RecyclerView(context);
        LayoutParams paramsRcv = new LayoutParams(-1, -1);
        paramsRcv.setMargins(0, (int) (5.556f * w / 100), 0, 0);
        addView(rcv, paramsRcv);
    }

    public void createThemeApp(Context context, String nameTheme){
        String jsonConfig = Utils.readFromFile(context, "theme/theme_app/" + nameTheme + "/config.txt");

        ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

        if (config == null) return;

        if (config.isBackgroundColor())
            setBackgroundColor(Color.parseColor(config.getColorBackground()));
        else if (config.isBackgroundGradient())
            setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorBackground()),
                    Color.parseColor(config.getColorBackgroundGradient())}, -1, -1, -1));
        else {
            Bitmap bmBg = BitmapFactory.decodeFile(Utils.getStore(context) + "/theme/theme_app/" + nameTheme + "/background.png");
            if (bmBg != null)
                setBackground(new BitmapDrawable(getResources(), bmBg));
        }
    }

    public ViewToolbar getViewToolbar() {
        return viewToolbar;
    }

    public RecyclerView getRcv() {
        return rcv;
    }
}
