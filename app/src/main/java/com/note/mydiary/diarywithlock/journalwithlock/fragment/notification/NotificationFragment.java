package com.note.mydiary.diarywithlock.journalwithlock.fragment.notification;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICheckTouch;
import com.note.mydiary.diarywithlock.journalwithlock.model.NotifyModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsTheme;

public class NotificationFragment extends Fragment {

    private ImageView ivBack;
    private EditText etTitle;
    private SwitchCompat switchCompat;
    private TimePicker timePicker;

    private final ICheckTouch isRun;

    private NotifyModel notifyModel;
    private String title, time;
    private boolean isSuccess;

    public NotificationFragment(ICheckTouch isRun) {
        this.isRun = isRun;
    }

    public static NotificationFragment newInstance(ICheckTouch isRun) {
        return new NotificationFragment(isRun);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notifyModel = DataLocalManager.getNotify(Constant.KEY_NOTIFY);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.notification_fragment, container, false);
        initView(v);
        init();
        return v;
    }

    private void initView(View v) {
        RelativeLayout rlBg = v.findViewById(R.id.background);
        TextView tvTitle = v.findViewById(R.id.tvTitle);
        TextView tvSwitch = v.findViewById(R.id.tvSwitch);
        ivBack = v.findViewById(R.id.ivBack);
        switchCompat = v.findViewById(R.id.switchCompat);
        timePicker = v.findViewById(R.id.timePicker);
        etTitle = v.findViewById(R.id.etTitle);

        timePicker.setIs24HourView(true);

        time = "";
        if (notifyModel != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timePicker.setHour(Integer.parseInt(notifyModel.getTime().substring(0, 2)));
                timePicker.setMinute(Integer.parseInt(notifyModel.getTime().substring(3, 5)));
            }
            time = notifyModel.getTime();
            etTitle.setText(notifyModel.getTitle());
            switchCompat.setChecked(notifyModel.isRun());
        }

        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
            int w = getResources().getDisplayMetrics().widthPixels;
            String jsonConfig = Utils.readFromFile(requireContext(), "theme/theme_app/theme_app" + DataLocalManager.getOption(Constant.THEME_APP) + "/config.txt");

            ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

            if (config == null) return;

            if (config.isBackgroundColor())
                rlBg.setBackgroundColor(Color.parseColor(config.getColorBackground()));
            else if (config.isBackgroundGradient())
                rlBg.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorBackground()),
                        Color.parseColor(config.getColorBackgroundGradient())}, -1, -1, -1));
            else {
                Bitmap bmBg = BitmapFactory.decodeFile(Utils.getStore(requireContext()) + "/theme/theme_app/theme_app" + DataLocalManager.getOption(Constant.THEME_APP) + "/background.png");
                if (bmBg != null)
                    rlBg.setBackground(new BitmapDrawable(getResources(), bmBg));
            }

            switchCompat.setThumbTintList(Utils.createColorState(Color.parseColor(config.getColorSwitch()), Color.parseColor(config.getColorSwitch())));

            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_checked},
                    Utils.createBackground(new int[]{Color.parseColor(config.getColorBackgroundSwitch())}, (int) (3.5f * w / 100), -1, -1));
            stateListDrawable.addState(new int[]{-android.R.attr.state_checked},
                    Utils.createBackground(new int[]{Color.parseColor(config.getColorUnSelect())}, (int) (3.5f * w / 100), -1, -1));
            stateListDrawable.addState(new int[]{}, Utils.createBackground(new int[]{Color.parseColor(config.getColorUnSelect())},
                    (int) (3.5f * w / 100), -1, -1));
            switchCompat.setTrackDrawable(stateListDrawable);

            UtilsTheme.changeIcon(requireContext(),
                    "back", 1, R.drawable.ic_back, ivBack,
                    Color.parseColor(config.getColorIcon()), Color.parseColor(config.getColorIcon()));

            tvTitle.setTextColor(Color.parseColor(config.getColorIcon()));
            tvSwitch.setTextColor(Color.parseColor(config.getColorIcon()));

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        ivBack.setOnClickListener(v -> getParentFragmentManager().popBackStack(this.getClass().getSimpleName(), 1));
        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            time = checkDate(hourOfDay, minute);
            if (switchCompat.isChecked()) {
                DataLocalManager.setNotify(Constant.KEY_NOTIFY, new NotifyModel(title, time, true));
                isRun.checkTouch(true);
            }
        });

        switchCompat.setOnTouchListener((buttonView, isChecked) -> {
            if (!etTitle.getText().toString().equals("")) title = etTitle.getText().toString();
            else title = getString(R.string.write_a_diary_for_today);

            if (time.equals("")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    time = checkDate(timePicker.getHour(), timePicker.getMinute());
                else
                    time = checkDate(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
            }

            return false;
        });

        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!etTitle.getText().toString().equals("")) title = etTitle.getText().toString();
            else title = getString(R.string.write_a_diary_for_today);

            if (time.equals("")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    time = checkDate(timePicker.getHour(), timePicker.getMinute());
                else
                    time = checkDate(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
            }

            if (isChecked) {
                DataLocalManager.setNotify(Constant.KEY_NOTIFY, new NotifyModel(title, time, true));
                isRun.checkTouch(true);
            } else
                DataLocalManager.setNotify(Constant.KEY_NOTIFY, new NotifyModel(title, time, false));
        });
    }

    private void stopNotify() {
        if (time.equals("")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                time = checkDate(timePicker.getHour(), timePicker.getMinute());
            else
                time = checkDate(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
        }

        notifyModel.setTime(time);
        notifyModel.setTitle(title);
        notifyModel.setRun(false);

        DataLocalManager.setNotify(Constant.KEY_NOTIFY, notifyModel);
    }

    private String checkDate(int hour, int minute) {
        String h, m;
        if (hour < 10) h = "0" + hour;
        else h = String.valueOf(hour);

        if (minute < 10) m = "0" + minute;
        else m = String.valueOf(minute);

        return h + ":" + m;
    }
}