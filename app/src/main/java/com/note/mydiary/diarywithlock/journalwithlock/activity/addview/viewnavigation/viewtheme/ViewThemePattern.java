package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewtheme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.patternview.customView.DefaultLockerHitCellView;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.patternview.customView.DefaultLockerLinkedLineView;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.patternview.customView.DefaultLockerNormalCustomCellView;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.patternview.customView.PatternLockerView;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigPatternThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

public class ViewThemePattern extends RelativeLayout {

    static int w;
    ImageView ivLock;
    TextView tvTitle, tvForgot;
    PatternLockerView patternLockerView;

    public ViewThemePattern(Context context) {
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
        paramsTextTitle.setMargins(0, (int) (6.667f * w / 100), 0, 0);
        addView(tvTitle, paramsTextTitle);

        tvForgot = new TextView(context);
        tvForgot.setId(1337);
        tvForgot.setText(Utils.underLine(getResources().getString(R.string.forgot_password)));
        tvForgot.setTextColor(getResources().getColor(R.color.red));
        tvForgot.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.389f * w / 100);
        tvForgot.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        LayoutParams paramsForgot = new LayoutParams(-2, -2);
        paramsForgot.setMargins(0, 0, 0, 20 * w / 100);
        paramsForgot.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        paramsForgot.addRule(CENTER_HORIZONTAL, TRUE);
        addView(tvForgot, paramsForgot);

        patternLockerView = new PatternLockerView(context);
        LayoutParams paramsPattern = new LayoutParams(-1, -1);
        paramsPattern.setMargins((int) (8.889f * w / 100), (int) (5.556f * w / 100), (int) (8.889f * w / 100), (int) (18.856f * w / 100));
        paramsPattern.addRule(ABOVE, tvForgot.getId());
        paramsPattern.addRule(CENTER_HORIZONTAL, TRUE);
        addView(patternLockerView, paramsPattern);
    }

    public void createThemeApp(Context context, String nameTheme){
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

        tvTitle.setTextColor(Color.parseColor(config.getColorIcon()));
    }

    public void createThemeLock(Context context, String nameTheme) {
        if (nameTheme.equals("default")) return;

        String jsonConfig = Utils.readFromFile(context, "theme/theme_passcode/" + nameTheme.substring(0, 13) + "/" + nameTheme + "/config.txt");

        ConfigPatternThemeModel config = DataLocalManager.getConfigPattern(jsonConfig);

        if (config == null) return;

        setBackground(new BitmapDrawable(getResources(),
                BitmapFactory.decodeFile(Utils.getStore(context) + "/theme/theme_passcode/" + nameTheme.substring(0, 13) + "/" + nameTheme + "/background.png")));

        ivLock.setImageBitmap(BitmapFactory.decodeFile(Utils.getStore(context) + "/theme/theme_passcode/" + nameTheme.substring(0, 13) + "/" + nameTheme + "/ic_lock.png"));
        tvTitle.setTextColor(Color.parseColor(config.getColorTextTitle()));

        if (config.getTypeTheme() == 0) {
            Bitmap bitmapUnHit = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(Utils.getStore(context) + "/theme/theme_passcode/" + nameTheme.substring(0, 13) + "/" + nameTheme + "/theme_pattern_unhit.png"),
                    (int) (config.getWidthUnHit() * w / 100), (int) (config.getHeightUnHit() * w / 100), false);
            patternLockerView.setNormalCellView(new DefaultLockerNormalCustomCellView().setBitmap(bitmapUnHit));

            Bitmap bitmapHit = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(Utils.getStore(context) + "/theme/theme_passcode/" + nameTheme.substring(0, 13) + "/" + nameTheme + "/theme_pattern_hit.png"),
                    (int) (config.getWidthHit() * w / 100), (int) (config.getHeightHit() * w / 100), false);
            patternLockerView.setHitCellView(new DefaultLockerHitCellView().setBitmap(bitmapHit));
        } else {
            patternLockerView.setNormalCellView(new DefaultLockerNormalCustomCellView()
                    .setThemeFolder(context, Utils.getStore(context) + "/theme/theme_passcode/" + nameTheme.substring(0, 13) + "/" + nameTheme, (int) (config.getWidthUnHit() * w / 100)));

            patternLockerView.setHitCellView(new DefaultLockerHitCellView()
                    .setThemeFolder(context, Utils.getStore(context) + "/theme/theme_passcode/" + nameTheme.substring(0, 13) + "/" + nameTheme, (int) (config.getWidthHit() * w / 100)));
        }

        patternLockerView.setLinkedLineView(new DefaultLockerLinkedLineView()
                .setLineWidth(config.getLinkedLine() * w / 100)
                .setHitColor(Color.parseColor(config.getColorLink())));
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public TextView getTvForgot() {
        return tvForgot;
    }

    public PatternLockerView getPatternView() {
        return patternLockerView;
    }
}
