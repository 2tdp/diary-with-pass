package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewtheme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.ViewToolbar;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

public class ViewPickThemeLock extends RelativeLayout {

    static int w;
    ViewToolbar viewToolbar;
    RecyclerView rcv;
    LinearLayout llNoInternet;

    @SuppressLint("ResourceType")
    public ViewPickThemeLock(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        setBackgroundColor(getResources().getColor(R.color.white));
        setClickable(true);
        setFocusable(true);

        viewToolbar = new ViewToolbar(context);
        viewToolbar.setId(1221);
        viewToolbar.getTvSave().setVisibility(GONE);
        viewToolbar.getIvVip().setVisibility(GONE);
        viewToolbar.getTvCancel().setVisibility(GONE);
        viewToolbar.getTvTitle().setText(getResources().getString(R.string.theme));
        LayoutParams paramsToolbar = new LayoutParams(-1, -2);
        paramsToolbar.setMargins(0, (int) (11.5f * w / 100), 0, 0);
        addView(viewToolbar, paramsToolbar);

        rcv = new RecyclerView(context);
        LayoutParams paramsRcv = new LayoutParams(-1, -1);
        paramsRcv.addRule(BELOW, viewToolbar.getId());
        paramsRcv.setMargins((int) (2.778f * w / 100), (int) (5.556f * w / 100), (int) (2.778f * w / 100), (int) (5.556f * w / 100));
        addView(rcv, paramsRcv);

        llNoInternet = new LinearLayout(context);
        llNoInternet.setOrientation(LinearLayout.VERTICAL);
        llNoInternet.setGravity(Gravity.CENTER);
        llNoInternet.setVisibility(GONE);
        llNoInternet.setBackgroundColor(Color.TRANSPARENT);

        ImageView iv = new ImageView(context);
        iv.setImageResource(R.drawable.im_no_internet);
        llNoInternet.addView(iv, (int) (30.556f * w / 100), (int) (30.556f * w / 100));

        TextView tvNoInternet = new TextView(context);
        tvNoInternet.setGravity(Gravity.CENTER);
        tvNoInternet.setText(getResources().getString(R.string.no_internet));
        tvNoInternet.setTextSize(TypedValue.COMPLEX_UNIT_PX, 5f * w / 100);
        tvNoInternet.setTextColor(getResources().getColor(R.color.gray_2));
        LayoutParams paramsTextNoInternet = new LayoutParams(-2, -2);
        paramsTextNoInternet.setMargins(0, (int) (4.889f * w / 100), 0, 0);
        llNoInternet.addView(tvNoInternet, paramsTextNoInternet);

        LayoutParams paramsNoInternet = new LayoutParams(-2, -2);
        paramsNoInternet.addRule(CENTER_IN_PARENT, TRUE);
        addView(llNoInternet, paramsNoInternet);
    }

    public void createTheme(Context context, String nameTheme) {
        if (nameTheme.equals("default")) return;

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

    public LinearLayout getLlNoInternet() {
        return llNoInternet;
    }
}
