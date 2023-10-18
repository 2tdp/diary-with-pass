package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewtheme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.pinlockview.IndicatorDots;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.pinlockview.PinLockView;
import com.note.mydiary.diarywithlock.journalwithlock.data.DataThemePincode;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigPinThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsBitmap;

import java.io.File;

public class ViewThemePinLock extends RelativeLayout {

    static int w;
    ImageView ivLock;
    TextView tvTitle, tvForgot;
    IndicatorDots indicatorDots;
    PinLockView pinLockView;

    public ViewThemePinLock(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        setBackgroundColor(Color.WHITE);
        setClickable(true);
        setFocusable(true);
        init(context);
    }

    @SuppressLint("ResourceType")
    private void init(Context context) {
        ivLock = new ImageView(context);
        ivLock.setId(1221);
        ivLock.setImageResource(R.drawable.iv_pin_lock);
        LayoutParams paramsImageLock = new LayoutParams((int) (13.889f * w / 100), 20 * w / 100);
        paramsImageLock.addRule(CENTER_HORIZONTAL, TRUE);
        paramsImageLock.setMargins(0, (int) (18.564f * w / 100), 0, 0);
        addView(ivLock, paramsImageLock);

        tvTitle = new TextView(context);
        tvTitle.setId(1225);
        tvTitle.setText(getResources().getString(R.string.enter_your_passcode));
        tvTitle.setTextColor(getResources().getColor(R.color.black));
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
        tvTitle.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        LayoutParams paramsTextTitle = new LayoutParams(-2, -2);
        paramsTextTitle.addRule(BELOW, ivLock.getId());
        paramsTextTitle.addRule(CENTER_HORIZONTAL, TRUE);
        paramsTextTitle.setMargins(0, (int) (5.556f * w / 100), 0, 0);
        addView(tvTitle, paramsTextTitle);

        indicatorDots = new IndicatorDots(context);
        indicatorDots.setId(1226);
        LayoutParams paramsDot = new LayoutParams(-2, -2);
        paramsDot.addRule(BELOW, tvTitle.getId());
        paramsDot.addRule(CENTER_HORIZONTAL, TRUE);
        paramsDot.setMargins(0, (int) (4.556f * w / 100), 0, 0);
        addView(indicatorDots, paramsDot);

        tvForgot = new TextView(context);
        tvForgot.setId(1227);
        tvForgot.setText(Utils.underLine(getResources().getString(R.string.forgot_password)));
        tvForgot.setTextColor(getResources().getColor(R.color.red));
        tvForgot.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.389f * w / 100);
        tvForgot.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        LayoutParams paramsForgot = new LayoutParams(-2, -2);
        paramsForgot.setMargins(0, 0, 0, (int) (10.564f * w / 100));
        paramsForgot.addRule(CENTER_HORIZONTAL, TRUE);
        paramsForgot.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        addView(tvForgot, paramsForgot);

        pinLockView = new PinLockView(context);
        pinLockView.attachIndicatorDots(indicatorDots);
        LayoutParams paramsPincode = new LayoutParams(-1, -1);
        paramsPincode.setMargins((int) (11.11f * w / 100), (int) (13.556f * w / 100), (int) (11.11f * w / 100), (int) (5.556f * w / 100));
        paramsPincode.addRule(CENTER_HORIZONTAL, TRUE);
        paramsPincode.addRule(BELOW, indicatorDots.getId());
        paramsPincode.addRule(ABOVE, tvForgot.getId());
        addView(pinLockView, paramsPincode);
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
        ivLock.setColorFilter(Color.parseColor(config.getColorIcon()));
    }

    public void createTheme(Context context, String nameTheme) {
        if (nameTheme.equals("default")) return;

        String jsonConfig = Utils.readFromFile(context, "theme/theme_passcode/" + nameTheme.substring(0, 13) + "/" + nameTheme + "/config.txt");

        ConfigPinThemeModel config = DataLocalManager.getConfigPin(jsonConfig);

        if (config == null) return;

        Bitmap bmBg = BitmapFactory.decodeFile(Utils.getStore(context) + "/theme/theme_passcode/" + nameTheme.substring(0, 13) + "/" + nameTheme + "/background.png");
        if (bmBg == null) setBackgroundColor(getResources().getColor(R.color.white));
        else setBackground(new BitmapDrawable(getResources(), bmBg));

        ivLock.setImageBitmap(BitmapFactory.decodeFile(Utils.getStore(context) + "/theme/theme_passcode/" + nameTheme.substring(0, 13) + "/" + nameTheme + "/ic_lock.png"));
        tvTitle.setTextColor(Color.parseColor(config.getColorTextTitle()));

        if (!config.isDot())
            indicatorDots.setDotDrawable(null, null);
        else {
            BitmapDrawable drawableFill = new BitmapDrawable(getResources(), UtilsBitmap.getBitmapFromVectorDrawable(context, R.drawable.dot_filled_default));
            BitmapDrawable drawableEmpty = new BitmapDrawable(getResources(), UtilsBitmap.getBitmapFromVectorDrawable(context, R.drawable.dot_empty_default));
            drawableEmpty.setTint(Color.parseColor(config.getColorDot()));
            drawableFill.setTint(Color.parseColor(config.getColorDot()));
            indicatorDots.setDotDrawable(drawableEmpty, drawableFill);
        }

        pinLockView.setHideText(config.isHideText());
        if (config.getTypeTheme() == 1) {
            pinLockView.setListBitmap(DataThemePincode.getTheme(context, nameTheme));
            pinLockView.setButtonSize((int) (config.getSizeButton() * w / 100));
        } else {
            pinLockView.setButtonBackgroundDrawable(new BitmapDrawable(getResources(),
                    BitmapFactory.decodeFile(Utils.getStore(context) + "/theme/theme_passcode/" + nameTheme.substring(0, 13) + "/" + nameTheme + "/theme_pincode.png")));
            pinLockView.setButtonDelete(new BitmapDrawable(getResources(),
                    BitmapFactory.decodeFile(Utils.getStore(context) + "/theme/theme_passcode/" + nameTheme.substring(0, 13) + "/" + nameTheme + "/theme_pincode_del.png")));
            if (config.isFont())
                pinLockView.setButtonTypeface(Typeface.createFromFile(
                        new File(Utils.getStore(context) + "/theme/theme_passcode/" + nameTheme.substring(0, 13) + "/" + nameTheme + "/" + config.getNameFont())));
            pinLockView.setTextColor(Color.parseColor(config.getColorText()));
        }
    }

    public TextView getTvForgot() {
        return tvForgot;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public PinLockView getPinLockView() {
        return pinLockView;
    }
}
