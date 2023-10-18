package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.ViewToolbar;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

public class ViewPassword extends RelativeLayout {

    static int w;
    ViewToolbar viewToolbar;
    EditText etSetPassword, etConfirm;
    TextView tvClick, tvError, tvConfirm, tvSetPassword;

    public ViewPassword(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        init(context);
        setBackgroundColor(getResources().getColor(R.color.white));
        setClickable(true);
        setFocusable(true);
    }

    @SuppressLint("ResourceType")
    private void init(Context context) {
        viewToolbar = new ViewToolbar(context);
        viewToolbar.setId(1238);
        viewToolbar.getIvVip().setVisibility(GONE);
        viewToolbar.getTvSave().setVisibility(GONE);
        viewToolbar.getTvCancel().setVisibility(GONE);
        viewToolbar.getTvTitle().setText(getResources().getText(R.string.passwords));
        LayoutParams paramsToolbar = new LayoutParams(-1, -2);
        paramsToolbar.setMargins(0, 0, 0, 0);
        paramsToolbar.addRule(ALIGN_PARENT_TOP, TRUE);
        addView(viewToolbar, paramsToolbar);

        tvSetPassword = new TextView(context);
        tvSetPassword.setId(3333);
        tvSetPassword.setText(getResources().getText(R.string.set_your_password));
        tvSetPassword.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
        tvSetPassword.setTextColor(getResources().getColor(R.color.black));
        tvSetPassword.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        LayoutParams paramsTitle = new LayoutParams(-2, -2);
        paramsTitle.addRule(BELOW, viewToolbar.getId());
        paramsTitle.setMargins((int) (5.56 * w / 100), (int) (5.56 * w / 100), (int) (5.56 * w / 100), 0);
        addView(tvSetPassword, paramsTitle);

        etSetPassword = new EditText(context);
        etSetPassword.setId(4444);
        etSetPassword.setSingleLine();
        etSetPassword.setBackgroundResource(R.drawable.border_background_edittext_dialog);
        etSetPassword.setHint(getResources().getString(R.string.write_somethings));
        etSetPassword.setHintTextColor(getResources().getColor(R.color.gray_2));
        etSetPassword.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.889f * w / 100);
        etSetPassword.setTextColor(getResources().getColor(R.color.black));
        etSetPassword.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        etSetPassword.setGravity(Gravity.CENTER_VERTICAL);
        etSetPassword.setPadding((int) (5.556f * w / 100), (int) (3.889f * w / 100), 0, (int) (3.889f * w / 100));
        LayoutParams paramsEtSetPassword = new LayoutParams(-1, -2);
        paramsEtSetPassword.addRule(BELOW, tvSetPassword.getId());
        paramsEtSetPassword.setMargins((int) (5.556f * w / 100), 0, (int) (5.556f * w / 100), 0);
        addView(etSetPassword, paramsEtSetPassword);

        tvConfirm = new TextView(context);
        tvConfirm.setId(5555);
        tvConfirm.setText(getResources().getText(R.string.confirm_your_password));
        tvConfirm.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
        tvConfirm.setTextColor(getResources().getColor(R.color.black));
        tvConfirm.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        LayoutParams paramsTextConfirm = new LayoutParams(-2, -2);
        paramsTextConfirm.addRule(BELOW, etSetPassword.getId());
        paramsTextConfirm.setMargins((int) (5.56 * w / 100), (int) (5.56 * w / 100), (int) (5.56 * w / 100), 0);
        addView(tvConfirm, paramsTextConfirm);

        etConfirm = new EditText(context);
        etConfirm.setId(6666);
        etConfirm.setSingleLine();
        etConfirm.setBackgroundResource(R.drawable.border_background_edittext_dialog);
        etConfirm.setHint(getResources().getString(R.string.confirm_password));
        etConfirm.setHintTextColor(getResources().getColor(R.color.gray_2));
        etConfirm.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.889f * w / 100);
        etConfirm.setTextColor(getResources().getColor(R.color.black));
        etConfirm.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        etConfirm.setGravity(Gravity.CENTER_VERTICAL);
        etConfirm.setPadding((int) (5.556f * w / 100), (int) (3.889f * w / 100), 0, (int) (3.889f * w / 100));
        LayoutParams paramsConfirm = new LayoutParams(-1, -2);
        paramsConfirm.addRule(BELOW, tvConfirm.getId());
        paramsConfirm.setMargins((int) (5.556f * w / 100), 0, (int) (5.556f * w / 100), 0);
        addView(etConfirm, paramsConfirm);

        tvError = new TextView(context);
        tvError.setVisibility(GONE);
        tvError.setText(getResources().getText(R.string.error_passwords));
        tvError.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
        tvError.setTextColor(getResources().getColor(R.color.red));
        tvError.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        LayoutParams paramsError = new LayoutParams(-2, -2);
        paramsError.addRule(BELOW, etConfirm.getId());
        paramsError.addRule(ALIGN_PARENT_END, TRUE);
        paramsError.setMargins(0, 0, (int) (5.56 * w / 100), 0);
        addView(tvError, paramsError);

        tvClick = new TextView(context);
        tvClick.setText(getResources().getString(R.string.add_passcode));
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

        tvSetPassword.setTextColor(Color.parseColor(config.getColorIcon()));
        tvConfirm.setTextColor(Color.parseColor(config.getColorIcon()));
        tvClick.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorMain())}, (int) (2.5f * w / 100), -1, -1));
    }

    public ViewToolbar getViewToolbar() {
        return viewToolbar;
    }

    public void setViewToolbar(ViewToolbar viewToolbar) {
        this.viewToolbar = viewToolbar;
    }

    public EditText getEtSetPassword() {
        return etSetPassword;
    }

    public void setEtSetPassword(EditText etSetPassword) {
        this.etSetPassword = etSetPassword;
    }

    public EditText getEtConfirm() {
        return etConfirm;
    }

    public void setEtConfirm(EditText etConfirm) {
        this.etConfirm = etConfirm;
    }

    public TextView getTvClick() {
        return tvClick;
    }

    public TextView getTvError() {
        return tvError;
    }
}
