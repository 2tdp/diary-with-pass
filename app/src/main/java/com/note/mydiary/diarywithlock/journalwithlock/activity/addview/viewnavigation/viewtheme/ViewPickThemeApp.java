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

import androidx.viewpager2.widget.ViewPager2;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.ViewToolbar;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

public class ViewPickThemeApp extends RelativeLayout {

    static int w;
    ViewToolbar viewToolbar;
    LinearLayout ll, llNoInternet;
    TextView tvClick;
    ImageView ivUpgrade;
    ViewPager2 viewPager2;

    @SuppressLint("ResourceType")
    public ViewPickThemeApp(Context context) {
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
        viewToolbar.getTvTitle().setText(getResources().getString(R.string.choose_style));
        LinearLayout.LayoutParams paramsToolbar = new LinearLayout.LayoutParams(-1, -2);
        paramsToolbar.setMargins(0, (int) (11.5f * w / 100), 0, 0);
        addView(viewToolbar, paramsToolbar);

        ll = new LinearLayout(context);
        ll.setId(1222);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setGravity(Gravity.CENTER);
        ll.setBackgroundResource(R.drawable.border_orange);

        ivUpgrade = new ImageView(context);
        ivUpgrade.setImageResource(R.drawable.ic_upgrade);
        LinearLayout.LayoutParams paramsImage = new LinearLayout.LayoutParams((int) (5.556f * w / 100), (int) (4.167f * w / 100));
        ll.addView(ivUpgrade, paramsImage);

        tvClick = new TextView(context);
        tvClick.setText(getResources().getString(R.string.download));
        tvClick.setTextColor(getResources().getColor(R.color.white));
        tvClick.setTextSize(TypedValue.COMPLEX_UNIT_PX, 5f * w / 100);
        tvClick.setTypeface(Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context));
        LinearLayout.LayoutParams paramsClick = new LinearLayout.LayoutParams(-2, -2);
        paramsClick.setMargins((int) (2.778f * w / 100), 0, 0, 0);
        ll.addView(tvClick, paramsClick);

        LayoutParams params = new LayoutParams(-1, (int) (13.889f * w / 100));
        params.addRule(CENTER_HORIZONTAL, TRUE);
        params.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        params.setMargins((int) (11.11f * w / 100), 0, (int) (11.11f * w / 100), (int) (11.11f * w / 100));
        addView(ll, params);

        viewPager2 = new ViewPager2(context);
        LayoutParams paramsPage = new LayoutParams(-1, -1);
        paramsPage.setMargins(0, (int) (5.556f * w / 100), 0, (int) (5.556f * w / 100));
        paramsPage.addRule(BELOW, viewToolbar.getId());
        paramsPage.addRule(ABOVE, ll.getId());
        paramsPage.addRule(CENTER_HORIZONTAL, TRUE);
        addView(viewPager2, paramsPage);

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

        ll.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorMain())}, (int) (2.5f * w / 100), -1, -1));
    }

    public ViewToolbar getViewToolbar() {
        return viewToolbar;
    }

    public LinearLayout getLlNoInternet() {
        return llNoInternet;
    }

    public LinearLayout getLl() {
        return ll;
    }

    public TextView getTvClick() {
        return tvClick;
    }

    public ImageView getIvUpgrade() {
        return ivUpgrade;
    }

    public ViewPager2 getViewPager2() {
        return viewPager2;
    }
}
