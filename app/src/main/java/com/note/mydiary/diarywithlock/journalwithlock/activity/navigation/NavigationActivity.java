package com.note.mydiary.diarywithlock.journalwithlock.activity.navigation;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.HelpWidgetActivity;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.ViewNavigation;
import com.note.mydiary.diarywithlock.journalwithlock.activity.base.BaseActivity;
import com.note.mydiary.diarywithlock.journalwithlock.broadcast.AlarmBroadcastNotification;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.cropcover.CropCoverFragment;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.notification.NotificationFragment;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.passcode.LockScreenFragment;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.passcode.PasscodeFragment;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.pickpicture.PickPictureFragment;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.theme.ThemeAppFragment;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.theme.ThemeLockFragment;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.vip.VipOneFragment;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.widget.PickDiaryWidgetFragment;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.widget.WidgetFragment;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.DiaryModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.picture.PicModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsTheme;
import com.note.mydiary.diarywithlock.journalwithlock.widget.FirstWidget;
import com.note.mydiary.diarywithlock.journalwithlock.widget.SecondWidget;
import com.note.mydiary.diarywithlock.journalwithlock.widget.ThirdWidget;

import java.util.Calendar;
import java.util.List;

public class NavigationActivity extends BaseActivity {

    private ViewNavigation viewNavigation;
    private PickDiaryWidgetFragment pickDiaryWidgetFragment;
    private LockScreenFragment lockScreenFragment;

    private ConfigAppThemeModel config;
    private long timeCheck;
    private boolean isStartRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewNavigation = new ViewNavigation(this);
        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
            viewNavigation.getViewToolbar().createTheme(this, "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
            viewNavigation.getViewMenu().createTheme(this, "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));

            String jsonConfig = Utils.readFromFile(this, "theme/theme_app/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP) + "/config.txt");

            config = DataLocalManager.getConfigApp(jsonConfig);

            if (config != null)
                if (config.isBackgroundColor())
                    viewNavigation.setBackgroundColor(Color.parseColor(config.getColorBackground()));
                else if (config.isBackgroundGradient())
                    viewNavigation.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorBackground()),
                            Color.parseColor(config.getColorBackgroundGradient())}, -1, -1, -1));
                else {
                    Bitmap bmBg = BitmapFactory.decodeFile(Utils.getStore(this) + "/theme/theme_app/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP) + "/background.png");
                    if (bmBg != null)
                        viewNavigation.setBackground(new BitmapDrawable(getResources(), bmBg));
                }
        }

        setContentView(viewNavigation);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                int count = 0;
                List<Fragment> lstFragment = getSupportFragmentManager().getFragments();

                if (lstFragment.size() == 0 || (lstFragment.size() == 1 && (lstFragment.get(0).getClass().getSimpleName().equals("SupportRequestManagerFragment")))) {
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else if (lstFragment.size() == 2
                        && (lstFragment.get(0).getClass().getSimpleName().equals("SupportRequestManagerFragment")
                        || lstFragment.get(1).getClass().getSimpleName().equals("SupportRequestManagerFragment"))) {
                    for (Fragment fm : lstFragment) {
                        if (!fm.getClass().getSimpleName().equals("SupportRequestManagerFragment") && fm.getClass().getSimpleName().contains("ItemThemeApp"))
                            count++;
                    }

                    if (count != 0 && count == lstFragment.size() - 1) {
                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else getSupportFragmentManager().popBackStack();
                } else {
                    for (Fragment fm : lstFragment) {
                        if (!fm.getClass().getSimpleName().equals("SupportRequestManagerFragment") && fm.getClass().getSimpleName().contains("ItemThemeApp"))
                            count++;
                    }

                    if (count != 0 && count == lstFragment.size() - 1) {
                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else getSupportFragmentManager().popBackStack();
                }
            }
        });

        evenClick();
    }

    private void evenClick() {
        viewNavigation.getViewToolbar().getIvBack().setOnClickListener(v -> onBackPressed(false, true));
        viewNavigation.getViewToolbar().getIvVip().setOnClickListener(v -> clickVip());
        viewNavigation.getViewMenu().getViewChangeTheme().setOnClickListener(v -> clickChangeTheme());
        viewNavigation.getViewMenu().getViewPasscode().setOnClickListener(v -> clickSetPasscode());
        viewNavigation.getViewMenu().getChangePasscode().setOnClickListener(v -> clickChangePasscode());
        viewNavigation.getViewMenu().getViewTheme().setOnClickListener(v -> clickTheme());
        viewNavigation.getViewMenu().getViewNotification().setOnClickListener(v -> clickNotification());
        viewNavigation.getViewMenu().getViewWidget().setOnClickListener(v -> clickWidget());
        viewNavigation.getViewMenu().getViewCover().setOnClickListener(v -> clickCover());
        viewNavigation.getViewMenu().getViewRate().setOnClickListener(v -> clickRate());
        viewNavigation.getViewMenu().getViewDownload().setOnClickListener(v -> clickDownload());
        viewNavigation.getViewMenu().getViewInsta().setOnClickListener(v -> clickInstar());
        viewNavigation.getViewMenu().getViewFacebook().setOnClickListener(v -> clickFacebook());
        viewNavigation.getViewMenu().getViewFeedback().setOnClickListener(v -> clickFeedback());
        viewNavigation.getViewMenu().getViewPP().setOnClickListener(v -> clickPP());
    }

    public void clickPP() {
        Utils.privacyApp(this);
    }

    public void clickFeedback() {
        Utils.sendFeedback(this);
    }

    public void clickFacebook() {
        Utils.openFacebook(this);
    }

    public void clickInstar() {
        Utils.openInstagram(this);
    }

    public void clickDownload() {
        Utils.moreApps(this);
    }

    public void clickRate() {
        Utils.rateApp(this);
    }

    public void clickCover() {
        PickPictureFragment pictureFragment = PickPictureFragment.newInstance(true, (o, pos) -> cropCover(((PicModel) o).getUri()));
        replaceFragment(getSupportFragmentManager(), pictureFragment, true, true, true);
    }

    private void cropCover(String uri) {
        CropCoverFragment cropCoverFragment = CropCoverFragment.newInstance(uri);
        replaceFragment(getSupportFragmentManager(), cropCoverFragment, true, true, true);
    }

    public void clickWidget() {
        WidgetFragment widgetFragment = WidgetFragment.newInstance((o, pos) -> {
            if (pos == 1 || pos == 3) {
                showToast(getString(R.string.select_the_diary_to_display_on_the_widget), Gravity.CENTER);
                pickDiaryWidgetFragment = PickDiaryWidgetFragment.newInstance((ob, p) -> {
                    DiaryModel diaryModel = (DiaryModel) ob;
                    DataLocalManager.setInt(diaryModel.getId(), Constant.ID_DIARY_WIDGET);
                    addWidget(pos);
                });
                replaceFragment(getSupportFragmentManager(), pickDiaryWidgetFragment, true, true, true);
            } else addWidget(pos);
        });
        replaceFragment(getSupportFragmentManager(), widgetFragment, true, true, true);
    }

    public void clickNotification() {
        NotificationFragment notificationFragment = NotificationFragment.newInstance(isRun -> sendBroadcast());
        replaceFragment(getSupportFragmentManager(), notificationFragment, true, true, true);
    }

    public void clickTheme() {
        ThemeLockFragment themeLockFragment = ThemeLockFragment.newInstance();
        replaceFragment(getSupportFragmentManager(), themeLockFragment, true, true, true);
    }

    private void clickChangePasscode() {
        lockScreenFragment = LockScreenFragment.newInstance((o, pos) -> {
        }, DataLocalManager.getInt(Constant.TYPE_LOCK), true, isExitCheck -> {
            lockScreenFragment.popBackStack();
        });
        Utils.replaceFragment(getSupportFragmentManager(), lockScreenFragment, false, true, true);
    }

    public void clickSetPasscode() {
        PasscodeFragment passcodeFragment = PasscodeFragment.newInstance("", (o, p) -> {
        }, isTouch -> {
            viewNavigation.getViewMenu().getChangePasscode().setVisibility(DataLocalManager.getCheck(Constant.IS_LOCK) ? View.VISIBLE : View.GONE);
            if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")
                    && viewNavigation.getViewMenu().getChangePasscode().getVisibility() == View.VISIBLE) {
                if (config == null) return;
                UtilsTheme.changeIcon(this,
                        "passcode", 2, R.drawable.ic_passcode, viewNavigation.getViewMenu().getChangePasscode().getIvOption(),
                        Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorIconInColor()));
            }
        }, false);
        replaceFragment(getSupportFragmentManager(), passcodeFragment, true, true, true);
    }

    private void clickChangeTheme() {
        ThemeAppFragment themeAppFragment = ThemeAppFragment.newInstance(isReset -> {
            DataLocalManager.setCheck("reset", true);
            finish();
        });
        replaceFragment(getSupportFragmentManager(), themeAppFragment, true, true, true);
    }

    private void clickVip() {
        VipOneFragment vipOneFragment = VipOneFragment.newInstance();
        replaceFragment(getSupportFragmentManager(), vipOneFragment, true, true, true);
    }

    private void addWidget(int pos) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ComponentName myProvider;
            AppWidgetManager appWidgetManager = getSystemService(AppWidgetManager.class);
            switch (pos) {
                case 1:
                    myProvider = new ComponentName(this, FirstWidget.class);
                    break;
                case 2:
                    myProvider = new ComponentName(this, SecondWidget.class);
                    break;
                case 3:
                    myProvider = new ComponentName(this, ThirdWidget.class);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + pos);
            }
            if (appWidgetManager.isRequestPinAppWidgetSupported()) {
                isStartRequest = true;
                Intent intent = new Intent();
                intent.setAction("add");
                @SuppressLint("UnspecifiedImmutableFlag")
                PendingIntent successCallback = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
                appWidgetManager.requestPinAppWidget(myProvider, null, successCallback);
            } else goHelp();
        } else goHelp();
    }

    private void sendBroadcast() {
        BroadcastReceiver alarmBroadcast = new AlarmBroadcastNotification();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.EXTRA_NO_CONNECTIVITY);
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        this.registerReceiver(alarmBroadcast, intentFilter);

        Calendar calendar = Calendar.getInstance(Constant.LOCALE_DEFAULT);
        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S)
            pendingIntent = PendingIntent.getBroadcast(
                    this,
                    0,
                    new Intent(this, AlarmBroadcastNotification.class),
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        else
            pendingIntent = PendingIntent.getBroadcast(
                    this,
                    0,
                    new Intent(this, AlarmBroadcastNotification.class),
                    PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60000, pendingIntent);
            createNotification();
        }
    }

    private void createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description = getString(R.string.channel_description);
            CharSequence name = getString(R.string.channel_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Constant.ID_CHANNEL, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        timeCheck = System.currentTimeMillis();
    }

    @Override
    public void onResume() {
        super.onResume();
        long check = System.currentTimeMillis() - timeCheck;
        if (check <= 190 && isStartRequest) goHelp();

        viewNavigation.getViewMenu().getChangePasscode().setVisibility(DataLocalManager.getCheck(Constant.IS_LOCK) ? View.VISIBLE : View.GONE);
        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")
                && viewNavigation.getViewMenu().getChangePasscode().getVisibility() == View.VISIBLE) {
            if (config == null) return;
            UtilsTheme.changeIcon(this,
                    "passcode", 2, R.drawable.ic_passcode, viewNavigation.getViewMenu().getChangePasscode().getIvOption(),
                    Color.parseColor(config.getColorMain()), Color.parseColor(config.getColorIconInColor()));
        }
    }

    private void goHelp() {
        isStartRequest = false;
        setIntent(HelpWidgetActivity.class.getName(), false);
    }
}
