package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsTheme;
import com.note.remiads.native_ads.ViewNativeAds;

public class ViewMenuNavigation extends LinearLayout {

    static int w;
    TextView tvCustomize, tvAbout;
    ViewOptionMenu viewChangeTheme, viewPasscode, changePasscode, viewTheme, viewNotification, viewWidget, viewCover, viewRate,
            viewDownload, viewInsta, viewFacebook, viewFeedback, viewPP;

    public ViewMenuNavigation(Context context) {
        super(context);
        setBackgroundColor(getResources().getColor(R.color.trans));
        setOrientation(LinearLayout.VERTICAL);
        w = getResources().getDisplayMetrics().widthPixels;
        init(context);
    }

    private void init(Context context) {
        LayoutParams paramsTitle = new LayoutParams(-2, -2);
        paramsTitle.setMargins(5 * w / 100, 2 * w / 100, 0, 0);

        tvCustomize = new TextView(context);
        tvCustomize.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3 * w / 100f);
        tvCustomize.setText(getResources().getString(R.string.customize));
        tvCustomize.setTextColor(getResources().getColor(R.color.gray_2));
        tvCustomize.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        addView(tvCustomize, paramsTitle);

        LayoutParams paramsMenu = new LayoutParams(-1, -2);
        paramsMenu.setMargins(0, 2 * w / 100, 0, 0);

        viewChangeTheme = new ViewOptionMenu(context);
        viewChangeTheme.getIvOption().setImageResource(R.drawable.ic_change_theme);
        viewChangeTheme.getTvOption().setText(getResources().getString(R.string.change_theme));
        viewChangeTheme.setBackgroundResource(Utils.effectPressRectangle(context).resourceId);
        addView(viewChangeTheme, paramsMenu);

        viewPasscode = new ViewOptionMenu(context);
        viewPasscode.getIvOption().setImageResource(R.drawable.ic_set_passcode);
        viewPasscode.getTvOption().setText(getResources().getString(R.string.menu_0));
        viewPasscode.setBackgroundResource(Utils.effectPressRectangle(context).resourceId);
        addView(viewPasscode, paramsMenu);

        changePasscode = new ViewOptionMenu(context);
        changePasscode.getIvOption().setImageResource(R.drawable.ic_passcode);
        changePasscode.getTvOption().setText(getResources().getString(R.string.menu_1));
        changePasscode.setBackgroundResource(Utils.effectPressRectangle(context).resourceId);
        addView(changePasscode, paramsMenu);

        viewTheme = new ViewOptionMenu(context);
        viewTheme.getIvOption().setImageResource(R.drawable.ic_theme);
        viewTheme.getTvOption().setText(getResources().getString(R.string.menu_2));
        viewTheme.setBackgroundResource(Utils.effectPressRectangle(context).resourceId);
        addView(viewTheme, paramsMenu);

        viewNotification = new ViewOptionMenu(context);
        viewNotification.getIvOption().setImageResource(R.drawable.ic_notification_setting);
        viewNotification.getTvOption().setText(getResources().getString(R.string.menu_3));
        viewNotification.setBackgroundResource(Utils.effectPressRectangle(context).resourceId);
        addView(viewNotification, paramsMenu);

        viewWidget = new ViewOptionMenu(context);
        viewWidget.getIvOption().setImageResource(R.drawable.ic_widget);
        viewWidget.getTvOption().setText(getResources().getString(R.string.menu_4));
        viewWidget.setBackgroundResource(Utils.effectPressRectangle(context).resourceId);
        addView(viewWidget, paramsMenu);

        viewCover = new ViewOptionMenu(context);
        viewCover.getIvOption().setImageResource(R.drawable.ic_cover);
        viewCover.getTvOption().setText(getResources().getString(R.string.menu_5));
        viewCover.setBackgroundResource(Utils.effectPressRectangle(context).resourceId);
        addView(viewCover, paramsMenu);

        ViewNativeAds vAds = new ViewNativeAds(context);
        vAds.addAds(((Activity) context), false, com.note.remiads.R.string.na_big);
        LayoutParams pAds = new LayoutParams(-1, -2);
        pAds.setMargins(w / 30, w / 30, w / 30, 0);
        addView(vAds, pAds);

        tvAbout = new TextView(context);
        tvAbout.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3 * w / 100f);
        tvAbout.setText(getResources().getString(R.string.about));
        tvAbout.setTextColor(getResources().getColor(R.color.gray_2));
        tvAbout.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        addView(tvAbout, paramsTitle);

        viewRate = new ViewOptionMenu(context);
        viewRate.getIvOption().setImageResource(R.drawable.ic_rate);
        viewRate.getTvOption().setText(getResources().getString(R.string.menu_6));
        viewRate.setBackgroundResource(Utils.effectPressRectangle(context).resourceId);
        addView(viewRate, paramsMenu);

        viewDownload = new ViewOptionMenu(context);
        viewDownload.getIvOption().setImageResource(R.drawable.ic_dowload);
        viewDownload.getTvOption().setText(getResources().getString(R.string.menu_7));
        viewDownload.setBackgroundResource(Utils.effectPressRectangle(context).resourceId);
        addView(viewDownload, paramsMenu);

        viewInsta = new ViewOptionMenu(context);
        viewInsta.getIvOption().setImageResource(R.drawable.ic_insta);
        viewInsta.getTvOption().setText(getResources().getString(R.string.menu_8));
        viewInsta.setBackgroundResource(Utils.effectPressRectangle(context).resourceId);
        addView(viewInsta, paramsMenu);

        viewFacebook = new ViewOptionMenu(context);
        viewFacebook.getIvOption().setImageResource(R.drawable.ic_facebook);
        viewFacebook.getTvOption().setText(getResources().getString(R.string.menu_9));
        viewFacebook.setBackgroundResource(Utils.effectPressRectangle(context).resourceId);
        addView(viewFacebook, paramsMenu);

        viewFeedback = new ViewOptionMenu(context);
        viewFeedback.getIvOption().setImageResource(R.drawable.ic_feedback);
        viewFeedback.getTvOption().setText(getResources().getString(R.string.menu_10));
        viewFeedback.setBackgroundResource(Utils.effectPressRectangle(context).resourceId);
        addView(viewFeedback, paramsMenu);

        viewPP = new ViewOptionMenu(context);
        viewPP.getIvOption().setImageResource(R.drawable.ic_pp);
        viewPP.getTvOption().setText(getResources().getString(R.string.menu_11));
        viewPP.setBackgroundResource(Utils.effectPressRectangle(context).resourceId);
        addView(viewPP, paramsMenu);

        addView(new View(context), -1, w / 7);
    }

    public static class ViewOptionMenu extends RelativeLayout {

        ImageView ivOption, ivRight;
        TextView tvOption;

        public ViewOptionMenu(Context context) {
            super(context);
            init(context);
        }

        @SuppressLint("ResourceType")
        private void init(Context context) {
            ivOption = new ImageView(context);
            ivOption.setId(3458);
//            ivOption.setPadding(w / 100, w / 100, w / 100, w / 100);
            LayoutParams paramsImageOption = new LayoutParams((int) (8.33f * w / 100), (int) (8.33f * w / 100));
            paramsImageOption.setMargins((int) (5.5f * w / 100), 0, 0, 0);
            paramsImageOption.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
            addView(ivOption, paramsImageOption);

            tvOption = new TextView(context);
            tvOption.setText(context.getResources().getString(R.string.menu));
            tvOption.setTextColor(context.getResources().getColor(R.color.black));
            tvOption.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 3.889f / 100);
            tvOption.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));

            LayoutParams paramsTextOption = new LayoutParams(-2, -2);
            paramsTextOption.addRule(RelativeLayout.RIGHT_OF, ivOption.getId());
            paramsTextOption.setMargins((int) (6 * w / 100), 0, 0, 0);
            paramsTextOption.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            addView(tvOption, paramsTextOption);

            ivRight = new ImageView(context);
            ivRight.setImageResource(R.drawable.ic_right);
            ivRight.setPadding(2 * w / 100, 2 * w / 100, 2 * w / 100, 2 * w / 100);
            LayoutParams paramsVip = new LayoutParams(9 * w / 100, 9 * w / 100);
            paramsVip.setMargins(0, 0, 6 * w / 100, 0);
            paramsVip.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
            addView(ivRight, paramsVip);
        }

        public ImageView getIvRight() {
            return ivRight;
        }

        public ImageView getIvOption() {
            return ivOption;
        }

        public TextView getTvOption() {
            return tvOption;
        }
    }

    public void createTheme(Context context, String nameTheme) {
        if (nameTheme.equals("default")) return;

        String jsonConfig = Utils.readFromFile(context, "theme/theme_app/" + nameTheme + "/config.txt");

        ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

        if (config == null) return;

        tvCustomize.setTextColor(Color.parseColor(config.getColorUnSelect()));
        tvAbout.setTextColor(Color.parseColor(config.getColorUnSelect()));

        UtilsTheme.changeIcon(context,
                "changetheme", 2, R.drawable.ic_change_theme, viewChangeTheme.getIvOption(),
                Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorIconInColor()));

        UtilsTheme.changeIcon(context,
                "passcode", 2, R.drawable.ic_set_passcode, viewPasscode.getIvOption(),
                Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorIconInColor()));

        UtilsTheme.changeIcon(context,
                "theme", 2, R.drawable.ic_theme, viewTheme.getIvOption(),
                Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorIconInColor()));

        UtilsTheme.changeIcon(context,
                "notification", 4, R.drawable.ic_notification_setting, viewNotification.getIvOption(),
                Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorIconInColor()));

        UtilsTheme.changeIcon(context,
                "widget", 5, R.drawable.ic_widget, viewWidget.getIvOption(),
                Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorIconInColor()));

        UtilsTheme.changeIcon(context,
                "cover", 8, R.drawable.ic_cover, viewCover.getIvOption(),
                Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorIconInColor()));

        viewChangeTheme.getTvOption().setTextColor(Color.parseColor(config.getColorIcon()));
        viewPasscode.getTvOption().setTextColor(Color.parseColor(config.getColorIcon()));
        changePasscode.getTvOption().setTextColor(Color.parseColor(config.getColorIcon()));
        viewTheme.getTvOption().setTextColor(Color.parseColor(config.getColorIcon()));
        viewNotification.getTvOption().setTextColor(Color.parseColor(config.getColorIcon()));
        viewWidget.getTvOption().setTextColor(Color.parseColor(config.getColorIcon()));
        viewCover.getTvOption().setTextColor(Color.parseColor(config.getColorIcon()));
        viewRate.getTvOption().setTextColor(Color.parseColor(config.getColorIcon()));
        viewDownload.getTvOption().setTextColor(Color.parseColor(config.getColorIcon()));
        viewInsta.getTvOption().setTextColor(Color.parseColor(config.getColorIcon()));
        viewFacebook.getTvOption().setTextColor(Color.parseColor(config.getColorIcon()));
        viewFeedback.getTvOption().setTextColor(Color.parseColor(config.getColorIcon()));
        viewPP.getTvOption().setTextColor(Color.parseColor(config.getColorIcon()));

        UtilsTheme.changeIcon(context, "nextMonth", 1, R.drawable.button_next_month_selector,
                viewChangeTheme.getIvRight(), Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));
        UtilsTheme.changeIcon(context, "nextMonth", 1, R.drawable.button_next_month_selector,
                viewPasscode.getIvRight(), Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));
        UtilsTheme.changeIcon(context, "nextMonth", 1, R.drawable.button_next_month_selector,
                changePasscode.getIvRight(), Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));
        UtilsTheme.changeIcon(context, "nextMonth", 1, R.drawable.button_next_month_selector,
                viewTheme.getIvRight(), Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));
        UtilsTheme.changeIcon(context, "nextMonth", 1, R.drawable.button_next_month_selector,
                viewNotification.getIvRight(), Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));
        UtilsTheme.changeIcon(context, "nextMonth", 1, R.drawable.button_next_month_selector,
                viewWidget.getIvRight(), Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));
        UtilsTheme.changeIcon(context, "nextMonth", 1, R.drawable.button_next_month_selector,
                viewCover.getIvRight(), Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));
        UtilsTheme.changeIcon(context, "nextMonth", 1, R.drawable.button_next_month_selector,
                viewRate.getIvRight(), Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));
        UtilsTheme.changeIcon(context, "nextMonth", 1, R.drawable.button_next_month_selector,
                viewDownload.getIvRight(), Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));
        UtilsTheme.changeIcon(context, "nextMonth", 1, R.drawable.button_next_month_selector,
                viewInsta.getIvRight(), Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));
        UtilsTheme.changeIcon(context, "nextMonth", 1, R.drawable.button_next_month_selector,
                viewFacebook.getIvRight(), Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));
        UtilsTheme.changeIcon(context, "nextMonth", 1, R.drawable.button_next_month_selector,
                viewFeedback.getIvRight(), Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));
        UtilsTheme.changeIcon(context, "nextMonth", 1, R.drawable.button_next_month_selector,
                viewPP.getIvRight(), Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));
    }

    public ViewOptionMenu getViewChangeTheme() {
        return viewChangeTheme;
    }

    public ViewOptionMenu getViewPasscode() {
        return viewPasscode;
    }

    public ViewOptionMenu getChangePasscode() {
        return changePasscode;
    }

    public ViewOptionMenu getViewTheme() {
        return viewTheme;
    }

    public ViewOptionMenu getViewNotification() {
        return viewNotification;
    }

    public ViewOptionMenu getViewWidget() {
        return viewWidget;
    }

    public ViewOptionMenu getViewCover() {
        return viewCover;
    }

    public ViewOptionMenu getViewRate() {
        return viewRate;
    }

    public ViewOptionMenu getViewDownload() {
        return viewDownload;
    }

    public ViewOptionMenu getViewInsta() {
        return viewInsta;
    }

    public ViewOptionMenu getViewFacebook() {
        return viewFacebook;
    }

    public ViewOptionMenu getViewFeedback() {
        return viewFeedback;
    }

    public ViewOptionMenu getViewPP() {
        return viewPP;
    }
}
