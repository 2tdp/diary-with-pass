package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewadddiary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
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
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsTheme;

public class ViewItemContent extends LinearLayout {

    static int w;
    EditText et;
    RelativeLayout rlRec;
    TextView tvRec;
    ImageView ivWave, ivRec, ivRemove;
    RecyclerView rcvPic;

    public ViewItemContent(Context context) {
        super(context);
        setOrientation(VERTICAL);
        setBackgroundColor(getResources().getColor(R.color.trans));
        setClickable(true);
        setFocusable(true);
        w = getResources().getDisplayMetrics().widthPixels;
        init(context);
    }

    @SuppressLint("ResourceType")
    private void init(Context context) {

        rlRec = new RelativeLayout(context);
        rlRec.setVisibility(GONE);
        rlRec.setBackgroundResource(R.drawable.border_stroke);
        rlRec.setGravity(Gravity.CENTER_VERTICAL);

        ivRec = new ImageView(context);
        ivRec.setId(1221);
        ivRec.setImageResource(R.drawable.ic_volume);
        RelativeLayout.LayoutParams paramsIvRec = new RelativeLayout.LayoutParams((int) (3.89f * w / 100), (int) (3.89f * w / 100));
        paramsIvRec.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsIvRec.setMargins((int) (1.944f * w / 100), 0, 0, 0);
        rlRec.addView(ivRec, paramsIvRec);

        tvRec = new TextView(context);
        tvRec.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvRec.setMarqueeRepeatLimit(-1);
        tvRec.setHorizontallyScrolling(true);
        tvRec.setSingleLine(true);
        tvRec.setSelected(true);
        tvRec.setTextColor(getResources().getColor(R.color.orange));
        tvRec.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        tvRec.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
        RelativeLayout.LayoutParams paramsTvRec = new RelativeLayout.LayoutParams(-1, (int) (5.556f * w / 100));
        paramsTvRec.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvRec.addRule(RelativeLayout.RIGHT_OF, ivRec.getId());
        paramsTvRec.setMargins((int) (1.389f * w / 100), 0, 0, 0);
        rlRec.addView(tvRec, paramsTvRec);

        ivWave = new ImageView(context);
        ivWave.setVisibility(GONE);
        ivWave.setImageResource(R.drawable.im_wave_audio);
        rlRec.addView(ivWave, paramsTvRec);

        ivRemove = new ImageView(context);
        ivRemove.setImageResource(R.drawable.ic_remove);
        RelativeLayout.LayoutParams paramsImageRemove = new RelativeLayout.LayoutParams((int) (3.889f * w / 100), (int) (3.889f * w / 100));
        paramsImageRemove.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
//        paramsImageRemove.setMargins(0, (int) (1.11f * w / 100), (int) (1.11f * w / 100), 0);
        rlRec.addView(ivRemove, paramsImageRemove);

        addView(rlRec, new LayoutParams((int) (30.556f * w / 100), (int) (7.22f * w / 100)));

        rcvPic = new RecyclerView(context);
        rcvPic.setVisibility(GONE);
        rcvPic.setPadding(0, w / 100, 0, w / 100);
        addView(rcvPic, new LayoutParams(-1, -1));

        et = new EditText(context);
        et.setHint(getResources().getString(R.string.hint_add_2));
        et.setBackgroundColor(getResources().getColor(R.color.trans));
        et.setTextColor(getResources().getColor(R.color.black));
        et.setHintTextColor(getResources().getColor(R.color.gray_2));
        et.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.89f * w / 100);
        et.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        addView(et, new LayoutParams(-1, -2));
    }

    public void createTheme(Context context, String nameTheme) {
        if (nameTheme.equals("default")) return;

        String jsonConfig = Utils.readFromFile(context, "theme/theme_app/" + nameTheme + "/config.txt");

        ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

        if (config == null) return;

        et.setTextColor(Color.parseColor(config.getColorIcon()));

        rlRec.setBackground(Utils.createBackground(new int[]{-1}, (int) (2.5f * w / 100), 2, Color.parseColor(config.getColorMain())));
        tvRec.setTextColor(Color.parseColor(config.getColorTextDate()));
        UtilsTheme.changeIcon(context,
                "wave", 20, R.drawable.im_wave_audio, ivWave,
                Color.parseColor(config.getColorTextDate()), Color.parseColor(config.getColorTextDate()));
        UtilsTheme.changeIcon(context,
                "volume", 3, R.drawable.ic_volume, ivRec,
                Color.parseColor(config.getColorTextDate()), Color.parseColor(config.getColorTextDate()));
    }

    public EditText getEt() {
        return et;
    }

    public RelativeLayout getRlRec() {
        return rlRec;
    }

    public ImageView getIvRemove() {
        return ivRemove;
    }

    public TextView getTvRec() {
        return tvRec;
    }

    public ImageView getIvWave() {
        return ivWave;
    }

    public RecyclerView getRcvPic() {
        return rcvPic;
    }
}
