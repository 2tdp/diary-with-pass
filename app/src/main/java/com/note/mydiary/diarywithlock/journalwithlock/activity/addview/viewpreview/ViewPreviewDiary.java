package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewpreview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.ViewToolbar;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsTheme;
import com.note.remiads.native_ads.ViewNativeAds;

public class ViewPreviewDiary extends RelativeLayout {

    static int w;
    ViewToolbar viewToolbar;
    TextView tvEdit, tvDateTime, tvTitle;
    ImageView ivEdit;
    RecyclerView rcvDiary;
    RelativeLayout rlEdit;

    public ViewPreviewDiary(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        setBackgroundColor(getResources().getColor(R.color.white));
        setFocusable(true);
        setClickable(true);
        init(context);
    }

    @SuppressLint("ResourceType")
    private void init(Context context) {
        viewToolbar = new ViewToolbar(context);
        viewToolbar.setId(1333);
        viewToolbar.getTvTitle().setText(getResources().getString(R.string.diary));
        viewToolbar.getIvVip().setImageResource(R.drawable.ic_delete);
        viewToolbar.getTvSave().setVisibility(GONE);
        LayoutParams paramsToolbar = new LayoutParams(-1, -2);
        paramsToolbar.setMargins(0, (int) (11.11f * w / 100), 0, 0);
        addView(viewToolbar, paramsToolbar);

        ViewNativeAds vAds = new ViewNativeAds(context);
        vAds.setId(45);
        vAds.addAds(null, true, com.note.remiads.R.string.na_small);
        LayoutParams pAds = new LayoutParams(-1,-2);
        pAds.setMargins(w/30, w/30,w/30,0);
        pAds.addRule(BELOW, viewToolbar.getId());
        addView(vAds, pAds);

        tvDateTime = new TextView(context);
        tvDateTime.setId(1334);
        tvDateTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
        tvDateTime.setTextColor(getResources().getColor(R.color.text_date));
        tvDateTime.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        LayoutParams paramsTextDateTime = new LayoutParams(-2, -2);
        paramsTextDateTime.addRule(BELOW, vAds.getId());
        paramsTextDateTime.setMargins((int) (5.556f * w / 100), (int) (5.556f * w / 100), 0, 0);

        addView(tvDateTime, paramsTextDateTime);

        tvTitle = new TextView(context);
        tvTitle.setId(1335);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.89f * w / 100);
        tvTitle.setTextColor(getResources().getColor(R.color.black));
        tvTitle.setTypeface(Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context));
        LayoutParams paramsTextTitle = new LayoutParams(-2, -2);
        paramsTextTitle.addRule(BELOW, tvDateTime.getId());
        paramsTextTitle.setMargins((int) (5.556f * w / 100), 0, 0, 0);

        addView(tvTitle, paramsTextTitle);

        rcvDiary = new RecyclerView(context);
        LayoutParams paramsRcv = new LayoutParams(-1, -1);
        paramsRcv.addRule(BELOW, tvTitle.getId());
        paramsRcv.setMargins((int) (5.556f * w / 100), 0, (int) (5.556f * w / 100), 0);
        addView(rcvDiary, paramsRcv);

        rlEdit = new RelativeLayout(context);
        rlEdit.setBackgroundResource(R.drawable.border_gray_1);

        ivEdit = new ImageView(context);
        ivEdit.setId(1336);
        ivEdit.setImageResource(R.drawable.ic_edit);
        LayoutParams paramsImage = new LayoutParams((int) (6.667f * w / 100), (int) (6.667f * w / 100));
        paramsImage.addRule(CENTER_VERTICAL, TRUE);
        paramsImage.setMarginStart((int) (3.89f * w / 100));
        rlEdit.addView(ivEdit, paramsImage);

        tvEdit = new TextView(context);
        tvEdit.setText(getResources().getString(R.string.edit_diary));
        tvEdit.setGravity(Gravity.CENTER);
        tvEdit.setTextColor(getResources().getColor(R.color.black));
        tvEdit.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
        tvEdit.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        LayoutParams paramsText = new LayoutParams(-1, -1);
        paramsText.addRule(CENTER_VERTICAL, TRUE);
        paramsText.addRule(RIGHT_OF, ivEdit.getId());

        rlEdit.addView(tvEdit, paramsText);

        LayoutParams paramsRlEdit = new LayoutParams(-2, (int) (11.11f * w / 100));
        paramsRlEdit.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        paramsRlEdit.addRule(CENTER_HORIZONTAL, TRUE);
        paramsRlEdit.setMargins((int) (33.33f * w / 100), 0, (int) (33.33f * w / 100), (int) (3.33f * w / 100));
        addView(rlEdit, paramsRlEdit);
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

        tvTitle.setTextColor(Color.parseColor(config.getColorIcon()));
        tvDateTime.setTextColor(Color.parseColor(config.getColorTextDate()));
        tvEdit.setTextColor(Color.parseColor(config.getColorIcon()));
        UtilsTheme.changeIcon(context,
                "edit", 2, R.drawable.ic_edit, ivEdit,
                Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorMain()));
        rlEdit.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorMainLight())}, (int) (2.5f * w / 100), -1, -1));
    }

    public ViewToolbar getViewToolbar() {
        return viewToolbar;
    }

    public TextView getTvDateTime() {
        return tvDateTime;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public RecyclerView getRcvDiary() {
        return rcvDiary;
    }

    public RelativeLayout getRlEdit() {
        return rlEdit;
    }
}
