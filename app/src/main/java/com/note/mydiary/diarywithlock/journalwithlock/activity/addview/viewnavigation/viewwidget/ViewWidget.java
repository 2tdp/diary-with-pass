package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewwidget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.ViewToolbar;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

public class ViewWidget extends LinearLayout {

    static int w;
    ViewToolbar viewToolbar;
    ImageView ivWidget1, ivWidget2, ivWidget3;

    public ViewWidget(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        setBackgroundColor(getResources().getColor(R.color.white));
        setOrientation(VERTICAL);
        setClickable(true);
        setFocusable(true);
        init(context);
    }

    private void init(Context context) {
        viewToolbar = new ViewToolbar(context);
        viewToolbar.getTvTitle().setText(getResources().getString(R.string.menu_4));
        viewToolbar.getTvSave().setVisibility(GONE);
        viewToolbar.getIvVip().setVisibility(GONE);
        LayoutParams paramsToolbar = new LayoutParams(-1, -2);
        paramsToolbar.setMargins(0, (int) (w * 11.556f / 100), 0, 0);
        addView(viewToolbar, paramsToolbar);

        RelativeLayout rlSwitch = new RelativeLayout(context);

        ivWidget1 = new ImageView(context);
        Glide.with(context)
                .load(R.drawable.im_widget_1)
                .into(ivWidget1);
        RelativeLayout.LayoutParams paramsWidget1 = new RelativeLayout.LayoutParams((int) (41.125f * w / 100), -1);
        paramsWidget1.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        rlSwitch.addView(ivWidget1, paramsWidget1);

        ivWidget2 = new ImageView(context);
        Glide.with(context)
                .load(R.drawable.im_widget_2)
                .into(ivWidget2);
        RelativeLayout.LayoutParams paramsWidget2 = new RelativeLayout.LayoutParams((int) (41.125f * w / 100), -1);
        paramsWidget2.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        rlSwitch.addView(ivWidget2, paramsWidget2);

        LayoutParams paramsLlSwitch = new LayoutParams(-1, (int) (41.125f * w / 100));
        paramsLlSwitch.setMargins((int) (5.556f * w / 100), (int) (4.556f * w / 100), (int) (5.556f * w / 100), 0);
        addView(rlSwitch, paramsLlSwitch);

        ivWidget3 = new ImageView(context);
        Glide.with(context)
                .load(R.drawable.im_widget_3)
                .into(ivWidget3);
        LayoutParams paramsImage = new LayoutParams(-1, (int) (41.667f * w / 100));
        paramsImage.setMargins((int) (5.556f * w / 100), (int) (5.556f * w / 100), (int) (5.556f * w / 100), 0);
        addView(ivWidget3, paramsImage);
    }

    public void createThemeApp(Context context, String nameTheme) {
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

    public ImageView getIvWidget1() {
        return ivWidget1;
    }

    public ImageView getIvWidget2() {
        return ivWidget2;
    }

    public ImageView getIvWidget3() {
        return ivWidget3;
    }
}
