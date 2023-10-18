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
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsTheme;

public class ViewVipTwo extends RelativeLayout {

    static int w;
    ImageView ivExit, ivBackground2, ivBackground3;
    ViewItem viewItem1, viewItem2, viewItem3;
    TextView tvUpgrade;

    public ViewVipTwo(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        setBackgroundResource(R.drawable.im_background_2);
        setClickable(true);
        setFocusable(true);
        init(context);
    }

    @SuppressLint("ResourceType")
    private void init(Context context) {
        TextView tvDes = new TextView(context);
        tvDes.setId(1442);
        tvDes.setGravity(Gravity.CENTER_HORIZONTAL);
        tvDes.setText(getResources().getString(R.string.subscription_will_auto_renew_cancel_anytime));
        tvDes.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        tvDes.setTextSize(TypedValue.COMPLEX_UNIT_PX, 2.884f * w / 100);
        tvDes.setTextColor(getResources().getColor(R.color.gray_2));
        LayoutParams paramsTextDes = new LayoutParams(-1, -2);
        paramsTextDes.setMargins((int) (5.556f * w / 100), 0, (int) (5.556f * w / 100), 0);
        paramsTextDes.addRule(CENTER_HORIZONTAL, TRUE);
        paramsTextDes.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        addView(tvDes, paramsTextDes);

        tvUpgrade = new TextView(context);
        tvUpgrade.setId(1443);
        tvUpgrade.setGravity(Gravity.CENTER);
        tvUpgrade.setBackgroundResource(R.drawable.border_orange);
        tvUpgrade.setText(getResources().getString(R.string.upgrade_premium));
        tvUpgrade.setTextColor(getResources().getColor(R.color.white));
        tvUpgrade.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        tvUpgrade.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.389f * w / 100);
        LayoutParams paramsTextContinue = new LayoutParams(-1, (int) (11.11f * w / 100));
        paramsTextContinue.setMargins((int) (5.556f * w / 100), 0, (int) (5.556f * w / 100), (int) (1.667f * w / 100));
        paramsTextContinue.addRule(CENTER_HORIZONTAL, TRUE);
        paramsTextContinue.addRule(ABOVE, tvDes.getId());
        addView(tvUpgrade, paramsTextContinue);

        LinearLayout llContent = new LinearLayout(context);
        llContent.setOrientation(LinearLayout.VERTICAL);
        LayoutParams paramsContent = new LayoutParams(-1, -1);
        paramsContent.addRule(CENTER_HORIZONTAL, TRUE);
        paramsContent.addRule(ABOVE, tvUpgrade.getId());
        paramsContent.setMargins(0, (int) (5.556f * w / 100), 0, (int) (6.11f * w / 100));

        ImageView ivBackground = new ImageView(context);
        ivBackground.setImageResource(R.drawable.im_vip_2);
        LinearLayout.LayoutParams paramsImageBackground = new LinearLayout.LayoutParams((int) (40.556f * w / 100), (int) (33.884f * w / 100));
        paramsImageBackground.gravity = Gravity.CENTER_HORIZONTAL;
        llContent.addView(ivBackground, paramsImageBackground);

        ivBackground2 = new ImageView(context);
        ivBackground2.setImageResource(R.drawable.im_vip_3);
        LinearLayout.LayoutParams paramsImageBackground2 = new LinearLayout.LayoutParams((int) (78.05f * w / 100), (int) (18.884f * w / 100));
        paramsImageBackground2.setMargins(0, (int) (7.22f * w / 100), 0, 0);
        paramsImageBackground2.gravity = Gravity.CENTER_HORIZONTAL;
        llContent.addView(ivBackground2, paramsImageBackground2);

        ivBackground3 = new ImageView(context);
        ivBackground3.setImageResource(R.drawable.im_vip_4);
        LinearLayout.LayoutParams paramsImageBackground3 = new LinearLayout.LayoutParams((int) (80.556f * w / 100), (int) (22.884f * w / 100));
        paramsImageBackground3.setMargins(0, (int) (4.44f * w / 100), 0, 0);
        paramsImageBackground3.gravity = Gravity.CENTER_HORIZONTAL;
        llContent.addView(ivBackground3, paramsImageBackground3);

        LinearLayout.LayoutParams paramsOption = new LinearLayout.LayoutParams(-1, (int) (13.889f * w / 100));
        paramsOption.setMargins((int) (5.556f * w / 100), (int) (4.44f * w / 100), (int) (5.556f * w / 100), 0);

        viewItem1 = new ViewItem(context);
        viewItem1.getIvChoose().setImageResource(R.drawable.ic_un_selected);
        viewItem1.getTv().setText(getResources().getString(R.string.yearly));
        viewItem1.getTvPrice().setText(getResources().getString(R.string.price_yearly));
        llContent.addView(viewItem1, paramsOption);

        viewItem2 = new ViewItem(context);
        viewItem2.getIvChoose().setImageResource(R.drawable.ic_selected);
        viewItem2.getTv().setText(getResources().getString(R.string.monthly));
        viewItem2.getTvPrice().setText(getResources().getString(R.string.price_yearly));
        llContent.addView(viewItem2, paramsOption);

        viewItem3 = new ViewItem(context);
        viewItem3.getIvChoose().setImageResource(R.drawable.ic_un_selected);
        viewItem3.getTv().setText(getResources().getString(R.string.weekly));
        viewItem3.getTvPrice().setText(getResources().getString(R.string.price_yearly));
        llContent.addView(viewItem3, paramsOption);

//        NestedScrollView nestedScrollView = new NestedScrollView(context);
//        nestedScrollView.addView(llContent, new LayoutParams(-1, -1));

        addView(llContent, paramsContent);

        ivExit = new ImageView(context);
        ivExit.setImageResource(R.drawable.ic_exit);
        LayoutParams paramsImageExit = new LayoutParams((int) (6.667f * w / 100), (int) (6.667f * w / 100));
        paramsImageExit.addRule(ALIGN_PARENT_END, TRUE);
        paramsImageExit.setMargins(0, (int) (13.889f * w / 100), (int) (5.556f * w / 100), 0);
        addView(ivExit, paramsImageExit);
    }

    public class ViewItem extends RelativeLayout {

        ImageView ivChoose;
        TextView tv, tvPrice;

        @SuppressLint("ResourceType")
        public ViewItem(Context context) {
            super(context);
            setBackgroundResource(R.drawable.border_stroke_orange);

            ivChoose = new ImageView(context);
            ivChoose.setId(1444);
            LayoutParams paramsImage = new LayoutParams((int) (5.556f * w / 100), (int) (5.556f * w / 100));
            paramsImage.setMargins((int) (5.556f * w / 100), 0, 0, 0);
            paramsImage.addRule(CENTER_VERTICAL, TRUE);
            addView(ivChoose, paramsImage);

            tv = new TextView(context);
            tv.setTextColor(getResources().getColor(R.color.text_menu));
            tv.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.389f * w / 100);
            LayoutParams paramsTextTitle = new LayoutParams(-2, -2);
            paramsTextTitle.addRule(CENTER_VERTICAL, TRUE);
            paramsTextTitle.addRule(RIGHT_OF, ivChoose.getId());
            paramsTextTitle.setMargins((int) (5.556f * w / 100), 0, 0, 0);
            addView(tv, paramsTextTitle);

            tvPrice = new TextView(context);
            tvPrice.setTextColor(getResources().getColor(R.color.black));
            tvPrice.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
            tvPrice.setTextSize(TypedValue.COMPLEX_UNIT_PX, 2.778f * w / 100);
            LayoutParams paramsTextPrice = new LayoutParams(-2, -2);
            paramsTextPrice.addRule(CENTER_VERTICAL, TRUE);
            paramsTextPrice.addRule(ALIGN_PARENT_END, TRUE);
            paramsTextPrice.setMargins(0, 0, (int) (2.778f * w / 100), 0);
            addView(tvPrice, paramsTextPrice);
        }

        public ImageView getIvChoose() {
            return ivChoose;
        }

        public TextView getTv() {
            return tv;
        }

        public TextView getTvPrice() {
            return tvPrice;
        }
    }

    public void createTheme(Context context, String nameTheme) {
        if (nameTheme.equals("default")) return;

        String jsonConfig = Utils.readFromFile(context, "theme/theme_app/" + nameTheme + "/config.txt");

        ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

        if (config == null) return;

        Bitmap bmBg = BitmapFactory.decodeFile(Utils.getStore(context) + "/theme/theme_app/" + nameTheme + "/background_vip_2.png");
        if (bmBg == null) setBackgroundColor(getResources().getColor(R.color.white));
        else setBackground(new BitmapDrawable(getResources(), bmBg));

        ivBackground2.setImageBitmap(BitmapFactory.decodeFile(Utils.getStore(context) + "/theme/theme_app/" + nameTheme + "/image_vip_2_1.png"));
        ivBackground3.setImageBitmap(BitmapFactory.decodeFile(Utils.getStore(context) + "/theme/theme_app/" + nameTheme + "/image_vip_2_2.png"));

        viewItem1.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorBackground())}, (int) (2.5f * w / 100), 2, Color.parseColor(config.getColorMain())));
        viewItem2.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorBackground())}, (int) (2.5f * w / 100), 2, Color.parseColor(config.getColorMain())));
        viewItem3.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorBackground())}, (int) (2.5f * w / 100), 2, Color.parseColor(config.getColorMain())));

        UtilsTheme.changeIconSelectVip(context, viewItem1.getIvChoose(), Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorUnSelect()), false);
        UtilsTheme.changeIconSelectVip(context, viewItem2.getIvChoose(), Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorUnSelect()), true);
        UtilsTheme.changeIconSelectVip(context, viewItem3.getIvChoose(), Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorUnSelect()), false);

        tvUpgrade.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorMain())}, (int) (2.5f * w / 100), -1, -1));

        viewItem1.getTv().setTextColor(Color.parseColor(config.getColorIcon()));
        viewItem2.getTv().setTextColor(Color.parseColor(config.getColorIcon()));
        viewItem3.getTv().setTextColor(Color.parseColor(config.getColorIcon()));

        viewItem1.getTvPrice().setTextColor(Color.parseColor(config.getColorIcon()));
        viewItem2.getTvPrice().setTextColor(Color.parseColor(config.getColorIcon()));
        viewItem3.getTvPrice().setTextColor(Color.parseColor(config.getColorIcon()));
    }

    public ImageView getIvExit() {
        return ivExit;
    }

    public ViewItem getViewItem1() {
        return viewItem1;
    }

    public ViewItem getViewItem2() {
        return viewItem2;
    }

    public ViewItem getViewItem3() {
        return viewItem3;
    }

    public TextView getTvUpgrade() {
        return tvUpgrade;
    }
}
