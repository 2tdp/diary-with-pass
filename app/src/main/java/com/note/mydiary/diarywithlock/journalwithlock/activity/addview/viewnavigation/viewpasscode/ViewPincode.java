package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.ViewToolbar;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.pinlockview.IndicatorDots;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.pinlockview.PinLockView;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsBitmap;

public class ViewPincode extends RelativeLayout {

    static int w;
    ViewToolbar viewToolbar;
    TextView tvError, tvTitle, tvClick;
    IndicatorDots indicatorDots;
    PinLockView pinLockView;

    public ViewPincode(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        setBackgroundColor(getResources().getColor(R.color.white));
        setClickable(true);
        setFocusable(true);
        init(context);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "ResourceType"})
    private void init(Context context) {
        viewToolbar = new ViewToolbar(context);
        viewToolbar.setId(1223);
        viewToolbar.getIvVip().setVisibility(GONE);
        viewToolbar.getTvCancel().setVisibility(GONE);
        viewToolbar.getTvSave().setVisibility(GONE);
        viewToolbar.getTvSave().setText(getResources().getString(R.string.theme));
        viewToolbar.getTvSave().setBackgroundColor(getResources().getColor(R.color.white));
        viewToolbar.getTvSave().setTextColor(getResources().getColor(R.color.orange));
        viewToolbar.getTvTitle().setText(getResources().getText(R.string.pincode));
        LayoutParams paramsToolbar = new LayoutParams(-1, -2);
        paramsToolbar.setMargins(0, (int) (w * 11.11f / 100), 0, 0);
        addView(viewToolbar, paramsToolbar);

        RelativeLayout rl = new RelativeLayout(context);

        pinLockView = new PinLockView(context);
        pinLockView.setId(1227);

        tvError = new TextView(context);
        tvError.setId(1224);
        tvError.setVisibility(INVISIBLE);
        tvError.setText(getResources().getString(R.string.error_pincode));
        tvError.setTextColor(getResources().getColor(R.color.red));
        tvError.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
        tvError.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        LayoutParams paramsTextErr = new LayoutParams(-2, -2);
        paramsTextErr.addRule(CENTER_HORIZONTAL);
        rl.addView(tvError, paramsTextErr);

        tvTitle = new TextView(context);
        tvTitle.setId(1225);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
        tvTitle.setTextColor(getResources().getColor(R.color.black));
        tvTitle.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        LayoutParams paramsTextTitle = new LayoutParams(-2, -2);
        paramsTextTitle.addRule(BELOW, tvError.getId());
        paramsTextTitle.addRule(CENTER_HORIZONTAL, TRUE);
        paramsTextTitle.setMargins(0, 0, 0, (int) (3.389f * w / 100));
        rl.addView(tvTitle, paramsTextTitle);

        indicatorDots = new IndicatorDots(context);
        indicatorDots.setId(1226);
        LayoutParams paramsDot = new LayoutParams(-2, -2);
        paramsDot.addRule(BELOW, tvTitle.getId());
        paramsDot.addRule(ABOVE, pinLockView.getId());
        paramsDot.addRule(CENTER_HORIZONTAL, TRUE);
        paramsDot.setMargins(0, (int) (2.789f * w / 100), 0, 0);
        rl.addView(indicatorDots, paramsDot);

        pinLockView.attachIndicatorDots(indicatorDots);
        LayoutParams paramsPincode = new LayoutParams(-1, (int) (98f * w / 100));
        paramsPincode.setMargins((int) (11.11f * w / 100), 0, (int) (11.11f * w / 100), 0);
        paramsPincode.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        paramsPincode.addRule(CENTER_HORIZONTAL, TRUE);
        rl.addView(pinLockView, paramsPincode);

        LayoutParams paramsRl = new LayoutParams(-1, (int) (134f * w / 100));
        paramsRl.addRule(CENTER_IN_PARENT, TRUE);
        addView(rl, paramsRl);

        tvClick = new TextView(context);
        tvClick.setPadding(2 * w / 100, 2 * w / 100, 2 * w / 100, 2 * w / 100);
        tvClick.setGravity(Gravity.CENTER);
        tvClick.setTextColor(getResources().getColor(R.color.white));
        tvClick.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.889f * w / 100);
        tvClick.setTypeface(Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context));
        tvClick.setBackgroundResource(R.drawable.border_orange);
        LayoutParams paramsTextClick = new LayoutParams(-1, -2);
        paramsTextClick.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        paramsTextClick.setMargins((int) (22.22f * w / 100), 0, (int) (22.22f * w / 100), (int) (11.11f * w / 100));
        addView(tvClick, paramsTextClick);
    }

    public void createThemeApp(Context context, String nameTheme) {
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

        tvTitle.setTextColor(Color.parseColor(config.getColorIcon()));

        BitmapDrawable drawableFill = new BitmapDrawable(getResources(), UtilsBitmap.getBitmapFromVectorDrawable(context, R.drawable.dot_filled_default));
        BitmapDrawable drawableEmpty = new BitmapDrawable(getResources(), UtilsBitmap.getBitmapFromVectorDrawable(context, R.drawable.dot_empty_default));
        drawableEmpty.setTint(Color.parseColor(config.getColorIcon()));
        drawableFill.setTint(Color.parseColor(config.getColorMain()));
        indicatorDots.setDotDrawable(drawableEmpty, drawableFill);

//        pinLockView.setTextColor(Color.parseColor(config.getColorIcon()));
        tvClick.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorMain())}, (int) (2.5f * w / 100), -1, -1));
    }

    public ViewToolbar getViewToolbar() {
        return viewToolbar;
    }

    public TextView getTvError() {
        return tvError;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public TextView getTvClick() {
        return tvClick;
    }

    public IndicatorDots getIndicatorDots() {
        return indicatorDots;
    }

    public PinLockView getPinLockView() {
        return pinLockView;
    }
}
