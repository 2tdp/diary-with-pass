package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.gesture.GestureOverlayView;
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
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

public class ViewsetSignature extends RelativeLayout {

    static int w;
    ViewToolbar viewToolbar;
    TextView tvError, tvTitle, tvClick, tvForgot;
    GestureOverlayView gestureOverlayView;

    public ViewsetSignature(Context context) {
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
        viewToolbar.setId(1228);
        viewToolbar.getIvVip().setVisibility(GONE);
        viewToolbar.getTvCancel().setVisibility(GONE);
        viewToolbar.getTvSave().setVisibility(GONE);
        viewToolbar.getTvSave().setText(getResources().getString(R.string.theme));
        viewToolbar.getTvSave().setBackgroundColor(getResources().getColor(R.color.white));
        viewToolbar.getTvSave().setTextColor(getResources().getColor(R.color.orange));
        viewToolbar.getTvTitle().setText(getResources().getText(R.string.signature));
        LayoutParams paramsToolbar = new LayoutParams(-1, -2);
        paramsToolbar.setMargins(0, (int) (w * 11.11f / 100), 0, 0);
        addView(viewToolbar, paramsToolbar);

        RelativeLayout rl = new RelativeLayout(context);
        rl.setId(1337);

        tvError = new TextView(context);
        tvError.setId(1229);
        tvError.setVisibility(INVISIBLE);
        tvError.setText(getResources().getString(R.string.error_signature));
        tvError.setTextColor(getResources().getColor(R.color.red));
        tvError.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
        tvError.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        LayoutParams paramsTextErr = new LayoutParams(-2, -2);
        paramsTextErr.addRule(CENTER_HORIZONTAL);
        rl.addView(tvError, paramsTextErr);

        tvTitle = new TextView(context);
        tvTitle.setId(1331);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
        tvTitle.setTextColor(getResources().getColor(R.color.black));
        tvTitle.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        LayoutParams paramsTextTitle = new LayoutParams(-2, -2);
        paramsTextTitle.addRule(BELOW, tvError.getId());
        paramsTextTitle.addRule(CENTER_HORIZONTAL, TRUE);
        paramsTextTitle.setMargins(0, 0, 0, (int) (6.667f * w / 100));
        rl.addView(tvTitle, paramsTextTitle);

        gestureOverlayView = new GestureOverlayView(context);
        gestureOverlayView.setBackgroundColor(getResources().getColor(R.color.gray_1));
        gestureOverlayView.setGestureStrokeType(GestureOverlayView.GESTURE_STROKE_TYPE_MULTIPLE);
        LayoutParams paramsSignature = new LayoutParams(-1, -1);
        paramsSignature.setMargins(0, (int) (5.556f * w / 100), 0, 0);
        paramsSignature.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        paramsSignature.addRule(CENTER_HORIZONTAL, TRUE);
        paramsSignature.addRule(BELOW, tvTitle.getId());
        rl.addView(gestureOverlayView, paramsSignature);

        LayoutParams paramsRl = new LayoutParams(-1, 110 * w / 100);
        paramsRl.addRule(CENTER_IN_PARENT, TRUE);
        addView(rl, paramsRl);

        tvForgot = new TextView(context);
        tvForgot.setVisibility(GONE);
        tvForgot.setText(Utils.underLine(getResources().getString(R.string.forgot_password)));
        tvForgot.setTextColor(getResources().getColor(R.color.red));
        tvForgot.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.389f * w / 100);
        tvForgot.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        LayoutParams paramsForgot = new LayoutParams(-2, -2);
        paramsForgot.setMargins(0, (int) (5.556f * w / 100), 0, 0);
        paramsForgot.addRule(BELOW, rl.getId());
        paramsForgot.addRule(CENTER_HORIZONTAL, TRUE);
        addView(tvForgot, paramsForgot);

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

    public TextView getTvForgot() {
        return tvForgot;
    }

    public GestureOverlayView getGestureOverlayView() {
        return gestureOverlayView;
    }
}
