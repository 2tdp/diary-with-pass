package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.ViewToolbar;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsTheme;

public class ViewPasscode extends LinearLayout {

    static int w;
    TextView tv, tvDes;
    ViewToolbar viewToolbar;
    ViewItem viewPassword, viewPincode, viewPattern, viewFingerprint, viewSignature;
    RelativeLayout rlSwitch;
    SwitchCompat switchCompat;

    public ViewPasscode(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        setBackgroundColor(getResources().getColor(R.color.white));
        setOrientation(VERTICAL);
        setClickable(true);
        setFocusable(true);
        init(context);
    }

    @SuppressLint("ResourceType")
    private void init(Context context) {
        viewToolbar = new ViewToolbar(context);
        viewToolbar.getTvTitle().setText(getResources().getString(R.string.passcode));
        viewToolbar.getIvVip().setVisibility(GONE);
        viewToolbar.getTvSave().setVisibility(GONE);
        LayoutParams paramsToolbar = new LayoutParams(-1, -2);
        paramsToolbar.setMargins(0, (int) (11.11f * w / 100), 0, 0);
        addView(viewToolbar, paramsToolbar);

        rlSwitch = new RelativeLayout(context);
        LayoutParams paramsRlSwitch = new LayoutParams(-1, -2);
        paramsRlSwitch.setMargins((int) (5.556f * w / 100), (int) (5.556f * w / 100), (int) (5.556f * w / 100), 0);

        switchCompat = new SwitchCompat(context);
        switchCompat.setId(1347);
        switchCompat.setThumbResource(R.drawable.custom_switch_thumb);
        switchCompat.setTrackResource(R.drawable.custom_switch_track);
        RelativeLayout.LayoutParams paramsSwitch = new RelativeLayout.LayoutParams((int) (16f * w / 100), (int) (6.667f * w / 100));
        paramsSwitch.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsSwitch.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        rlSwitch.addView(switchCompat, paramsSwitch);

        tv = new TextView(context);
        tv.setText(getResources().getString(R.string.set_a_passcode_for_your_diary));
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
        tv.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -2);
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.LEFT_OF, switchCompat.getId());
        rlSwitch.addView(tv, params);

        addView(rlSwitch, paramsRlSwitch);

        tvDes = new TextView(context);
        tvDes.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.389f * w / 100);
        tvDes.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        tvDes.setTextColor(getResources().getColor(R.color.gray_2));
        LayoutParams paramsText = new LayoutParams(-2, -2);
        paramsText.gravity = Gravity.CENTER_HORIZONTAL;
        paramsText.setMargins(0, (int) (5.556f * w / 100), 0, 0);
        addView(tvDes, paramsText);

        LinearLayout llContent = new LinearLayout(context);
        llContent.setOrientation(VERTICAL);
        LayoutParams paramsContent = new LayoutParams(-1, -2);
        paramsContent.setMargins((int) (5.556f * w / 100), 0, (int) (5.556f * w / 100), 0);

        LayoutParams paramsItem = new LayoutParams(-1, (int) (13.889f * w / 100));
        paramsItem.setMargins(0, (int) (4.167f * w / 100), 0, 0);

        viewPassword = new ViewItem(context);
        viewPassword.getIv().setImageResource(R.drawable.ic_passwords);
        viewPassword.getTv().setText(getResources().getString(R.string.passwords));
        llContent.addView(viewPassword, paramsItem);

        viewPincode = new ViewItem(context);
        viewPincode.getIv().setImageResource(R.drawable.ic_pincode);
        viewPincode.getTv().setText(getResources().getString(R.string.pincode));
        llContent.addView(viewPincode, paramsItem);

        viewPattern = new ViewItem(context);
        viewPattern.getIv().setImageResource(R.drawable.ic_pattern);
        viewPattern.getTv().setText(getResources().getString(R.string.pattern));
        llContent.addView(viewPattern, paramsItem);

        viewFingerprint = new ViewItem(context);
        viewFingerprint.getIv().setImageResource(R.drawable.ic_fingerprint);
        viewFingerprint.getTv().setText(getResources().getString(R.string.fingerprint));
        llContent.addView(viewFingerprint, paramsItem);

        viewSignature = new ViewItem(context);
        viewSignature.getIv().setImageResource(R.drawable.ic_signature);
        viewSignature.getTv().setText(getResources().getString(R.string.signature));
        llContent.addView(viewSignature, paramsItem);

        addView(llContent, paramsContent);
    }

    public class ViewItem extends RelativeLayout {

        ImageView iv, ivTick;
        TextView tv;

        @SuppressLint("ResourceType")
        public ViewItem(Context context) {
            super(context);
            setBackgroundResource(R.drawable.border_orange_2);

            iv = new ImageView(context);
            iv.setId(1234);
            LayoutParams paramsImage = new LayoutParams((int) (8.889f * w / 100), (int) (8.889f * w / 100));
            paramsImage.setMargins((int) (3.056f * w / 100), 0, 0, 0);
            paramsImage.addRule(CENTER_VERTICAL, TRUE);
            addView(iv, paramsImage);

            tv = new TextView(context);
            tv.setTextColor(getResources().getColor(R.color.black));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.389f * w / 100);
            tv.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
            LayoutParams paramsText = new LayoutParams(-2, -2);
            paramsText.addRule(CENTER_VERTICAL, TRUE);
            paramsText.addRule(RIGHT_OF, iv.getId());
            paramsText.setMargins((int) (5.556f * w / 100), 0, 0, 0);
            addView(tv, paramsText);

            ivTick = new ImageView(context);
            ivTick.setVisibility(GONE);
            ivTick.setImageResource(R.drawable.ic_tick);
            ivTick.setColorFilter(getResources().getColor(R.color.orange));
            LayoutParams paramsTick = new LayoutParams((int) (6.667f * w / 100), (int) (6.667f * w / 100));
            paramsTick.setMargins(0, 0, (int) (3.33f * w / 100), 0);
            paramsTick.addRule(CENTER_VERTICAL, TRUE);
            paramsTick.addRule(ALIGN_PARENT_END, TRUE);
            addView(ivTick, paramsTick);
        }

        public ImageView getIv() {
            return iv;
        }

        public TextView getTv() {
            return tv;
        }

        public ImageView getIvTick() {
            return ivTick;
        }
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

        tv.setTextColor(Color.parseColor(config.getColorIcon()));
        tvDes.setTextColor(Color.parseColor(config.getColorUnSelect()));

        switchCompat.setThumbTintList(Utils.createColorState(Color.parseColor(config.getColorSwitch()), Color.parseColor(config.getColorSwitch())));

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_checked},
                Utils.createBackground(new int[]{Color.parseColor(config.getColorBackgroundSwitch())}, (int) (3.5f * w / 100), -1, -1));
        stateListDrawable.addState(new int[]{-android.R.attr.state_checked},
                Utils.createBackground(new int[]{Color.parseColor(config.getColorUnSelect())}, (int) (3.5f * w / 100), -1, -1));
        stateListDrawable.addState(new int[]{}, Utils.createBackground(new int[]{Color.parseColor(config.getColorUnSelect())}, (int) (3.5f * w / 100), -1, -1));
        switchCompat.setTrackDrawable(stateListDrawable);

        viewPassword.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorMainLight())}, (int) (2.5f * w / 100), -1, -1));
        viewPincode.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorMainLight())}, (int) (2.5f * w / 100), -1, -1));
        viewPattern.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorMainLight())}, (int) (2.5f * w / 100), -1, -1));
        viewFingerprint.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorMainLight())}, (int) (2.5f * w / 100), -1, -1));
        viewSignature.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorMainLight())}, (int) (2.5f * w / 100), -1, -1));

        viewPassword.getTv().setTextColor(Color.parseColor(config.getColorIcon()));
        viewPincode.getTv().setTextColor(Color.parseColor(config.getColorIcon()));
        viewPattern.getTv().setTextColor(Color.parseColor(config.getColorIcon()));
        viewFingerprint.getTv().setTextColor(Color.parseColor(config.getColorIcon()));
        viewSignature.getTv().setTextColor(Color.parseColor(config.getColorIcon()));

        UtilsTheme.changeIcon(context,
                "password", 8, R.drawable.ic_passwords, viewPassword.getIv(),
                Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorIconInColor()));

        UtilsTheme.changeIcon(context,
                "pincode", 5, R.drawable.ic_pincode, viewPincode.getIv(),
                Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorIconInColor()));

        UtilsTheme.changeIcon(context,
                "pattern", 11, R.drawable.ic_pattern, viewPattern.getIv(),
                Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorIconInColor()));

        UtilsTheme.changeIcon(context,
                "fingerprint", 6, R.drawable.ic_fingerprint, viewFingerprint.getIv(),
                Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorIconInColor()));

        UtilsTheme.changeIcon(context,
                "signature", 8, R.drawable.ic_signature, viewSignature.getIv(),
                Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorIconInColor()));

        viewPassword.getIvTick().setColorFilter(Color.parseColor(config.getColorMain()));
        viewPincode.getIvTick().setColorFilter(Color.parseColor(config.getColorMain()));
        viewPattern.getIvTick().setColorFilter(Color.parseColor(config.getColorMain()));
        viewFingerprint.getIvTick().setColorFilter(Color.parseColor(config.getColorMain()));
        viewSignature.getIvTick().setColorFilter(Color.parseColor(config.getColorMain()));
    }

    public ViewToolbar getViewToolbar() {
        return viewToolbar;
    }

    public TextView getTvDes() {
        return tvDes;
    }

    public SwitchCompat getSwitchCompat() {
        return switchCompat;
    }

    public ViewItem getViewPassword() {
        return viewPassword;
    }

    public ViewItem getViewPincode() {
        return viewPincode;
    }

    public ViewItem getViewPattern() {
        return viewPattern;
    }

    public ViewItem getViewFingerprint() {
        return viewFingerprint;
    }

    public ViewItem getViewSignature() {
        return viewSignature;
    }

    public RelativeLayout getRlSwitch() {
        return rlSwitch;
    }
}
