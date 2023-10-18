package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.vip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.ViewToolbar;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

public class ViewVipOne extends RelativeLayout {

    static int w;
    ViewToolbar viewToolbar;
    ImageView ivContent;
    TextView tvContinue, tvTitleBold;

    public ViewVipOne(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        setBackgroundResource(R.drawable.im_background_1);
        setFocusable(true);
        setClickable(true);
        init(context);
    }

    @SuppressLint("ResourceType")
    private void init(Context context) {
        viewToolbar = new ViewToolbar(context);
        viewToolbar.setId(1339);
        viewToolbar.getTvTitle().setVisibility(GONE);
        viewToolbar.getIvVip().setVisibility(GONE);
        viewToolbar.getTvSave().setVisibility(GONE);
        LayoutParams paramsToolbar = new LayoutParams(-1, -2);
        paramsToolbar.setMargins(0, (int) (11.11f * w / 100), 0, 0);
        addView(viewToolbar, paramsToolbar);

        tvContinue = new TextView(context);
        tvContinue.setId(1441);
        tvContinue.setGravity(Gravity.CENTER);
        tvContinue.setBackgroundResource(R.drawable.border_orange);
        tvContinue.setText(getResources().getString(R.string.action_continue));
        tvContinue.setTextColor(getResources().getColor(R.color.white));
        tvContinue.setTypeface(Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context));
        tvContinue.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.389f * w / 100);
        LayoutParams paramsTextContinue = new LayoutParams((int) (55.56f * w / 100), (int) (11.11f * w / 100));
        paramsTextContinue.setMargins(0, 0, 0, (int) (4.44f * w / 100));
        paramsTextContinue.addRule(CENTER_HORIZONTAL, TRUE);
        paramsTextContinue.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        addView(tvContinue, paramsTextContinue);

        LinearLayout llContent = new LinearLayout(context);
        llContent.setOrientation(LinearLayout.VERTICAL);
        LayoutParams paramsLlContent = new LayoutParams(-1, -1);
        paramsLlContent.addRule(BELOW, viewToolbar.getId());
        paramsLlContent.addRule(ABOVE, tvContinue.getId());
        paramsLlContent.setMargins(0, (int) (3.056f * w / 100), 0, 0);

        ImageView ivVip = new ImageView(context);
        ivVip.setImageResource(R.drawable.ic_vip);
        LinearLayout.LayoutParams paramsImageVip = new LinearLayout.LayoutParams((int) (23.33f * w / 100), (int) (23.33f * w / 100));
        paramsImageVip.gravity = Gravity.CENTER_HORIZONTAL;
        llContent.addView(ivVip, paramsImageVip);

        tvTitleBold = new TextView(context);
        tvTitleBold.setText(getResources().getString(R.string.start_like_a_pro));
        tvTitleBold.setTextColor(getResources().getColor(R.color.black));
        tvTitleBold.setTypeface(Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context));
        tvTitleBold.setTextSize(TypedValue.COMPLEX_UNIT_PX, 6.11f * w / 100);
        LinearLayout.LayoutParams paramsTextTitleBold = new LinearLayout.LayoutParams(-2, -2);
        paramsTextTitleBold.gravity = Gravity.CENTER_HORIZONTAL;
        paramsTextTitleBold.setMargins(0, (int) (0.833f * w / 100), 0, 0);
        llContent.addView(tvTitleBold, paramsTextTitleBold);

        TextView tvTitle = new TextView(context);
        tvTitle.setText(getResources().getString(R.string.unlock_all_feature));
        tvTitle.setTextColor(getResources().getColor(R.color.text_vip));
        tvTitle.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.389f * w / 100);
        LinearLayout.LayoutParams paramsTextTitle = new LinearLayout.LayoutParams(-2, -2);
        paramsTextTitle.gravity = Gravity.CENTER_HORIZONTAL;
        llContent.addView(tvTitle, paramsTextTitle);

        ivContent = new ImageView(context);
        ivContent.setImageResource(R.drawable.im_vip);
        LinearLayout.LayoutParams paramsImageContent = new LinearLayout.LayoutParams((int) (90.728f * w / 100), (int) (67.778f * w / 100));
        paramsImageContent.gravity = Gravity.CENTER_HORIZONTAL;
        paramsImageContent.setMargins(0, (int) (8.44f * w / 100), 0, 0);
        llContent.addView(ivContent, paramsImageContent);

        addView(llContent, paramsLlContent);
    }

    public void createTheme(Context context, String nameTheme) {
        if (nameTheme.equals("default")) return;

        String jsonConfig = Utils.readFromFile(context, "theme/theme_app/" + nameTheme + "/config.txt");

        ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

        if (config == null) return;

        Bitmap bmBg = BitmapFactory.decodeFile(Utils.getStore(context) + "/theme/theme_app/" + nameTheme + "/background_vip_1.png");
        if (bmBg == null) setBackgroundColor(getResources().getColor(R.color.white));
        else setBackground(new BitmapDrawable(getResources(), bmBg));

        ivContent.setImageBitmap(BitmapFactory.decodeFile(Utils.getStore(context) + "/theme/theme_app/" + nameTheme + "/image_vip_1.png"));

        tvContinue.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorMain())}, (int) (2.5f * w / 100), -1, -1));
        tvTitleBold.setTextColor(Color.parseColor(config.getColorIcon()));
    }

    public ViewToolbar getViewToolbar() {
        return viewToolbar;
    }

    public TextView getTvContinue() {
        return tvContinue;
    }
}
