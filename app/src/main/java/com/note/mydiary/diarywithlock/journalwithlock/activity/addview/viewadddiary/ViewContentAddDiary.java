package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewadddiary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsBitmap;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsTheme;

public class ViewContentAddDiary extends RelativeLayout {

    static int w;
    ImageView ivPickEmoji;
    ViewPickDate viewPickDate;
    WriteTitleDiary writeTitle;
    WriteContentDiary writeContent;

    public ViewContentAddDiary(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        setBackgroundColor(getResources().getColor(R.color.trans));
        init(context);
    }

    @SuppressLint("ResourceType")
    private void init(Context context) {
        ivPickEmoji = new ImageView(context);
        ivPickEmoji.setId(1237);
        ivPickEmoji.setImageBitmap(UtilsBitmap.getBitmapFromAsset(context, "emoji", "emoji_1.png"));
        ivPickEmoji.setBackgroundResource(R.drawable.boder_background_emoji);
        ivPickEmoji.setPadding((int) (1.38f * w / 100), (int) (1.38f * w / 100), (int) (1.38f * w / 100), (int) (1.38f * w / 100));
        addView(ivPickEmoji, new LayoutParams((int) (11.11f * w / 100), (int) (11.11f * w / 100)));

        viewPickDate = new ViewPickDate(context);
        viewPickDate.setBackgroundResource(Utils.effectPressRectangle(context).resourceId);
        LayoutParams paramsPickDate = new LayoutParams((int) (33.33f * w / 100), (int) (13.33f * w / 100));
        paramsPickDate.setMargins((int) (5.55f * w / 100), 0, 0, 0);
        paramsPickDate.addRule(RIGHT_OF, ivPickEmoji.getId());
        addView(viewPickDate, paramsPickDate);

        writeTitle = new WriteTitleDiary(context);
        writeTitle.setId(1238);
        writeTitle.getViewTitle().getTvTitle().setText(getResources().getString(R.string.title));
        writeTitle.getEtContent().setHint(getResources().getString(R.string.hint_add_1));
        LayoutParams paramsWriteTitle = new LayoutParams(-1, -2);
        paramsWriteTitle.setMargins(0, (int) (8.83f * w / 100), 0, 0);
        paramsWriteTitle.addRule(BELOW, ivPickEmoji.getId());
        addView(writeTitle, paramsWriteTitle);

        writeContent = new WriteContentDiary(context);
        LayoutParams paramsWriteContent = new LayoutParams(-1, -2);
        paramsWriteContent.setMargins(0, (int) (5.55f * w / 100), 0, 0);
        paramsWriteContent.addRule(BELOW, writeTitle.getId());
        addView(writeContent, paramsWriteContent);
    }

    public static class ViewPickDate extends RelativeLayout {

        ImageView iv;
        TextView tv, tvDate;

        public ViewPickDate(Context context) {
            super(context);
            init(context);
        }

        @SuppressLint("ResourceType")
        private void init(Context context) {
            iv = new ImageView(context);
            iv.setId(1239);
            iv.setImageResource(R.drawable.ic_calendar);
            LayoutParams paramsImage = new LayoutParams((int) (6.67f * w / 100), (int) (6.67f * w / 100));
            addView(iv, paramsImage);

            tv = new TextView(context);
            tv.setText(getResources().getString(R.string.day));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.89f * w / 100);
            tv.setTextColor(getResources().getColor(R.color.black));
            tv.setTypeface(Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context));
            LayoutParams paramsText = new LayoutParams((int) (7.78f * w / 100), (int) (5.83f * w / 100));
            paramsText.addRule(RIGHT_OF, iv.getId());
            paramsText.setMargins((int) (1.389f * w / 100), 0, 0, 0);
            addView(tv, paramsText);

            tvDate = new TextView(context);
            tvDate.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.389f * w / 100);
            tvDate.setTextColor(getResources().getColor(R.color.text_date));
            tvDate.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
            LayoutParams paramsDate = new LayoutParams(-1, (int) (5.83f * w / 100));
            paramsDate.setMargins(0, (int) (0.83f * w / 100), 0, 0);
            paramsDate.addRule(BELOW, iv.getId());
            addView(tvDate, paramsDate);
        }

        public ImageView getIv() {
            return iv;
        }

        public TextView getTv() {
            return tv;
        }

        public TextView getTvDate() {
            return tvDate;
        }
    }

    public static class WriteTitleDiary extends LinearLayout {

        EditText etContent;
        ViewTitle viewTitle;

        public WriteTitleDiary(Context context) {
            super(context);
            setOrientation(VERTICAL);
            setBackgroundColor(getResources().getColor(R.color.trans));
            init(context);
        }

        private void init(Context context) {
            viewTitle = new ViewTitle(context);
            addView(viewTitle, new LayoutParams(-2, -2));

            etContent = new EditText(context);
            etContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
            etContent.setBackgroundColor(getResources().getColor(R.color.trans));
            etContent.setTextColor(getResources().getColor(R.color.black));
            etContent.setHintTextColor(getResources().getColor(R.color.gray_2));
            etContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.89f * w / 100);
            etContent.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
            LayoutParams paramsContent = new LayoutParams(-1, -2);
            addView(etContent, paramsContent);

            etContent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    viewTitle.setLength(s.length());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        public static class ViewTitle extends LinearLayout {

            int length;
            TextView tvTitle, tvLengthText;

            public ViewTitle(Context context) {
                super(context);
                length = 0;
                setOrientation(HORIZONTAL);
                init(context);
            }

            @SuppressLint("SetTextI18n")
            private void init(Context context) {
                tvTitle = new TextView(context);
                tvTitle.setText(getResources().getString(R.string.title));
                tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.89f * w / 100);
                tvTitle.setTextColor(getResources().getColor(R.color.black));
                tvTitle.setTypeface(Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context));
                addView(tvTitle, new LayoutParams(-2, -2));

                tvLengthText = new TextView(context);
                tvLengthText.setText("(" + length + "/100)");
                tvLengthText.setGravity(Gravity.CENTER);
                tvLengthText.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
                tvLengthText.setTextColor(getResources().getColor(R.color.gray_2));
                tvLengthText.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
                LayoutParams paramsLengthText = new LayoutParams(-2, -2);
                paramsLengthText.setMargins((int) (1.11f * w / 100), 0, 0, 0);
                paramsLengthText.gravity = Gravity.CENTER_VERTICAL;
                addView(tvLengthText, paramsLengthText);
            }

            public TextView getTvTitle() {
                return tvTitle;
            }

            @SuppressLint("SetTextI18n")
            public void setLength(int length) {
                tvLengthText.setText("(" + length + "/100)");
            }
        }

        public ViewTitle getViewTitle() {
            return viewTitle;
        }

        public EditText getEtContent() {
            return etContent;
        }
    }

    public static class WriteContentDiary extends LinearLayout {

        TextView tvTitle;
        RecyclerView rcvContent;

        public WriteContentDiary(Context context) {
            super(context);
            setOrientation(VERTICAL);
            init(context);
        }

        private void init(Context context) {
            tvTitle = new TextView(context);
            tvTitle.setText(getResources().getString(R.string.content));
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.89f * w / 100);
            tvTitle.setTextColor(getResources().getColor(R.color.black));
            tvTitle.setTypeface(Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context));
            addView(tvTitle, new LayoutParams(-2, -2));

            rcvContent = new RecyclerView(context);
            LayoutParams paramsRcv = new LayoutParams(-1, -1);
            paramsRcv.setMargins(0, 0, 0, (int) (5.556f * w / 100));
            addView(rcvContent, paramsRcv);
        }

        public TextView getTvTitle() {
            return tvTitle;
        }

        public RecyclerView getRcvContent() {
            return rcvContent;
        }
    }

    public void createTheme(Context context, String nameTheme) {
        if (nameTheme.equals("default")) return;

        String jsonConfig = Utils.readFromFile(context, "theme/theme_app/" + nameTheme + "/config.txt");

        ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

        if (config == null) return;

        ivPickEmoji.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorMainLight())}, 2 * w / 100, -1, -1));

        viewPickDate.getTv().setTextColor(Color.parseColor(config.getColorIcon()));
        viewPickDate.getTvDate().setTextColor(Color.parseColor(config.getColorTextDate()));
        UtilsTheme.changeIconCalendar(context, "calendar", 5, R.drawable.ic_calendar, viewPickDate.getIv(),
                Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));

        writeTitle.getViewTitle().getTvTitle().setTextColor(Color.parseColor(config.getColorIcon()));
        writeTitle.getEtContent().setTextColor(Color.parseColor(config.getColorIcon()));

        writeContent.getTvTitle().setTextColor(Color.parseColor(config.getColorIcon()));
    }

    public ImageView getIvPickEmoji() {
        return ivPickEmoji;
    }

    public ViewPickDate getViewPickDate() {
        return viewPickDate;
    }

    public WriteTitleDiary getWriteTitle() {
        return writeTitle;
    }

    public WriteContentDiary getWriteContent() {
        return writeContent;
    }
}
