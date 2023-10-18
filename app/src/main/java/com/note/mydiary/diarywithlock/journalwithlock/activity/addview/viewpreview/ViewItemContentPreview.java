package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewpreview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsTheme;

public class ViewItemContentPreview extends LinearLayout {

    static int w;
    TextView tv;
    LinearLayout llRec;
    TextView tvRec;
    ImageView ivWave, ivRec;
    RecyclerView rcvPic;

    public ViewItemContentPreview(Context context) {
        super(context);
        setOrientation(VERTICAL);
        setBackgroundColor(getResources().getColor(R.color.white));
        setClickable(true);
        setFocusable(true);
        w = getResources().getDisplayMetrics().widthPixels;
        init(context);
    }

    private void init(Context context) {

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
        tvRec.setMarqueeRepeatLimit(-1);
        tvRec.setHorizontallyScrolling(true);
        tvRec.setSingleLine(true);
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

        addView(llRec, new LayoutParams(-2, (int) (8.33f * w / 100)));

        rcvPic = new RecyclerView(context);
        rcvPic.setVisibility(GONE);
        rcvPic.setPadding(0, w / 100, 0, w / 100);
        addView(rcvPic, new LayoutParams(-1, -1));

        tv = new TextView(context);
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.89f * w / 100);
        tv.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        addView(tv, new LayoutParams(-1, -2));
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

        llRec.setBackground(Utils.createBackground(new int[]{-1}, (int) (2.5f * w / 100), 2, Color.parseColor(config.getColorMain())));
        tvRec.setTextColor(Color.parseColor(config.getColorTextDate()));
        tv.setTextColor(Color.parseColor(config.getColorIcon()));
        UtilsTheme.changeIcon(context,
                "wave", 20, R.drawable.im_wave_audio, ivWave,
                Color.parseColor(config.getColorTextDate()), Color.parseColor(config.getColorTextDate()));
        UtilsTheme.changeIcon(context,
                "volume", 3, R.drawable.ic_volume, ivRec,
                Color.parseColor(config.getColorTextDate()), Color.parseColor(config.getColorTextDate()));
    }

    public TextView getTv() {
        return tv;
    }

    public LinearLayout getLlRec() {
        return llRec;
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
