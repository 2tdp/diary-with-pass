package com.note.mydiary.diarywithlock.journalwithlock.activity.addview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsTheme;

public class ViewContent extends LinearLayout {

    static int w;
    RecyclerView rcvPic;
    TextView tvDate, tvTitle, tvContent, tvRec;
    ImageView ivMore, ivEmoji, ivRec, ivWave;
    LinearLayout llRec;

    public ViewContent(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        setOrientation(VERTICAL);
        setBackgroundResource(R.drawable.border_item_diary_home);
        setPadding(3 * w / 100, 2 * w / 100, 3 * w / 100, 2 * w / 100);
        init(context);
    }

    @SuppressLint("ResourceType")
    private void init(Context context) {
        RelativeLayout rlTop = new RelativeLayout(context);

        tvDate = new TextView(context);
        tvDate.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
        tvDate.setTextColor(getResources().getColor(R.color.text_date));
        tvDate.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        RelativeLayout.LayoutParams paramsTextDate = new RelativeLayout.LayoutParams(-2, -2);
        paramsTextDate.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTextDate.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        rlTop.addView(tvDate, paramsTextDate);

        ivMore = new ImageView(context);
        ivMore.setImageResource(R.drawable.ic_more_horiziontal);
        ivMore.setPadding(w / 100, w / 100, w / 100, w / 100);
        RelativeLayout.LayoutParams paramsImageMore = new RelativeLayout.LayoutParams(-2, -1);
        paramsImageMore.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsImageMore.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        rlTop.addView(ivMore, paramsImageMore);

        addView(rlTop, new LayoutParams(-1, (int) (5.556f * w / 100)));

        RelativeLayout rlCenter = new RelativeLayout(context);

        ivEmoji = new ImageView(context);
        ivEmoji.setId(1236);
        RelativeLayout.LayoutParams paramsEmoji = new RelativeLayout.LayoutParams((int) (8.33f * w / 100), (int) (8.33f * w / 100));
        paramsEmoji.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        paramsEmoji.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        rlCenter.addView(ivEmoji, paramsEmoji);

        LinearLayout llText = new LinearLayout(context);
        llText.setOrientation(VERTICAL);

        tvTitle = new TextView(context);
        tvTitle.setEllipsize(TextUtils.TruncateAt.END);
        tvTitle.setMarqueeRepeatLimit(-1);
        tvTitle.setHorizontallyScrolling(true);
        tvTitle.setSingleLine(true);
        tvTitle.setSelected(true);
        tvTitle.setTypeface(Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context));
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.889f * w / 100);
        tvTitle.setTextColor(getResources().getColor(R.color.black));
        llText.addView(tvTitle, new LayoutParams(-1, -2));

        tvContent = new TextView(context);
        tvContent.setEllipsize(TextUtils.TruncateAt.END);
        tvContent.setMarqueeRepeatLimit(-1);
        tvContent.setHorizontallyScrolling(true);
        tvContent.setSingleLine(true);
        tvContent.setSelected(true);
        tvContent.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
        tvContent.setTextColor(getResources().getColor(R.color.black));
        llText.addView(tvContent, new LayoutParams(-1, -2));

        RelativeLayout.LayoutParams paramsText = new RelativeLayout.LayoutParams(-1, -1);
        paramsText.setMargins(0, 0, (int) (5.556f * w / 100), 0);
        paramsText.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        paramsText.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsText.addRule(RelativeLayout.LEFT_OF, ivEmoji.getId());
        rlCenter.addView(llText, paramsText);

        addView(rlCenter, new LayoutParams(-1, (int) (11.11f * w / 100)));

        llRec = new LinearLayout(context);
        llRec.setVisibility(GONE);
        llRec.setOrientation(HORIZONTAL);
        llRec.setBackgroundResource(R.drawable.border_stroke);
        llRec.setGravity(Gravity.CENTER_VERTICAL);
        llRec.setPadding(w / 100, w / 100, w / 100, w / 100);

        ivRec = new ImageView(context);
        ivRec.setImageResource(R.drawable.ic_volume);
        LayoutParams paramsIvRec = new LayoutParams((int) (3.89f * w / 100), (int) (3.89f * w / 100));
        paramsIvRec.setMargins((int) (1.944f * w / 100), 0, 0, 0);
        llRec.addView(ivRec, paramsIvRec);

        tvRec = new TextView(context);
        tvRec.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvRec.setMarqueeRepeatLimit(-1);
        tvRec.setHorizontallyScrolling(true);
        tvRec.setSingleLine(true);
        tvRec.setSelected(true);
        tvRec.setTextColor(getResources().getColor(R.color.orange));
        tvRec.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        tvRec.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
        LayoutParams paramsTvRec = new LayoutParams((int) (30.556f * w / 100), (int) (5.556f * w / 100));
        paramsTvRec.setMargins((int) (1.389f * w / 100), 0, 0, 0);
        llRec.addView(tvRec, paramsTvRec);

        ivWave = new ImageView(context);
        ivWave.setVisibility(GONE);
        ivWave.setImageResource(R.drawable.im_wave_audio);
        llRec.addView(ivWave, paramsTvRec);

        LayoutParams paramsAudio = new LayoutParams(-2, (int) (7.22f * w / 100));
        paramsAudio.setMargins(0, (int) (2.278f * w / 100), 0, 0);
        addView(llRec, paramsAudio);

        rcvPic = new RecyclerView(context);
        addView(rcvPic, -1, -2);
    }

    public void setParams() {
        LayoutParams paramsContent = new LayoutParams(-1, -2);
        paramsContent.setMargins(0, (int) (2.778f * w / 100), 0, 0);
        this.setLayoutParams(paramsContent);
    }

    public void createTheme(Context context, String nameTheme) {
        if (nameTheme.equals("default")) return;

        String jsonConfig = Utils.readFromFile(context, "theme/theme_app/" + nameTheme + "/config.txt");

        ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

        if (config == null) return;

        setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorMainLight())}, (int) (3f * w / 100), -1, -1));

        tvDate.setTextColor(Color.parseColor(config.getColorTextDate()));
        tvTitle.setTextColor(Color.parseColor(config.getColorIcon()));
        tvContent.setTextColor(Color.parseColor(config.getColorIcon()));
        llRec.setBackground(Utils.createBackground(new int[]{-1}, (int) (2.5f * w / 100), 2, Color.parseColor(config.getColorMain())));
        tvRec.setTextColor(Color.parseColor(config.getColorTextDate()));
        UtilsTheme.changeIconMore(context,
                "more", 3, R.drawable.ic_more_horiziontal, ivMore,
                Color.parseColor(config.getColorIconMore()), Color.parseColor(config.getColorIconMore()));
        UtilsTheme.changeIcon(context,
                "wave", 20, R.drawable.im_wave_audio, ivWave,
                Color.parseColor(config.getColorTextDate()), Color.parseColor(config.getColorTextDate()));
        UtilsTheme.changeIcon(context,
                "volume", 3, R.drawable.ic_volume, ivRec,
                Color.parseColor(config.getColorTextDate()), Color.parseColor(config.getColorTextDate()));
    }

    public TextView getTvRec() {
        return tvRec;
    }

    public ImageView getIvWave() {
        return ivWave;
    }

    public LinearLayout getLlRec() {
        return llRec;
    }

    public RecyclerView getRcvPic() {
        return rcvPic;
    }

    public TextView getTvDate() {
        return tvDate;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public TextView getTvContent() {
        return tvContent;
    }

    public ImageView getIvMore() {
        return ivMore;
    }

    public ImageView getIvEmoji() {
        return ivEmoji;
    }
}