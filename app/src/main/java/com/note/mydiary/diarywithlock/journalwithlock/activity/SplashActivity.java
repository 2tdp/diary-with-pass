package com.note.mydiary.diarywithlock.journalwithlock.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.base.BaseActivity;
import com.note.mydiary.diarywithlock.journalwithlock.activity.onboarding.OnBoardingActivity;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.remiads.SplashController;
import com.note.remiads.ads.FullManager;
import com.note.remiads.utils.RmSave;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends BaseActivity {

    private ImageView imageView;
    private TextView tv;
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private final Handler handler = new Handler();

    private SplashController splashController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseApp.initializeApp(this);
        FirebaseAnalytics.getInstance(this);
        splashController = new SplashController(this, this::onEnd);

        imageView = findViewById(R.id.imageView);
        tv = findViewById(R.id.textView);
        progressBar = findViewById(R.id.progress_bar);

        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) checkThemeApp();

        if (RmSave.isFistOpen(this)) {
            RmSave.setFistOpen(this);
            FullManager.getInstance().loadAds(this);
            onEnd();
            return;
        }

        new Thread(() -> {
            while (progressStatus < 100) {
                progressStatus += 1;
                handler.post(() -> progressBar.setProgress(progressStatus));
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        splashController.loadAds();
    }

    private void checkThemeApp() {
        int w = getResources().getDisplayMetrics().widthPixels;
        String nameTheme = DataLocalManager.getOption(Constant.THEME_APP);
        String jsonConfig = Utils.readFromFile(this, "theme/theme_app/theme_app/" + nameTheme + "/config.txt");

        ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

        if (config == null) return;

        Bitmap bmBg = BitmapFactory.decodeFile(Utils.getStore(this) + "/theme/theme_app/theme_app/" + nameTheme + "/background_vip_2.png");
        if (bmBg == null) imageView.setBackgroundColor(getResources().getColor(R.color.white));
        else imageView.setImageBitmap(bmBg);

        tv.setTextColor(Color.parseColor(config.getColorIcon()));

        progressBar.setBackground(Utils.createBackground(new int[]{Color.parseColor(config.getColorIconInColor())},
                (int) (2.5f * w / 100), 10, Color.parseColor(config.getColorMain())));

        LayerDrawable progressBarDrawable = (LayerDrawable) progressBar.getProgressDrawable();
        Drawable backgroundDrawable = progressBarDrawable.getDrawable(0);
        Drawable progressDrawable = progressBarDrawable.getDrawable(1);

        backgroundDrawable.setColorFilter(Color.parseColor(config.getColorIconInColor()), PorterDuff.Mode.SRC_IN);
        progressDrawable.setColorFilter(Color.parseColor(config.getColorMain()), PorterDuff.Mode.SRC_IN);
    }

    private void onEnd() {
        Intent intent = new Intent(SplashActivity.this, OnBoardingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (splashController != null)
            splashController.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (splashController != null)
            splashController.onPause();
    }
}