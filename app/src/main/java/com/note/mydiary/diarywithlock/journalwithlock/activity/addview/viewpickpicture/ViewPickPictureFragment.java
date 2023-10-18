package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewpickpicture;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.ViewToolbar;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsTheme;

public class ViewPickPictureFragment extends LinearLayout {

    int w;
    ViewToolbar viewToolbar;
    RecyclerView rcvBucket, rcvPicture;
    TextView tv, tvDes;
    RelativeLayout rlSwitch;
    SwitchCompat switchCompat;

    public ViewPickPictureFragment(Context context) {
        super(context);
        w = getResources().getDisplayMetrics().widthPixels;
        setOrientation(VERTICAL);
        setClickable(true);
        setFocusable(true);
        setBackgroundColor(getResources().getColor(R.color.white));

        init(context);
    }

    @SuppressLint("ResourceType")
    private void init(Context context) {
        viewToolbar = new ViewToolbar(context);
        viewToolbar.getTvSave().setVisibility(GONE);
        viewToolbar.getIvVip().setVisibility(GONE);
        viewToolbar.getIvVip().setImageResource(R.drawable.ic_tick);
        viewToolbar.getTvTitle().setText(getResources().getString(R.string.custom_cover));
        LayoutParams paramsToolbar = new LayoutParams(-1, -2);
        paramsToolbar.setMargins(0, (int) (w * 11.125f / 100), 0, (int) (4.167f * w / 100));
        addView(viewToolbar, paramsToolbar);

        rlSwitch = new RelativeLayout(context);
        rlSwitch.setVisibility(GONE);
        LayoutParams paramsRlSwitch = new LayoutParams(-1, -2);
        paramsRlSwitch.setMargins((int) (5.556f * w / 100), 0, (int) (5.556f * w / 100), 0);

        switchCompat = new SwitchCompat(context);
        switchCompat.setId(1347);
        switchCompat.setThumbResource(R.drawable.custom_switch_thumb);
        switchCompat.setTrackResource(R.drawable.custom_switch_track);
        RelativeLayout.LayoutParams paramsSwitch = new RelativeLayout.LayoutParams((int) (16f * w / 100), (int) (6.667f * w / 100));
        paramsSwitch.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsSwitch.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        rlSwitch.addView(switchCompat, paramsSwitch);

        tv = new TextView(context);
        tv.setId(1326);
        tv.setText(getResources().getString(R.string.use_cover_photo));
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 4.44f * w / 100);
        tv.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -2);
        params.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.LEFT_OF, switchCompat.getId());
        rlSwitch.addView(tv, params);

        tvDes = new TextView(context);
        tvDes.setText(getResources().getString(R.string.allows_using_images_to_create_cover_photos));
        tvDes.setTextColor(getResources().getColor(R.color.gray_2));
        tvDes.setTextSize(TypedValue.COMPLEX_UNIT_PX, 2.889f * w / 100);
        tvDes.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        RelativeLayout.LayoutParams paramsDes = new RelativeLayout.LayoutParams(-2, -2);
        paramsDes.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        paramsDes.addRule(RelativeLayout.LEFT_OF, switchCompat.getId());
        paramsDes.addRule(RelativeLayout.BELOW, tv.getId());
        rlSwitch.addView(tvDes, paramsDes);

        addView(rlSwitch, paramsRlSwitch);

        LayoutParams paramsRcv = new LayoutParams(-1, -1);
        paramsRcv.setMargins((int) (5.56f * w / 100), (int) (5.56f * w / 100), (int) (5.56f * w / 100), (int) (4.167f * w / 100));

        rcvBucket = new RecyclerView(context);
        rcvBucket.setVisibility(GONE);
        addView(rcvBucket, paramsRcv);

        rcvPicture = new RecyclerView(context);
        rcvPicture.setVisibility(GONE);
        addView(rcvPicture, paramsRcv);
    }

    public void createThemeApp(Context context, String nameTheme) {
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
            if (bmBg != null) setBackground(new BitmapDrawable(getResources(), bmBg));
        }

        tv.setTextColor(Color.parseColor(config.getColorIcon()));
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_checked},
                Utils.createBackground(new int[]{Color.parseColor(config.getColorBackgroundSwitch())}, (int) (3.5f * w / 100), -1, -1));
        stateListDrawable.addState(new int[]{-android.R.attr.state_checked},
                Utils.createBackground(new int[]{Color.parseColor(config.getColorUnSelect())}, (int) (3.5f * w / 100), -1, -1));
        stateListDrawable.addState(new int[]{}, Utils.createBackground(new int[]{Color.parseColor(config.getColorUnSelect())}, (int) (3.5f * w / 100), -1, -1));
        switchCompat.setTrackDrawable(stateListDrawable);

        UtilsTheme.changeIcon(context, "tick", 1, R.drawable.ic_tick, viewToolbar.getIvVip(),
                Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));
    }

    public ViewToolbar getViewToolbar() {
        return viewToolbar;
    }

    public RelativeLayout getRlSwitch() {
        return rlSwitch;
    }

    public SwitchCompat getSwitchCompat() {
        return switchCompat;
    }

    public RecyclerView getRcvBucket() {
        return rcvBucket;
    }

    public RecyclerView getRcvPicture() {
        return rcvPicture;
    }
}
