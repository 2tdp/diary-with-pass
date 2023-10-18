package com.note.mydiary.diarywithlock.journalwithlock.fragment.passcode;

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureStroke;
import android.gesture.Prediction;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.ViewCheckPassword;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.ViewsetSignature;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.patternview.customView.OnPatternChangeListener;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.patternview.customView.PatternLockerView;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.pinlockview.PinLockListener;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewtheme.ViewThemePattern;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewtheme.ViewThemePinLock;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICheckTouch;
import com.note.mydiary.diarywithlock.journalwithlock.model.theme.ConfigAppThemeModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CheckPasscodeFragment extends Fragment {

    public static String IS_MAIN_CHECK = "type_lock";

    private int typeLock;
    private boolean isShow, isMainCheck;
    private final ICheckTouch isUnLock;

    private ConfigAppThemeModel config;

    ViewThemePinLock viewThemePincode;
    ViewCheckPassword viewPassword;
    ViewThemePattern viewPattern;
    ViewsetSignature viewSignature;

    private LockScreenFragment lockScreenFragment;

    public CheckPasscodeFragment(ICheckTouch isUnLock) {
        this.isUnLock = isUnLock;
    }

    public static CheckPasscodeFragment newInstance(boolean isMainCheck, ICheckTouch isUnLock) {
        Bundle args = new Bundle();
        CheckPasscodeFragment fragment = new CheckPasscodeFragment(isUnLock);
        args.putBoolean(IS_MAIN_CHECK, isMainCheck);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) isMainCheck = getArguments().getBoolean(IS_MAIN_CHECK);

        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!isMainCheck) Utils.clearBackStack(getParentFragmentManager());
            }
        });

        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
            String jsonConfig = Utils.readFromFile(requireContext(), "theme/theme_app/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP) + "/config.txt");

            config = DataLocalManager.getConfigApp(jsonConfig);
        }

        typeLock = DataLocalManager.getInt(Constant.TYPE_LOCK);
        if (typeLock == Constant.POS_PIN_CODE) {
            viewThemePincode = new ViewThemePinLock(requireContext());
            if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default"))
                viewThemePincode.createThemeApp(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
            if (!DataLocalManager.getOption(Constant.THEME_LOCK).equals(""))
                viewThemePincode.createTheme(requireContext(), DataLocalManager.getOption(Constant.THEME_LOCK));
        }
        if (typeLock == Constant.POS_PASSWORD) {
            viewPassword = new ViewCheckPassword(requireContext());
            if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default"))
                viewPassword.createThemeApp(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
        }
        if (typeLock == Constant.POS_PATTERN) {
            viewPattern = new ViewThemePattern(requireContext());
            if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default"))
                viewPattern.createThemeApp(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
            if (!DataLocalManager.getOption(Constant.THEME_LOCK).equals(""))
                viewPattern.createThemeLock(requireContext(), DataLocalManager.getOption(Constant.THEME_LOCK));
        }
        if (typeLock == Constant.POS_SIGNATURE) {
            viewSignature = new ViewsetSignature(requireContext());
            if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default"))
                viewSignature.createThemeApp(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (typeLock == Constant.POS_PIN_CODE) {
            initPinCode();
            return viewThemePincode;
        } else if (typeLock == Constant.POS_PASSWORD) {
            initPassword();
            return viewPassword;
        } else if (typeLock == Constant.POS_PATTERN) {
            initPattern();
            return viewPattern;
        } else if (typeLock == Constant.POS_SIGNATURE) {
            initSignature();
            return viewSignature;
        }

        return null;
    }

    private void initPinCode() {
        viewThemePincode.getTvForgot().setOnClickListener(v -> questionLock());

        viewThemePincode.getPinLockView().setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                checkPincode(pin);
            }

            @Override
            public void onEmpty() {

            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {

            }
        });
    }

    private void checkPincode(String pin) {
        String pinConfirm = DataLocalManager.getOption(Constant.PASS_CODE_PIN_CODE);
        if (pin.equals(pinConfirm)) {
            getParentFragmentManager().popBackStack();
            isUnLock.checkTouch(true);
        } else {
            Utils.effectVibrate(requireContext());
            viewThemePincode.getTvTitle().setText(getString(R.string.wrong_password_please_try_again));
            viewThemePincode.getTvTitle().setTextColor(getResources().getColor(R.color.red));
            viewThemePincode.getPinLockView().resetPinLockView();
        }
    }

    private void initPassword() {
        viewPassword.getTvForgot().setOnClickListener(v -> questionLock());

        String pinConfirm = DataLocalManager.getOption(Constant.PASS_CODE_PASSWORD);

        viewPassword.getEt().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    if (config != null)
                        viewPassword.getTv().setTextColor(Color.parseColor(config.getColorIcon()));
                    else
                        viewPassword.getTv().setTextColor(getResources().getColor(R.color.black));
                    viewPassword.getTv().setText(getString(R.string.enter_your_password));
                } else if (charSequence.toString().equals(pinConfirm)) {
                    getParentFragmentManager().popBackStack();
                    isUnLock.checkTouch(true);
                    Utils.hideKeyboard(requireContext(), viewPassword);
                } else {
                    viewPassword.getTv().setTextColor(getResources().getColor(R.color.red));
                    viewPassword.getTv().setText(getString(R.string.wrong_password_please_try_again));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        viewPassword.getIv().setOnClickListener(v -> {
            if (isShow) {
                isShow = false;
                viewPassword.getIv().setImageResource(R.drawable.ic_hint);
                viewPassword.getEt().setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                isShow = true;
                viewPassword.getIv().setImageResource(R.drawable.ic_hide);
                viewPassword.getEt().setTransformationMethod(null);
            }
        });
    }

    private void initPattern() {
        viewPattern.getTvForgot().setOnClickListener(v -> questionLock());

        String pinConfirm = DataLocalManager.getOption(Constant.PASS_CODE_PATTERN);
        viewPattern.getPatternView().setOnPatternChangedListener(new OnPatternChangeListener() {
            @Override
            public void onStart(PatternLockerView view) {

            }

            @Override
            public void onChange(PatternLockerView view, List<Integer> hitList) {

            }

            @Override
            public void onComplete(PatternLockerView view, List<Integer> hitList) {
                if (Utils.convertListToString(hitList).equals(pinConfirm)) {
                    getParentFragmentManager().popBackStack();
                    isUnLock.checkTouch(true);
                } else {
                    Utils.effectVibrate(requireContext());
                    viewPattern.getTvTitle().setText(getString(R.string.wrong_password_please_try_again));
                    viewPattern.getTvTitle().setTextColor(getResources().getColor(R.color.red));
                    viewPattern.getPatternView().clearHitState();
                }
            }

            @Override
            public void onClear(PatternLockerView view) {
            }
        });
    }

    private void initSignature() {
        viewSignature.getTvForgot().setVisibility(View.VISIBLE);
        viewSignature.getViewToolbar().setVisibility(View.GONE);
        viewSignature.getTvClick().setVisibility(View.GONE);
        viewSignature.getTvTitle().setText(getString(R.string.enter_your_signature));

        viewSignature.getTvForgot().setOnClickListener(v -> questionLock());

        GestureLibrary gestureLibrary = GestureLibraries.fromFile(new File(Utils.getStore(requireContext()), Constant.SIGNATURE_FOLDER));
        ArrayList<GestureStroke> lstGestureStroke = null;
        if (!DataLocalManager.getListGestureStroke(Constant.PASS_CODE_GESTURE_STROKE).isEmpty())
            lstGestureStroke = DataLocalManager.getListGestureStroke(Constant.PASS_CODE_GESTURE_STROKE);

        if (lstGestureStroke != null && !lstGestureStroke.isEmpty()) {
            Gesture gesture = new Gesture();
            gesture.getStrokes().addAll(lstGestureStroke);
            gestureLibrary.addGesture(Constant.SIGNATURE_PIN, gesture);
        }

        viewSignature.getGestureOverlayView().addOnGesturingListener(new GestureOverlayView.OnGesturingListener() {
            @Override
            public void onGesturingStarted(GestureOverlayView gestureOverlayView) {

            }

            @Override
            public void onGesturingEnded(GestureOverlayView gestureOverlayView) {
                Gesture gesture = gestureOverlayView.getGesture();
                if (gesture != null) {

                    ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);

                    for (Prediction prediction : predictions) {
                        String name = prediction.name;
                        double score = prediction.score;
                        Log.d(Constant.TAG, "onGesturingEnded: " + score);
                        if (score > 2.5f && name.equalsIgnoreCase(Constant.SIGNATURE_PIN)) {
                            getParentFragmentManager().popBackStack();
                            isUnLock.checkTouch(true);
                        } else {
                            Utils.effectVibrate(requireContext());
                            viewSignature.getTvTitle().setVisibility(View.INVISIBLE);
                            viewSignature.getTvError().setVisibility(View.VISIBLE);
                            viewSignature.getTvError().setText(getString(R.string.wrong_password_please_try_again));
                            viewSignature.getGestureOverlayView().clear(false);
                        }
                    }
                }
            }
        });
    }

    private void questionLock() {
        lockScreenFragment = LockScreenFragment.newInstance((o, pos) -> {
        }, Constant.POS_QUESTION_PASS, true, isExitCheck -> {
            if (isMainCheck) {
                Utils.clearBackStack(getParentFragmentManager());
                isUnLock.checkTouch(true);
            } else {
                DataLocalManager.setInt(-1, Constant.TYPE_LOCK);
                lockScreenFragment.popBackStack();
                getParentFragmentManager().popBackStack();
            }
        });
        Utils.replaceFragment(getParentFragmentManager(), lockScreenFragment, true, true, true);
    }
}
