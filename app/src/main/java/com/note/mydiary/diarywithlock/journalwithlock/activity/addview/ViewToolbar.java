package com.note.mydiary.diarywithlock.journalwithlock.activity.addview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsTheme;


public class ViewToolbar extends RelativeLayout {

    private ImageView ivBack, ivVip;
    private TextView tvCancel, tvTitle, tvSave;
    private int w;

    public ViewToolbar(Context context) {
        super(context);
        w = context.getResources().getDisplayMetrics().widthPixels;
        setClickable(true);
        setFocusable(true);
        init(context);
    }

    public ViewToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);
        setFocusable(true);
        init(context);
    }

    public ViewToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
        setFocusable(true);
        init(context);
    }

    private void init(Context context) {
        ivBack = new ImageView(context);
        ivBack.setImageResource(R.drawable.ic_back);
        ivBack.setPadding(w / 100, w / 100, w / 100, w / 100);
        ivBack.setBackgroundResource(Utils.effectPressRectangle(context).resourceId);
        LayoutParams paramsBack = new LayoutParams(9 * w / 100, 9 * w / 100);
        paramsBack.setMargins(6 * w / 100, 0, 0, 0);
        paramsBack.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        addView(ivBack, paramsBack);

        tvCancel = new TextView(context);
        tvCancel.setVisibility(GONE);
        tvCancel.setText(getResources().getString(R.string.cancel));
        tvCancel.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
        tvCancel.setTextColor(getResources().getColor(R.color.black));
        tvCancel.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        LayoutParams paramsTextCancel = new LayoutParams(-2, -2);
        paramsTextCancel.setMargins(6 * w / 100, 0, 0, 0);
        paramsTextCancel.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        paramsTextCancel.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        addView(tvCancel, paramsTextCancel);

        tvTitle = new TextView(context);
        tvTitle.setText(context.getResources().getString(R.string.menu));
        tvTitle.setTextColor(context.getResources().getColor(R.color.black));
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4.4f / 100);
        tvTitle.setTypeface(Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context));

        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        addView(tvTitle, layoutParams);

        ivVip = new ImageView(context);
        ivVip.setImageResource(R.drawable.ic_vip);
        ivVip.setPadding(w / 100, w / 100, w / 100, w / 100);
        ivVip.setBackgroundResource(Utils.effectPressRectangle(context).resourceId);
        LayoutParams paramsVip = new LayoutParams(9 * w / 100, 9 * w / 100);
        paramsVip.setMargins(0, 0, 6 * w / 100, 0);
        paramsVip.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        addView(ivVip, paramsVip);

        tvSave = new TextView(context);
        tvSave.setBackgroundResource(R.drawable.border_orange);
        tvSave.setTextColor(getResources().getColor(R.color.white));
        tvSave.setText(getResources().getString(R.string.save));
        tvSave.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.8f * w / 100);
        tvSave.setGravity(Gravity.CENTER);
        LayoutParams paramsTextSave = new LayoutParams((int) (13.8f * w / 100), (int) (7 * w / 100f));
        paramsTextSave.setMargins(0, 0, 6 * w / 100, 0);
        paramsTextSave.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        addView(tvSave, paramsTextSave);
    }

    public void createTheme(Context context, String nameTheme) {
        if (nameTheme.equals("default")) return;

        String jsonConfig = Utils.readFromFile(context, "theme/theme_app/" + nameTheme + "/config.txt");

        ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

        if (config == null) return;

        tvCancel.setTextColor(Color.parseColor(config.getColorIcon()));
        tvTitle.setTextColor(Color.parseColor(config.getColorIcon()));
        tvSave.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorMain())}, (int) (2f * w / 100), -1, -1));
        UtilsTheme.changeIcon(context,
                "back", 1, R.drawable.ic_back, ivBack,
                Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));
    }

    public ImageView getIvBack() {
        return ivBack;
    }

    public TextView getTvCancel() {
        return tvCancel;
    }

    public ImageView getIvVip() {
        return ivVip;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public TextView getTvSave() {
        return tvSave;
    }
}
