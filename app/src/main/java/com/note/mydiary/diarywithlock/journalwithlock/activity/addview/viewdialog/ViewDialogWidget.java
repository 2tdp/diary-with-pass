package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewdialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsTheme;

public class ViewDialogWidget extends RelativeLayout {

    static int w;
    ViewItemDialogWidget viewDiary, viewCustomWidget;

    public ViewDialogWidget(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        setBackgroundResource(R.drawable.boder_dialog);
        setPadding(0, (int) (4.72f * w / 100), 0, (int) (4.72f * w / 100));
        init(context);
    }

    @SuppressLint("ResourceType")
    private void init(Context context) {
        TextView tvTitle = new TextView(context);
        tvTitle.setId(1221);
        tvTitle.setText(getResources().getString(R.string.share));
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.889f * w / 100);
        tvTitle.setTypeface(Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context));
        tvTitle.setTextColor(getResources().getColor(R.color.black));
        LayoutParams paramsTitle = new LayoutParams(-2, -2);
        paramsTitle.addRule(CENTER_HORIZONTAL, TRUE);
        addView(tvTitle, paramsTitle);

        viewDiary = new ViewItemDialogWidget(context);
        viewDiary.setId(1222);
        viewDiary.getIv().setImageResource(R.drawable.ic_view_diary);
        viewDiary.getTvTitle().setText(getResources().getString(R.string.view_diary));
        viewDiary.getTvDes().setText(getResources().getString(R.string.open_the_diary_you_pinned_in_the_widget));
        LayoutParams paramsViewDiary = new LayoutParams(-1, (int) (15.56f * w / 100));
        paramsViewDiary.addRule(CENTER_HORIZONTAL, TRUE);
        paramsViewDiary.addRule(BELOW, tvTitle.getId());
        paramsViewDiary.setMargins((int) (5.83f * w / 100), (int) (2.5f * w / 100), (int) (5.83f * w / 100), 0);

        addView(viewDiary, paramsViewDiary);

        viewCustomWidget = new ViewItemDialogWidget(context);
        viewCustomWidget.getIv().setImageResource(R.drawable.ic_custom_widget);
        viewCustomWidget.getTvTitle().setText(getResources().getString(R.string.custom_widget));
        viewCustomWidget.getTvDes().setText(getResources().getString(R.string.select_the_diary_to_display_on_the_widget));
        LayoutParams paramsCustom = new LayoutParams(-1, (int) (15.56f * w / 100));
        paramsCustom.addRule(CENTER_HORIZONTAL, TRUE);
        paramsCustom.addRule(BELOW, viewDiary.getId());
        paramsCustom.setMargins((int) (5.83f * w / 100), (int) (5.25f * w / 100), (int) (5.83f * w / 100), 0);

        addView(viewCustomWidget, paramsCustom);

        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
            String jsonConfig = Utils.readFromFile(context, "theme/theme_app/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP) + "/config.txt");

            ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

            if (config == null) return;

            viewDiary.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorMainLight())}, (int) (2.5f * w / 100), -1, -1));
            viewDiary.getTvTitle().setTextColor(Color.parseColor(config.getColorIcon()));
            UtilsTheme.changeIcon(context, "viewdiary", 1, R.drawable.ic_view_diary, viewDiary.getIv(),
                    Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));

            viewCustomWidget.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorMainLight())}, (int) (2.5f * w / 100), -1, -1));
            viewCustomWidget.getTvTitle().setTextColor(Color.parseColor(config.getColorIcon()));
            UtilsTheme.changeIcon(context, "customwidget", 2, R.drawable.ic_custom_widget, viewCustomWidget.getIv(),
                    Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));
        }
    }

    public class ViewItemDialogWidget extends RelativeLayout {

        ImageView iv;
        TextView tvTitle, tvDes;

        @SuppressLint("ResourceType")
        public ViewItemDialogWidget(Context context) {
            super(context);
            setBackgroundResource(R.drawable.border_item_diary_home);

            iv = new ImageView(context);
            iv.setId(1221);
            LayoutParams paramsImageViewDiary = new LayoutParams((int) (8.889f * w / 100), (int) (8.889f * w / 100));
            paramsImageViewDiary.addRule(CENTER_VERTICAL, TRUE);
            paramsImageViewDiary.setMargins((int) (3.056f * w / 100), 0, 0, 0);
            addView(iv, paramsImageViewDiary);

            LinearLayout llText = new LinearLayout(context);
            llText.setOrientation(LinearLayout.VERTICAL);
            llText.setGravity(Gravity.CENTER_VERTICAL);

            tvTitle = new TextView(context);
            tvTitle.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
            tvTitle.setTextColor(getResources().getColor(R.color.black));
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
            llText.addView(tvTitle);

            tvDes = new TextView(context);
            tvDes.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
            tvDes.setTextColor(getResources().getColor(R.color.gray_2));
            tvDes.setTextSize(TypedValue.COMPLEX_UNIT_PX, 2.556f * w / 100);
            llText.addView(tvDes);

            LayoutParams params = new LayoutParams(-1, -1);
            params.addRule(CENTER_VERTICAL, TRUE);
            params.addRule(RIGHT_OF, iv.getId());
            params.setMargins((int) (3.33f * w / 100), 0, 0, 0);
            addView(llText, params);
        }

        public ImageView getIv() {
            return iv;
        }

        public TextView getTvTitle() {
            return tvTitle;
        }

        public TextView getTvDes() {
            return tvDes;
        }
    }

    public ViewItemDialogWidget getViewDiary() {
        return viewDiary;
    }

    public ViewItemDialogWidget getViewCustomWidget() {
        return viewCustomWidget;
    }
}
