package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewdialog;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

public class ViewDialogRecord extends LinearLayout {

    int w;
    EditText et;
    ViewYesNo viewYesNo;

    public ViewDialogRecord(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        setOrientation(VERTICAL);
        setBackgroundResource(R.drawable.boder_dialog);
        setPadding(0, (int) (4.72f * w / 100), 0, (int) (4.72f * w / 100));
        init(context);
    }

    private void init(Context context) {
        TextView tv = new TextView(context);
        tv.setText(getResources().getString(R.string.name_your_recording));
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.89f * w / 100);
        tv.setTypeface(Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context));
        LayoutParams paramsTitle = new LayoutParams(-2, -2);
        paramsTitle.gravity = Gravity.CENTER_HORIZONTAL;
        addView(tv, paramsTitle);

        et = new EditText(context);
        et.setSingleLine();
        et.setTextColor(getResources().getColor(R.color.black));
        et.setBackgroundResource(R.drawable.border_background_edittext_dialog);
        et.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        et.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.89f * w / 100);
        et.setGravity(Gravity.CENTER);
        et.setHint(getResources().getString(R.string.enter_your_name_recording));
        et.setHintTextColor(getResources().getColor(R.color.gray_2));
        LayoutParams paramsEditText = new LayoutParams(-1, (int) (14.167f * w / 100));
        paramsEditText.setMargins((int) (5.833f * w / 100), (int) (2.78f * w / 100), (int) (5.833f * w / 100), 0);
        addView(et, paramsEditText);

        viewYesNo = new ViewYesNo(context);
        LayoutParams paramsYesNo = new LayoutParams(-1, (int) (11.11f * w / 100));
        paramsYesNo.setMargins((int) (5.83f * w / 100), (int) (5.83f * w / 100), (int) (5.83f * w / 100), 0);
        addView(viewYesNo, paramsYesNo);

        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")){
            String jsonConfig = Utils.readFromFile(context, "theme/theme_app/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP) + "/config.txt");

            ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);
            if (config == null) return;

            tv.setTextColor(Color.parseColor(config.getColorIcon()));
        }
    }

    public EditText getEt() {
        return et;
    }

    public ViewYesNo getViewYesNo() {
        return viewYesNo;
    }
}
