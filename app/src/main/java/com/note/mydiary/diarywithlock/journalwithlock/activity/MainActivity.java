package com.note.mydiary.diarywithlock.journalwithlock.activity;

import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.biometric.BiometricManager;
import androidx.fragment.app.Fragment;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.adddiary.AddDiary;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewdialog.ViewDialogSort;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewdialog.ViewDialogWidget;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewhome.ViewHome;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.viewfingerprint.callback.FingerprintSecureCallback;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.viewfingerprint.utils.FingerprintToken;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.viewfingerprint.view.Fingerprint;
import com.note.mydiary.diarywithlock.journalwithlock.activity.base.BaseActivity;
import com.note.mydiary.diarywithlock.journalwithlock.activity.navigation.NavigationActivity;
import com.note.mydiary.diarywithlock.journalwithlock.data.DataPic;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.home.HomeAllDiaryFragment;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.home.HomeDiaryInDayFragment;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.passcode.CheckPasscodeFragment;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.passcode.LockScreenFragment;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.previewdiary.PreviewDiaryFragment;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.search.SearchFragment;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.vip.VipOneFragment;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.widget.PickDiaryWidgetFragment;
import com.note.mydiary.diarywithlock.journalwithlock.model.WidgetModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.DiaryModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.widget.FirstWidget;
import com.note.mydiary.diarywithlock.journalwithlock.widget.ThirdWidget;
import com.note.remiads.ads.FullManager;
import com.note.remiads.itf.ShowAdsListen;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Sort;

public class MainActivity extends BaseActivity {

    private final int positionHome = 0, positionCalendar = 1;

    private ViewHome viewHome;

    private HomeAllDiaryFragment homeAllDiaryFragment;
    private HomeDiaryInDayFragment homeDiaryInDayFragment;

    private boolean isLock, widgetSecond, isAll;
    private int w;
    private Sort sort;
    private Date dateSelected;

    private boolean isShowAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewHome = new ViewHome(this);
        viewHome.setBackgroundResource(R.color.white);

        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
            viewHome.createTheme(this, "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                int count = 0;
                Fragment fragment = null;
                List<Fragment> lstFragment = getSupportFragmentManager().getFragments();
                for (Fragment fr : lstFragment) {
                    if (lstFragment.size() == 1) count++;
                    else if ((lstFragment.size() == 2)
                            && (fr.getClass().getSimpleName().equals("HomeAllDiaryFragment")
                            || fr.getClass().getSimpleName().equals("HomeDiaryInDayFragment")
                            || fr.getClass().getSimpleName().equals("SupportRequestManagerFragment")))
                        count++;
                    else if (lstFragment.size() == 3 && (fr.getClass().getSimpleName().equals("HomeAllDiaryFragment")
                            || fr.getClass().getSimpleName().equals("HomeDiaryInDayFragment")
                            || fr.getClass().getSimpleName().equals("SupportRequestManagerFragment")))
                        count++;
                    else {
                        count = 0;
                        fragment = fr;
                    }
                }
                if (count == 0) {
                    Utils.hideKeyboard(MainActivity.this, viewHome);
                    if (fragment != null)
                        getSupportFragmentManager().popBackStack(fragment.getClass().getSimpleName(), 1);

                    if (homeAllDiaryFragment != null && homeAllDiaryFragment.isVisible()
                            || homeDiaryInDayFragment != null && homeDiaryInDayFragment.isVisible())
                        resetMain();
                } else onBackPressed(true, false);
            }
        });

        setContentView(viewHome);
        w = getResources().getDisplayMetrics().widthPixels;

        if (DataLocalManager.getCheck("first")) {
            Utils.makeFolder(this, Constant.FOLDER_COVER_IMAGE);
            Utils.makeFolder(this, Constant.SIGNATURE_FOLDER);
            Utils.makeFolder(this, Constant.THEME_PASSCODE_FOLDER + "theme_pattern");
            Utils.makeFolder(this, Constant.THEME_PASSCODE_FOLDER + "theme_pincode");
            Utils.makeFolder(this, Constant.THEME_APP_FOLDER);
            if (DataLocalManager.getOption(Constant.THEME_LOCK).equals(""))
                DataLocalManager.setOption("default", Constant.THEME_LOCK);
        }

        init();
        evenClick();
    }

    private void init() {
        new Thread(() -> DataPic.getBucketPictureList(this)).start();

        sort = Sort.DESCENDING;
        String idWidget = getIntent().getStringExtra(Constant.CALLBACK_WIDGET);
        widgetSecond = getIntent().getBooleanExtra(Constant.WIDGET_2, false);

        if (DataLocalManager.getCheck(Constant.IS_LOCK)) {
            if (idWidget != null) checkPassCode(true, idWidget);
            else checkPassCode(false, "");
            isLock = true;
        } else {
            if (widgetSecond) createViewCalenderHome();
            else if (idWidget != null) checkPassCode(true, idWidget);
            else createViewHome(true);
        }
    }

    private void evenClick() {
        //openNavigation
        viewHome.getViewToolbarHome().getIvNavigation().setOnClickListener(v ->
                showAds(() -> openNavigation(NavigationActivity.class.getName(), false))
        );
        //switchTabHome
        viewHome.getViewBottomHome().getViewOptionHome().getViewClickHome().setOnClickListener(v -> {
            isAll = true;
            if (homeAllDiaryFragment == null) createViewHome(true);
            else resetMain();
        });
        viewHome.getViewBottomHome().getViewOptionHome().getViewClickCalendar().setOnClickListener(v -> {
            isAll = false;
            if (homeDiaryInDayFragment == null) createViewCalenderHome();
            else resetMain();
        });
        //addDiary
        viewHome.getViewBottomHome().getIvAdd().setOnClickListener(v -> showAds(() -> {
            Intent intent = new Intent(this, AddDiary.class);
            if (dateSelected != null && homeDiaryInDayFragment.isVisible())
                intent.putExtra("selectedDate", dateSelected);
            startActivity(intent);
        }));
        //search
        viewHome.getViewToolbarHome().getIvSearch().setOnClickListener(v -> clickSearch());
        //sort
        viewHome.getViewToolbarHome().getIvSort().setOnClickListener(v -> clickSort());
        //vip
        viewHome.getViewToolbarHome().getIvVip().setOnClickListener(v -> clickVip());
    }

    private void clickVip() {
        VipOneFragment vipOneFragment = VipOneFragment.newInstance();
        replaceFragment(getSupportFragmentManager(), vipOneFragment, true, true, true);
    }

    private void clickSort() {
        int w = getResources().getDisplayMetrics().widthPixels;
        ViewDialogSort viewDialogSort = new ViewDialogSort(this);
        if (sort == Sort.ASCENDING) {
            viewDialogSort.getViewItemLatest().getIvTick().setVisibility(View.GONE);
            viewDialogSort.getViewItemOldest().getIvTick().setVisibility(View.VISIBLE);
        } else if (sort == Sort.DESCENDING) {
            viewDialogSort.getViewItemOldest().getIvTick().setVisibility(View.GONE);
            viewDialogSort.getViewItemLatest().getIvTick().setVisibility(View.VISIBLE);
        }
        PopupWindow popUp = new PopupWindow(viewDialogSort, (int) (45f * w / 100), RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        popUp.showAsDropDown(viewHome.getViewToolbarHome().getIvSort(), (int) (-34f * w / 100), 0);

        viewDialogSort.getViewItemLatest().setOnClickListener(v -> {
            sort = Sort.DESCENDING;
            if (homeAllDiaryFragment != null) homeAllDiaryFragment.getDataDiaryHome(sort);
            popUp.dismiss();
        });
        viewDialogSort.getViewItemOldest().setOnClickListener(v -> {
            sort = Sort.ASCENDING;
            if (homeAllDiaryFragment != null) homeAllDiaryFragment.getDataDiaryHome(sort);
            popUp.dismiss();
        });
    }

    private void clickSearch() {
        SearchFragment searchFragment = SearchFragment.newInstance(isDel -> resetMain(), resetCalendar -> resetMain());
        replaceFragment(getSupportFragmentManager(), searchFragment, true, true, true);
    }

    private void setUpLayoutBottomHome(int position) {
        switch (position) {
            case positionHome:
                if (homeAllDiaryFragment != null)
                    showFragment(getSupportFragmentManager(), homeAllDiaryFragment, R.anim.slide_in_left_small, R.anim.slide_out_left);

                if (homeDiaryInDayFragment != null)
                    hideFragment(getSupportFragmentManager(), homeDiaryInDayFragment, R.anim.slide_in_right, R.anim.slide_out_right);

                Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left_small);
                viewHome.getIvCover().setAnimation(animation);
                viewHome.getIvCover().setVisibility(View.VISIBLE);
                viewHome.getViewToolbarHome().getTvTitle().setVisibility(View.GONE);
                viewHome.getViewToolbarHome().getIvSearch().setVisibility(View.VISIBLE);
                viewHome.getViewToolbarHome().getIvSort().setVisibility(View.VISIBLE);

                viewHome.getViewBottomHome().getViewOptionHome().getViewClickHome().getTv().setText(getResources().getString(R.string.home));

                viewHome.getViewBottomHome().getViewOptionHome().getViewClickHome().getViewLine().setVisibility(View.VISIBLE);

                viewHome.getViewBottomHome().getViewOptionHome().getViewClickCalendar().getTv().setText("");
                viewHome.getViewBottomHome().getViewOptionHome().getViewClickCalendar().getViewLine().setVisibility(View.GONE);
                break;
            case positionCalendar:
                if (homeAllDiaryFragment != null)
                    hideFragment(getSupportFragmentManager(), homeAllDiaryFragment, R.anim.slide_in_left_small, R.anim.slide_out_left);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
                viewHome.getIvCover().setAnimation(animation);
                viewHome.getIvCover().setVisibility(View.GONE);

                if (homeDiaryInDayFragment != null)
                    showFragment(getSupportFragmentManager(), homeDiaryInDayFragment, R.anim.slide_in_right, R.anim.slide_out_right);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
                viewHome.getViewToolbarHome().getTvTitle().setVisibility(View.VISIBLE);
                viewHome.getViewToolbarHome().getIvSearch().setVisibility(View.GONE);
                viewHome.getViewToolbarHome().getIvSort().setVisibility(View.GONE);

                viewHome.getViewBottomHome().getViewOptionHome().getViewClickCalendar().getTv().setText(getResources().getString(R.string.calendar));
                viewHome.getViewBottomHome().getViewOptionHome().getViewClickCalendar().getViewLine().setVisibility(View.VISIBLE);

                viewHome.getViewBottomHome().getViewOptionHome().getViewClickHome().getTv().setText("");
                viewHome.getViewBottomHome().getViewOptionHome().getViewClickHome().getViewLine().setVisibility(View.GONE);
                break;
        }
    }

    private void checkPassCode(boolean isWidget, String idWidget) {
        if (DataLocalManager.getCheck(Constant.IS_LOCK)) {
            if (DataLocalManager.getInt(Constant.TYPE_LOCK) == Constant.POS_FINGER_PRINT) {
                if (BiometricManager.from(this).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) != BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED)
                    initFingerPrint();
                else errorFingerPrint();
            } else {
                CheckPasscodeFragment checkPasscodeFragment = CheckPasscodeFragment.newInstance(true, isTouch -> {
                    isLock = !isTouch;
                    if (!isWidget) {
                        if (widgetSecond) createViewCalenderHome();
                        else createViewHome(true);
                    } else showDialogWidget(idWidget);
                });
                replaceFragment(getSupportFragmentManager(), checkPasscodeFragment, false, true, false);
            }
        } else {
            if (!isWidget) {
                if (widgetSecond) createViewCalenderHome();
                else createViewHome(true);
            } else showDialogWidget(idWidget);
        }
    }

    private void initFingerPrint() {

        View v = LayoutInflater.from(this).inflate(R.layout.dialog_check_fingerprintf, null);

        Fingerprint fingerprint = v.findViewById(R.id.fingerprint);
        TextView tvError = v.findViewById(R.id.tvError);
        TextView tvUnlock = v.findViewById(R.id.tvUnlock);

        tvUnlock.setText(Utils.underLine(getString(R.string.confirm_fingerprint_to_unlock_apps)));
        tvError.setVisibility(View.GONE);

        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
            String jsonConfig = Utils.readFromFile(this, "theme/theme_app/theme_app" + DataLocalManager.getOption(Constant.THEME_APP) + "/config.txt");

            ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

            if (config == null) return;

            tvUnlock.setTextColor(Color.parseColor(config.getColorSwitch()));
        }

        AlertDialog dialog = new AlertDialog.Builder(this, R.style.SheetDialog).create();
        dialog.setView(v);
        dialog.setCancelable(false);
        dialog.show();

        tvUnlock.setOnClickListener(vUnlock -> {
            errorFingerPrint();
            dialog.cancel();
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            fingerprint.callback(new FingerprintSecureCallback() {
                        @Override
                        public void onAuthenticationSucceeded() {
                            dialog.cancel();
                            setUpLayoutBottomHome(positionHome);
                        }

                        @Override
                        public void onAuthenticationFailed() {
                            tvError.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onNewFingerprintEnrolled(FingerprintToken token) {
                        }

                        @Override
                        public void onAuthenticationError(int errorCode, String error) {
                        }
                    }, "KeyName2")
                    .circleScanningColor(android.R.color.black)
                    .fingerprintScanningColor(R.color.orange)
                    .authenticate();
    }

    private void errorFingerPrint() {
        Toast.makeText(this, getString(R.string.fingerPrint_error_none_enrolled), Toast.LENGTH_SHORT).show();
        LockScreenFragment lockScreenFragment = LockScreenFragment.newInstance((o, pos) -> {
        }, Constant.POS_QUESTION_PASS, true, isExitCheck -> {
            isLock = !isExitCheck;
            Utils.clearBackStack(getSupportFragmentManager());
            createViewHome(true);
        });
        replaceFragment(getSupportFragmentManager(), lockScreenFragment, true, true, true);
    }

    private void createViewHome(boolean isAnim) {
        homeAllDiaryFragment = HomeAllDiaryFragment.newInstance(sort, resetCalendar -> resetMain());
        replaceFragment(getSupportFragmentManager(), homeAllDiaryFragment, true, true, isAnim);

        if (isAnim) setUpLayoutBottomHome(positionHome);
    }

    private void createViewCalenderHome() {
        homeDiaryInDayFragment = HomeDiaryInDayFragment.newInstance(resetCalendar -> resetMain(), (o, pos) -> dateSelected = (Date) o);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right)
                .add(android.R.id.content, homeDiaryInDayFragment)
                .addToBackStack(homeDiaryInDayFragment.getClass().getSimpleName())
                .commit();

        setUpLayoutBottomHome(positionCalendar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FullManager.getInstance().onResume(this);
        if (DataLocalManager.getCheck("reset")) {
            DataLocalManager.setCheck("reset", false);
            Intent intent = new Intent(this, SplashActivity.class);
            this.finish();
            startActivity(intent);
        }

        DataLocalManager.setInt(-1, Constant.KEY_ID_DIARY);
        if (!isLock) {
            if (getIntent().getBooleanExtra(Constant.WIDGET_2, false))
                setUpLayoutBottomHome(positionCalendar);

            resetMain();
        }

        if (!DataLocalManager.getOption(Constant.NAME_COVER_IMAGE).equals("") && DataLocalManager.getCheck(Constant.IS_COVER))
            viewHome.getIvCover().setImageBitmap(BitmapFactory.decodeFile(DataLocalManager.getOption(Constant.NAME_COVER_IMAGE)));
        else {
            viewHome.getIvCover().setImageResource(R.drawable.im_home);

            if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
                String jsonConfig = Utils.readFromFile(this, "theme/theme_app/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP) + "/config.txt");

                ConfigAppThemeModel config = DataLocalManager.getConfigApp(jsonConfig);

                if (config != null)
                    viewHome.getIvCover().setImageBitmap(BitmapFactory.decodeFile(
                            Utils.getStore(this) + "/theme/theme_app/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP) + "/background_home.png"));
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        FullManager.getInstance().onPause(this);
    }

    private void resetMain() {
        if (DataLocalManager.getCheck("resetCalendar")) {
            if (homeAllDiaryFragment == null && homeDiaryInDayFragment == null)
                createViewHome(true);
            else if (homeAllDiaryFragment != null && isAll) {
                setUpLayoutBottomHome(positionHome);
                homeAllDiaryFragment.getDataDiaryHome(sort);
            } else {
                if (homeDiaryInDayFragment == null) createViewCalenderHome();
                else {
                    homeDiaryInDayFragment.resetCalendar();

                    if (dateSelected == null)
                        homeDiaryInDayFragment.getDataDiaryCalendar(Calendar.getInstance(Constant.LOCALE_DEFAULT).getTime().getTime());
                    else {
                        homeDiaryInDayFragment.getDataDiaryCalendar(dateSelected.getTime());
                        homeDiaryInDayFragment.setSelectedDay(dateSelected);
                    }
                    setUpLayoutBottomHome(positionCalendar);
                }
            }
            DataLocalManager.setCheck("resetCalendar", false);
        } else {
            if (homeAllDiaryFragment == null && homeDiaryInDayFragment == null)
                createViewHome(true);
            else if (homeAllDiaryFragment != null && homeDiaryInDayFragment != null) {
                if (isAll) {
                    if (homeAllDiaryFragment.isHidden()) setUpLayoutBottomHome(positionHome);
                    homeAllDiaryFragment.getDataDiaryHome(sort);
                } else {
                    homeDiaryInDayFragment.resetCalendar();
                    if (homeDiaryInDayFragment.isHidden()) setUpLayoutBottomHome(positionCalendar);
                    if (dateSelected == null)
                        homeDiaryInDayFragment.getDataDiaryCalendar(Calendar.getInstance(Constant.LOCALE_DEFAULT).getTime().getTime());
                    else {
                        homeDiaryInDayFragment.getDataDiaryCalendar(dateSelected.getTime());
                        homeDiaryInDayFragment.setSelectedDay(dateSelected);
                    }
                }
            } else if (homeAllDiaryFragment != null) {
                setUpLayoutBottomHome(positionHome);
                homeAllDiaryFragment.getDataDiaryHome(sort);
            } else {
                setUpLayoutBottomHome(positionHome);
                homeAllDiaryFragment.getDataDiaryHome(sort);
                if (dateSelected == null)
                    homeDiaryInDayFragment.getDataDiaryCalendar(Calendar.getInstance(Constant.LOCALE_DEFAULT).getTime().getTime());
                else {
                    homeDiaryInDayFragment.getDataDiaryCalendar(dateSelected.getTime());
                    homeDiaryInDayFragment.setSelectedDay(dateSelected);
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (homeAllDiaryFragment != null && homeAllDiaryFragment.isVisible()) isAll = true;
        else if (homeDiaryInDayFragment != null && homeDiaryInDayFragment.isVisible())
            isAll = false;
    }

    private void showDialogWidget(String idWidget) {
        if (DataLocalManager.getInt(idWidget) == -1)
            return;

        ViewDialogWidget viewDialogWidget = new ViewDialogWidget(this);
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.SheetDialog).create();
        dialog.setView(viewDialogWidget);
        dialog.setCancelable(false);
        dialog.show();

        viewDialogWidget.getLayoutParams().width = (int) (84.444f * w / 100);
        viewDialogWidget.getLayoutParams().height = (int) (55.56f * w / 100);

        viewDialogWidget.getViewDiary().setOnClickListener(v -> {
            dialog.cancel();
            PreviewDiaryFragment previewDiaryFragment = PreviewDiaryFragment.newInstance(DataLocalManager.getInt(idWidget),
                    isDelete -> {
                        DataLocalManager.setInt(-1, Constant.ID_DIARY_WIDGET);
                        updateWidget(idWidget, -1);
                        createViewHome(true);
                    },
                    resetCalendar -> {
                        updateWidget(idWidget, DataLocalManager.getInt(idWidget));
                        resetMain();
                    });
            Utils.replaceFragment(getSupportFragmentManager(), previewDiaryFragment, true, true, true);
        });

        viewDialogWidget.getViewCustomWidget().setOnClickListener(v -> {
            dialog.cancel();
            PickDiaryWidgetFragment pickDiaryWidgetFragment = PickDiaryWidgetFragment.newInstance((ob, p) -> {
                DiaryModel diaryModel = (DiaryModel) ob;
                updateWidget(idWidget, diaryModel.getId());
                getSupportFragmentManager().popBackStack(PickDiaryWidgetFragment.class.getSimpleName(), 1);
                resetMain();
            });
            replaceFragment(getSupportFragmentManager(), pickDiaryWidgetFragment, true, true, true);
        });
    }

    private void updateWidget(String widget, int idDiary) {
        ArrayList<WidgetModel> lstWidget = DataLocalManager.getArrWidget(Constant.LIST_ID_WIDGET);
        for (WidgetModel widgetModel : lstWidget) {
            if (widget.contains(widgetModel.getNameWidget())) {
                DataLocalManager.setInt(idDiary, widgetModel.getNameWidget());
                Log.d(Constant.TAG, "updateWidget: " + widgetModel.getNameWidget());
                if (widget.contains("First"))
                    FirstWidget.updateAppWidget(this, AppWidgetManager.getInstance(this), widgetModel.getIdWidget());
                else if (widget.contains("Third"))
                    ThirdWidget.updateAppWidget(this, AppWidgetManager.getInstance(this), widgetModel.getIdWidget());
                showToast(getString(R.string.done), Gravity.CENTER_HORIZONTAL);
            }
        }
    }

    private void showAds(ShowAdsListen showAdsListen) {
        isShowAds = !isShowAds;
        if (isShowAds)
            FullManager.getInstance().showAds(this, showAdsListen);
        else
            showAdsListen.onCloseAds();
    }
}