package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode;

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

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.ViewToolbar;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

public class ViewFingerPrint extends LinearLayout {

    static int w;
    ViewToolbar viewToolbar;
    RelativeLayout rlSwitch;
    SwitchCompat switchCompat;

    public ViewFingerPrint(Context context) {
        super(context);
        setOrientation(VERTICAL);
        setBackgroundColor(getResources().getColor(R.color.white));
        setClickable(true);
        setFocusable(true);

        w = getResources().getDisplayMetrics().widthPixels;
        init(context);
    }

    @SuppressLint("ResourceType")
    private void init(Context context) {
        viewToolbar = new ViewToolbar(context);
        viewToolbar.getTvTitle().setText(getResources().getString(R.string.passcode));
        viewToolbar.getIvVip().setVisibility(GONE);
        viewToolbar.getTvSave().setVisibility(GONE);
        LayoutParams paramsToolbar = new LayoutParams(-1, -2);
        paramsToolbar.setMargins(0, (int) (11.11f * w / 100), 0, 0);
        addView(viewToolbar, paramsToolbar);

        rlSwitch = new RelativeLayout(context);
        LayoutParams paramsRlSwitch = new LayoutParams(-1, -2);
        paramsRlSwitch.setMargins((int) (5.556f * w / 100), (int) (5.556f * w / 100), (int) (5.556f * w / 100), 0);

        switchCompat = new SwitchCompat(context);
        switchCompat.setId(1346);
        switchCompat.setThumbResource(R.drawable.custom_switch_thumb);
        switchCompat.setTrackResource(R.drawable.custom_switch_track);
        RelativeLayout.LayoutParams paramsSwitch = new RelativeLayout.LayoutParams((int) (16f * w / 100), (int) (6.667f * w / 100));
        paramsSwitch.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsSwitch.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        rlSwitch.addView(switchCompat, paramsSwitch);

        TextView tv = new TextView(context);
        tv.setText(getResources().getString(R.string.use_fingerprint_according_to_system_security));
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 3.33f * w / 100);
        tv.setTypeface(Utils.getTypeFace("poppins", "poppins_regular.ttf", context));
        RelativeLayout.LayoutParams paramsText = new RelativeLayout.LayoutParams(-2, -2);
        paramsText.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsText.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        paramsText.addRule(RelativeLayout.LEFT_OF, switchCompat.getId());
        rlSwitch.addView(tv, paramsText);

        addView(rlSwitch, paramsRlSwitch);
    }

    public void createThemeApp(Context context, String nameTheme){
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

        switchCompat.setThumbTintList(Utils.createColorState(Color.parseColor(config.getColorSwitch()), Color.parseColor(config.getColorSwitch())));

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_checked},
                Utils.createBackground(new int[]{Color.parseColor(config.getColorBackgroundSwitch())}, (int) (3.5f * w / 100), -1, -1));
        stateListDrawable.addState(new int[]{-android.R.attr.state_checked},
                Utils.createBackground(new int[]{Color.parseColor(config.getColorUnSelect())}, (int) (3.5f * w / 100), -1, -1));
        stateListDrawable.addState(new int[]{}, Utils.createBackground(new int[]{Color.parseColor(config.getColorUnSelect())}, (int) (3.5f * w / 100), -1, -1));
        switchCompat.setTrackDrawable(stateListDrawable);

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
}
