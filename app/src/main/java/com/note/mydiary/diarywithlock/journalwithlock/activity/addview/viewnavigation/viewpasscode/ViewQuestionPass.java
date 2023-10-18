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

public class ViewQuestionPass extends RelativeLayout {

    static int w;
    ViewToolbar viewToolbar;
    EditText etQuestionOne, etQuestionTwo;
    TextView tvClick, tvError, tvSendGmail, tvNote, tvQuestionOne, tvQuestionTwo;

    public ViewQuestionPass(Context context) {
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
        viewToolbar.setId(1221);
        viewToolbar.getIvVip().setVisibility(GONE);
        viewToolbar.getTvSave().setVisibility(GONE);
        viewToolbar.getTvCancel().setVisibility(GONE);
        viewToolbar.getTvTitle().setText(getResources().getText(R.string.question_settings));
        LayoutParams paramsToolbar = new LayoutParams(-1, -2);
        paramsToolbar.setMargins(0, 0, 0, 0);
        paramsToolbar.addRule(ALIGN_PARENT_TOP, TRUE);
        addView(viewToolbar, paramsToolbar);

        tvNote = new TextView(context);
        tvNote.setId(1238);
        tvNote.setGravity(Gravity.CENTER);
        tvNote.setText(getResources().getString(R.string.note_security_question_is_set_only_once_check_carefully_before_use));
        tvNote.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        tvNote.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
        tvNote.setTextColor(getResources().getColor(R.color.gray_2));
        LayoutParams paramsTextNote = new LayoutParams(-2, -2);
        paramsTextNote.addRule(CENTER_HORIZONTAL, TRUE);
        paramsTextNote.addRule(BELOW, viewToolbar.getId());
        addView(tvNote, paramsTextNote);

        tvQuestionOne = new TextView(context);
        tvQuestionOne.setId(1111);
        tvQuestionOne.setText(getResources().getText(R.string.what_is_your_favorite_color));
        tvQuestionOne.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
        tvQuestionOne.setTextColor(getResources().getColor(R.color.black));
        tvQuestionOne.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        LayoutParams paramsQuestionOne = new LayoutParams(-2, -2);
        paramsQuestionOne.addRule(BELOW, tvNote.getId());
        paramsQuestionOne.setMargins((int) (5.56 * w / 100), (int) (5.56 * w / 100), (int) (5.56 * w / 100), 0);
        addView(tvQuestionOne, paramsQuestionOne);

        etQuestionOne = new EditText(context);
        etQuestionOne.setId(2222);
        etQuestionOne.setBackgroundResource(R.drawable.border_background_edittext_dialog);
        etQuestionOne.setHint(getResources().getString(R.string.write_somethings));
        etQuestionOne.setHintTextColor(getResources().getColor(R.color.gray_2));
        etQuestionOne.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.889f * w / 100);
        etQuestionOne.setTextColor(getResources().getColor(R.color.black));
        etQuestionOne.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        etQuestionOne.setGravity(Gravity.CENTER_VERTICAL);
        etQuestionOne.setPadding((int) (5.556f * w / 100), (int) (3.889f * w / 100), 0, (int) (3.889f * w / 100));
        LayoutParams paramsEtQuestionOne = new LayoutParams(-1, -2);
        paramsEtQuestionOne.addRule(BELOW, tvQuestionOne.getId());
        paramsEtQuestionOne.setMargins((int) (5.556f * w / 100), 0, (int) (5.556f * w / 100), 0);
        addView(etQuestionOne, paramsEtQuestionOne);

        tvQuestionTwo = new TextView(context);
        tvQuestionTwo.setId(3333);
        tvQuestionTwo.setText(getResources().getText(R.string.what_was_your_childhood_home_name));
        tvQuestionTwo.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
        tvQuestionTwo.setTextColor(getResources().getColor(R.color.black));
        tvQuestionTwo.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        LayoutParams paramsQuestionTwo = new LayoutParams(-2, -2);
        paramsQuestionTwo.addRule(BELOW, etQuestionOne.getId());
        paramsQuestionTwo.setMargins((int) (5.56 * w / 100), (int) (5.56 * w / 100), (int) (5.56 * w / 100), 0);
        addView(tvQuestionTwo, paramsQuestionTwo);

        etQuestionTwo = new EditText(context);
        etQuestionTwo.setId(4444);
        etQuestionTwo.setBackgroundResource(R.drawable.border_background_edittext_dialog);
        etQuestionTwo.setHint(getResources().getString(R.string.write_somethings));
        etQuestionTwo.setHintTextColor(getResources().getColor(R.color.gray_2));
        etQuestionTwo.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.889f * w / 100);
        etQuestionTwo.setTextColor(getResources().getColor(R.color.black));
        etQuestionTwo.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        etQuestionTwo.setGravity(Gravity.CENTER_VERTICAL);
        etQuestionTwo.setPadding((int) (5.556f * w / 100), (int) (3.889f * w / 100), 0, (int) (3.889f * w / 100));
        LayoutParams paramsEtQuestionTwo = new LayoutParams(-1, -2);
        paramsEtQuestionTwo.addRule(BELOW, tvQuestionTwo.getId());
        paramsEtQuestionTwo.setMargins((int) (5.556f * w / 100), 0, (int) (5.556f * w / 100), 0);
        addView(etQuestionTwo, paramsEtQuestionTwo);

        tvError = new TextView(context);
        tvError.setId(4445);
        tvError.setVisibility(INVISIBLE);
        tvError.setGravity(Gravity.CENTER);
        tvError.setText(getResources().getString(R.string.the_answer_is_not_correct_you_can_contact_us_via_our_mail_for_support));
        tvError.setTextColor(getResources().getColor(R.color.red));
        tvError.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
        tvError.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        LayoutParams paramsTextError = new LayoutParams(-2, -2);
        paramsTextError.addRule(CENTER_HORIZONTAL, TRUE);
        paramsTextError.addRule(BELOW, etQuestionTwo.getId());
        paramsTextError.setMargins(0, (int) (8.889f * w / 100), 0, 0);
        addView(tvError, paramsTextError);

        tvSendGmail = new TextView(context);
        tvSendGmail.setId(4446);
        tvSendGmail.setVisibility(INVISIBLE);
        tvSendGmail.setGravity(Gravity.CENTER);
        tvSendGmail.setText(Utils.underLine(getResources().getString(R.string.click_here)));
        tvSendGmail.setTextColor(getResources().getColor(R.color.red));
        tvSendGmail.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.889f * w / 100);
        tvSendGmail.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        LayoutParams paramsTextSendGmail = new LayoutParams(-2, -2);
        paramsTextSendGmail.addRule(CENTER_HORIZONTAL, TRUE);
        paramsTextSendGmail.addRule(BELOW, tvError.getId());
        paramsTextSendGmail.setMargins(0, 0, 0, 0);
        addView(tvSendGmail, paramsTextSendGmail);

        tvClick = new TextView(context);
        tvClick.setText(getResources().getString(R.string.confirm));
        tvClick.setPadding(2 * w / 100, 2 * w / 100, 2 * w / 100, 2 * w / 100);
        tvClick.setGravity(Gravity.CENTER);
        tvClick.setTextColor(getResources().getColor(R.color.white));
        tvClick.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.889f * w / 100);
        tvClick.setTypeface(Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context));
        tvClick.setBackgroundResource(R.drawable.border_orange);
        LayoutParams paramsTextClick = new LayoutParams(-1, -2);
        paramsTextClick.addRule(BELOW, tvError.getId());
        paramsTextClick.setMargins((int) (22.22f * w / 100), (int) (15.556 * w / 100), (int) (22.22f * w / 100), 0);
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

        tvQuestionOne.setTextColor(Color.parseColor(config.getColorIcon()));
        tvQuestionTwo.setTextColor(Color.parseColor(config.getColorIcon()));

        tvClick.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorMain())}, (int) (2.5f * w / 100), -1, -1));
    }

    public ViewToolbar getViewToolbar() {
        return viewToolbar;
    }

    public EditText getEtQuestionOne() {
        return etQuestionOne;
    }

    public EditText getEtQuestionTwo() {
        return etQuestionTwo;
    }

    public TextView getTvNote() {
        return tvNote;
    }

    public TextView getTvClick() {
        return tvClick;
    }

    public TextView getTvError() {
        return tvError;
    }

    public TextView getTvSendGmail() {
        return tvSendGmail;
    }
}
