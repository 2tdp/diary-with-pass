package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

public class ViewCheckPassword extends RelativeLayout {

    static int w;
    TextView tv, tvForgot;
    EditText et;
    ImageView iv;

    public ViewCheckPassword(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        setBackgroundColor(getResources().getColor(R.color.white));
        setClickable(true);
        setFocusable(true);
        init(context);
    }

    @SuppressLint("ResourceType")
    private void init(Context context) {

        tv = new TextView(context);
        tv.setId(1345);
        tv.setText(getResources().getString(R.string.enter_your_password));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        LayoutParams paramsText = new LayoutParams(-2, -2);
        paramsText.setMargins(0, (int) (47.22f * w / 100), 0, 0);
        paramsText.addRule(CENTER_HORIZONTAL, TRUE);
        addView(tv, paramsText);

        RelativeLayout rl = new RelativeLayout(context);
        rl.setId(1337);

        et = new EditText(context);
        et.setTransformationMethod(PasswordTransformationMethod.getInstance());
        et.setBackgroundResource(R.drawable.border_background_edittext_dialog);
        et.setHintTextColor(getResources().getColor(R.color.gray_2));
        et.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.889f * w / 100);
        et.setTextColor(getResources().getColor(R.color.black));
        et.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        et.setGravity(Gravity.CENTER_VERTICAL);
        et.setPadding((int) (5.556f * w / 100), (int) (3.889f * w / 100), 0, (int) (3.889f * w / 100));
        rl.addView(et, -1, -2);

        iv = new ImageView(context);
        iv.setImageResource(R.drawable.ic_hint);
        LayoutParams paramsImage = new LayoutParams((int) (6.667f * w / 100), (int) (6.667f * w / 100));
        paramsImage.rightMargin = (int) (3.33f * w / 100);
        paramsImage.addRule(ALIGN_PARENT_END, TRUE);
        paramsImage.addRule(CENTER_VERTICAL, TRUE);
        rl.addView(iv, paramsImage);

        LayoutParams paramsConfirm = new LayoutParams(-1, -2);
        paramsConfirm.addRule(BELOW, tv.getId());
        paramsConfirm.setMargins((int) (5.556f * w / 100), (int) (5.556f * w / 100), (int) (5.556f * w / 100), 0);
        addView(rl, paramsConfirm);

        tvForgot = new TextView(context);
        tvForgot.setId(1227);
        tvForgot.setText(Utils.underLine(getResources().getString(R.string.forgot_password)));
        tvForgot.setTextColor(getResources().getColor(R.color.red));
        tvForgot.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.389f * w / 100);
        tvForgot.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        LayoutParams paramsForgot = new LayoutParams(-2, -2);
        paramsForgot.setMargins(0, (int) (3.389f * w / 100), (int) (5.556f * w / 100), 0);
        paramsForgot.addRule(ALIGN_PARENT_END, TRUE);
        paramsForgot.addRule(BELOW, rl.getId());
        addView(tvForgot, paramsForgot);
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

        tv.setTextColor(Color.parseColor(config.getColorIcon()));
    }

    public TextView getTv() {
        return tv;
    }

    public EditText getEt() {
        return et;
    }

    public ImageView getIv() {
        return iv;
    }

    public TextView getTvForgot() {
        return tvForgot;
    }
}
