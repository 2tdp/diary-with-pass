package com.note.mydiary.diarywithlock.journalwithlock.fragment.passcode;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureStroke;
import android.gesture.Prediction;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.fragment.app.Fragment;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewdialog.ViewDialogTextDiary;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.ViewFingerPrint;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.ViewPassword;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.ViewPattern;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.ViewPincode;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.ViewQuestionPass;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.ViewsetSignature;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.pinlockview.PinLockListener;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.viewfingerprint.callback.FingerprintSecureCallback;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.viewfingerprint.utils.FingerprintToken;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.viewfingerprint.view.Fingerprint;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICallBackItem;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICheckTouch;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

import java.io.File;
import java.util.ArrayList;

public class LockScreenFragment extends Fragment {

    public static String POS_VIEW = "pos_view";

    ViewQuestionPass viewQuestionPass;
    ViewPincode viewPincode;
    ViewPassword viewPassword;
    ViewPattern viewPattern;
    ViewFingerPrint viewFingerPrint;
    ViewsetSignature viewsetSignature;

    private final ICheckTouch isExitCheck;
    private final ICallBackItem callBackTypeCode;
    private boolean isChange, isSetSignature, isTurnFingerPrint;
    private int positionView;
    private String pin, pinConfirm;
    private Gesture gesture, gestureOld;
    private GestureLibrary gestureLibrary;

    public LockScreenFragment(ICallBackItem callBackTypeCode, int positionView, boolean isChange, ICheckTouch isExitCheck) {
        this.callBackTypeCode = callBackTypeCode;
        this.positionView = positionView;
        this.isChange = isChange;
        this.isExitCheck = isExitCheck;
    }

    public static LockScreenFragment newInstance(ICallBackItem callBackTypeCode, int positionView, boolean isChange, ICheckTouch isExitCheck) {
        LockScreenFragment fragment = new LockScreenFragment(callBackTypeCode, positionView, isChange, isExitCheck);
        Bundle args = new Bundle();
        args.putInt(POS_VIEW, positionView);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) positionView = getArguments().getInt(POS_VIEW);

        ArrayList<GestureStroke> lstGestureStroke = null;
        if (!DataLocalManager.getListGestureStroke(Constant.PASS_CODE_GESTURE_STROKE).isEmpty())
            lstGestureStroke = DataLocalManager.getListGestureStroke(Constant.PASS_CODE_GESTURE_STROKE);
        if (gestureLibrary == null) {
            gestureLibrary = GestureLibraries.fromFile(new File(Utils.getStore(requireContext()), Constant.SIGNATURE_FOLDER));
            if (lstGestureStroke != null && !lstGestureStroke.isEmpty()) {
                gestureOld = new Gesture();
                gestureOld.getStrokes().addAll(lstGestureStroke);
                gestureLibrary.addGesture(Constant.SIGNATURE_PIN, gestureOld);
            }
        }

        if (positionView == Constant.POS_QUESTION_PASS) {
            viewQuestionPass = new ViewQuestionPass(requireContext());
            viewQuestionPass.setFitsSystemWindows(true);
            if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
                viewQuestionPass.getViewToolbar().createTheme(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
                viewQuestionPass.createThemeApp(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
            }
        }
        if (positionView == Constant.POS_PIN_CODE) {
            viewPincode = new ViewPincode(requireContext());
            if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
                viewPincode.getViewToolbar().createTheme(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
                viewPincode.createThemeApp(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
            }
        }
        if (positionView == Constant.POS_PASSWORD) {
            viewPassword = new ViewPassword(requireContext());
            viewPassword.setFitsSystemWindows(true);
            if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
                viewPassword.getViewToolbar().createTheme(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
                viewPassword.createThemeApp(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
            }
        }
        if (positionView == Constant.POS_PATTERN) {
            viewPattern = new ViewPattern(requireContext());
            if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
                viewPattern.getViewToolbar().createTheme(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
                viewPattern.createThemeApp(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
            }
        }
        if (positionView == Constant.POS_FINGER_PRINT) {
            viewFingerPrint = new ViewFingerPrint(requireContext());
            if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
                viewFingerPrint.getViewToolbar().createTheme(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
                viewFingerPrint.createThemeApp(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
            }
        }
        if (positionView == Constant.POS_SIGNATURE) {
            viewsetSignature = new ViewsetSignature(requireContext());
            viewsetSignature.getViewToolbar().createTheme(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
            viewsetSignature.createThemeApp(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (positionView == Constant.POS_QUESTION_PASS) {
            initQuestion();
            return viewQuestionPass;
        } else {
            if (DataLocalManager.getInt(Constant.TYPE_LOCK) != -1) {
                isChange = true;
                checkPassCode();
            }
            if (positionView == Constant.POS_PIN_CODE) {
                initPinCode();
                evenClickPinCode();
                return viewPincode;
            } else if (positionView == Constant.POS_PASSWORD) {
                evenClickPassword();
                return viewPassword;
            } else if (positionView == Constant.POS_PATTERN) {
                initPattern();
                evenClickPattern();
                return viewPattern;
            } else if (positionView == Constant.POS_SIGNATURE) {
                initSignature();
                evenClickSignature();
                return viewsetSignature;
            } else if (positionView == Constant.POS_FINGER_PRINT) {
                if (DataLocalManager.getInt(Constant.TYPE_LOCK) == 4)
                    viewFingerPrint.getSwitchCompat().setChecked(true);
                evenClickFingerPrint();
                return viewFingerPrint;
            }
        }

        return null;
    }

    private void initQuestion() {
        if (DataLocalManager.getOption(Constant.PASS_QUESTION_ONE).equals("") || DataLocalManager.getOption(Constant.PASS_QUESTION_TWO).equals(""))
            Toast.makeText(requireContext(), getString(R.string.you_need_set_question), Toast.LENGTH_SHORT).show();

        viewQuestionPass.getViewToolbar().getIvBack().setOnClickListener(v -> getParentFragmentManager().popBackStack());

        if (isChange)
            viewQuestionPass.getTvNote().setText(getString(R.string.confirm_security_question_to_change_password));

        viewQuestionPass.getTvClick().setOnClickListener(v -> {
            String strQuestionOne = viewQuestionPass.getEtQuestionOne().getText().toString();
            String strQuestionTwo = viewQuestionPass.getEtQuestionTwo().getText().toString();

            if (strQuestionOne.equals("") || strQuestionTwo.equals(""))
                Utils.effectVibrate(requireContext());
            else {
                if (!isChange) {
                    Utils.hideKeyboard(requireContext(), v);
                    DataLocalManager.setOption(strQuestionOne, Constant.PASS_QUESTION_ONE);
                    DataLocalManager.setOption(strQuestionTwo, Constant.PASS_QUESTION_TWO);
                    getParentFragmentManager().popBackStack();
                } else {
                    if (!DataLocalManager.getOption(Constant.PASS_QUESTION_ONE).equals(strQuestionOne) || !DataLocalManager.getOption(Constant.PASS_QUESTION_TWO).equals(strQuestionTwo)) {
                        Utils.effectVibrate(requireContext());
                        viewQuestionPass.getTvError().setVisibility(View.VISIBLE);
                        viewQuestionPass.getTvSendGmail().setVisibility(View.VISIBLE);
                    } else {
                        Utils.hideKeyboard(requireContext(), v);
                        resetPassLock();
                        DataLocalManager.setInt(-1, Constant.TYPE_LOCK);
                        DataLocalManager.setCheck(Constant.IS_LOCK, false);
                        isExitCheck.checkTouch(true);
                    }
                }
            }
        });

        viewQuestionPass.getTvSendGmail().setOnClickListener(v -> {
            String questionPass = DataLocalManager.getOption(Constant.PASS_QUESTION_ONE) + ", " + DataLocalManager.getOption(Constant.PASS_QUESTION_TWO);
            Utils.forgotQuestionPass(requireContext(), Utils.encodeQuestionPass(questionPass));
        });
    }

    private void evenClickPassword() {
        viewPassword.getViewToolbar().getIvBack().setOnClickListener(v -> getParentFragmentManager().popBackStack());
        viewPassword.setOnClickListener(v -> Utils.hideKeyboard(requireContext(), v));
        viewPassword.getTvClick().setOnClickListener(v -> {
            pin = viewPassword.getEtSetPassword().getText().toString();
            pinConfirm = viewPassword.getEtConfirm().getText().toString();
            if (pin.equals(pinConfirm)) setPass(Constant.POS_PASSWORD, Constant.PASS_CODE_PASSWORD);
            else {
                Utils.effectVibrate(requireContext());
                viewPassword.getTvError().setVisibility(View.VISIBLE);
                viewPassword.getEtConfirm().setText("");
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void evenClickFingerPrint() {
        viewFingerPrint.getViewToolbar().getIvBack().setOnClickListener(v -> getParentFragmentManager().popBackStack());

        viewFingerPrint.getSwitchCompat().setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) checkBiometric();
        });
    }

    private void checkBiometric() {
        BiometricManager biometricManager = BiometricManager.from(requireContext());
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                viewFingerPrint.getSwitchCompat().setChecked(true);
                setPass(Constant.POS_FINGER_PRINT, Constant.PASS_CODE_FINGERPRINT);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                viewFingerPrint.getSwitchCompat().setChecked(false);
                Toast.makeText(requireContext(), getString(R.string.unsupported_device), Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                viewFingerPrint.getSwitchCompat().setChecked(false);
                Toast.makeText(requireContext(), getString(R.string.hardware_is_unavailable), Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
//                if (Build.VERSION.SDK_INT >= 30) {
                ViewDialogTextDiary viewDialogTextDiary = new ViewDialogTextDiary(requireContext());
                viewDialogTextDiary.getTvTitle().setVisibility(View.GONE);
                viewDialogTextDiary.getTvContent().setText(getString(R.string.your_phone_does_not_have_a_fingerprint_installed_Please_set_fingerprint_to_use_this_feature));

                AlertDialog dialog = new AlertDialog.Builder(requireContext(), R.style.SheetDialog).create();
                dialog.setView(viewDialogTextDiary);
                dialog.setCancelable(false);
                dialog.show();

                viewDialogTextDiary.getViewYesNo().getTvYes().setOnClickListener(v -> {
//                    Intent enrollIntent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                    Intent enrollIntent = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                        enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                        enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BiometricManager.Authenticators.BIOMETRIC_STRONG);
                    } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                        if (checkPhoneScreenLocked())
                            enrollIntent = new Intent(Settings.ACTION_FINGERPRINT_ENROLL);
                        else enrollIntent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
                    } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        if (checkPhoneScreenLocked())
                            enrollIntent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
                    } else
                        Toast.makeText(requireContext(), getString(R.string.cant_open_security_setting), Toast.LENGTH_SHORT).show();

                    startActivity(enrollIntent);
                    isTurnFingerPrint = true;
                    dialog.cancel();
                });

                viewDialogTextDiary.getViewYesNo().getTvNo().setOnClickListener(v -> {
                    viewFingerPrint.getSwitchCompat().setChecked(false);
                    dialog.cancel();
                });
//                }
                break;
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
                viewFingerPrint.getSwitchCompat().setChecked(false);
                Toast.makeText(requireContext(), "BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
                viewFingerPrint.getSwitchCompat().setChecked(false);
                Toast.makeText(requireContext(), "BIOMETRIC_ERROR_UNSUPPORTED", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                viewFingerPrint.getSwitchCompat().setChecked(false);
                Intent enrollIntent = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                    enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BiometricManager.Authenticators.BIOMETRIC_STRONG);
                } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                    if (checkPhoneScreenLocked())
                        enrollIntent = new Intent(Settings.ACTION_FINGERPRINT_ENROLL);
                    else enrollIntent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
                }

                startActivity(enrollIntent);
                Toast.makeText(requireContext(), "BIOMETRIC_STATUS_UNKNOWN", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private boolean checkPhoneScreenLocked() {
        KeyguardManager myKM = (KeyguardManager) requireContext().getSystemService(Context.KEYGUARD_SERVICE);
        return myKM.isKeyguardSecure();
    }

    private void initSignature() {
        viewsetSignature.getTvTitle().setText(getString(R.string.set_your_signature));
        viewsetSignature.getTvClick().setText(getResources().getString(R.string.action_continue));
    }

    private void evenClickSignature() {
        viewsetSignature.getViewToolbar().getIvBack().setOnClickListener(v -> getParentFragmentManager().popBackStack());

        viewsetSignature.getTvClick().setOnClickListener(v -> {
            if (viewsetSignature.getTvTitle().getText().equals(getResources().getString(R.string.set_your_signature)))
                setGestureLibrary(viewsetSignature.getGestureOverlayView().getGesture());
            else {
                if (checkGesture(viewsetSignature.getGestureOverlayView().getGesture())) {
                    setPass(Constant.POS_SIGNATURE, Constant.PASS_CODE_SIGNATURE);
                    isSetSignature = true;
                } else {
                    Utils.effectVibrate(requireContext());
                    viewsetSignature.getGestureOverlayView().clear(false);
                    viewsetSignature.getTvError().setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setGestureLibrary(Gesture gestureCurrent) {
        gesture = gestureCurrent;
        DataLocalManager.setListGestureStroke(gesture.getStrokes(), Constant.PASS_CODE_GESTURE_STROKE);
        gestureLibrary.addGesture(Constant.SIGNATURE_PIN, gesture);

        if (viewsetSignature != null) {
            viewsetSignature.getTvClick().setText(getResources().getString(R.string.add_passcode));
            viewsetSignature.getTvTitle().setText(getText(R.string.confirm_your_signature));
            viewsetSignature.getGestureOverlayView().clear(false);
        }
    }

    private boolean checkGesture(Gesture gesture) {
        if (gesture != null) {

            ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);

            for (Prediction prediction : predictions) {
                String name = prediction.name;
                double score = prediction.score;
                if (score > 2.5 && name.equalsIgnoreCase(Constant.SIGNATURE_PIN)) return true;
            }
        }

        return false;
    }

    private void initPattern() {
        viewPattern.getTvTitle().setText(getResources().getString(R.string.set_your_pattern));
        viewPattern.getTvClick().setText(getResources().getString(R.string.action_continue));
    }

    private void evenClickPattern() {
        viewPattern.getViewToolbar().getIvBack().setOnClickListener(v ->
                getParentFragmentManager().popBackStack());
        viewPattern.getTvClick().setOnClickListener(v -> {
            if (viewPattern.getTvTitle().getText().toString().equals(getResources().getString(R.string.set_your_pattern)))
                pin = Utils.convertListToString(viewPattern.getPatternView().getHitList());
            else pinConfirm = Utils.convertListToString(viewPattern.getPatternView().getHitList());

            if (viewPattern.getTvClick().getText().toString().equals(getResources().getString(R.string.action_continue))) {
                viewPattern.getTvTitle().setText(getResources().getString(R.string.confirm_your_pattern));
                viewPattern.getTvClick().setText(getResources().getString(R.string.add_passcode));
                viewPattern.getPatternView().clearHitState();
            } else {
                if (pinConfirm.equals(pin))
                    setPass(Constant.POS_PATTERN, Constant.PASS_CODE_PATTERN);
                else {
                    Utils.effectVibrate(requireContext());
                    viewPattern.getTvError().setVisibility(View.VISIBLE);
                    viewPattern.getPatternView().clearHitState();
                }
            }
        });
    }

    private void initPinCode() {
        viewPincode.getTvTitle().setText(getResources().getString(R.string.set_your_pincode));
        viewPincode.getTvClick().setText(getResources().getString(R.string.action_continue));
        viewPincode.getPinLockView().setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                viewPincode.getPinLockView().attachIndicatorDots(null);
            }

            @Override
            public void onEmpty() {
                Log.d(Constant.TAG, "onEmpty: ");
            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {
                if (pinLength < 4)
                    viewPincode.getPinLockView().attachIndicatorDots(viewPincode.getIndicatorDots());
            }
        });
    }

    private void evenClickPinCode() {
        viewPincode.getViewToolbar().getIvBack().setOnClickListener(v -> getParentFragmentManager().popBackStack());
        viewPincode.getTvClick().setOnClickListener(v -> {
            if (viewPincode.getPinLockView().getPin().length() < 4) {
                Utils.effectVibrate(requireContext());
                return;
            }

            if (viewPincode.getTvTitle().getText().toString().equals(getResources().getString(R.string.set_your_pincode)))
                pin = viewPincode.getPinLockView().getPin();
            else pinConfirm = viewPincode.getPinLockView().getPin();

            if (viewPincode.getTvTitle().getText().toString().equals(getResources().getString(R.string.set_your_pincode))) {
                viewPincode.getTvTitle().setText(getResources().getString(R.string.confirm_your_pincode));
                viewPincode.getPinLockView().attachIndicatorDots(viewPincode.getIndicatorDots());
                viewPincode.getPinLockView().resetPinLockView();
                viewPincode.getTvClick().setText(getResources().getString(R.string.add_passcode));
            } else {
                if (pinConfirm.equals(pin))
                    setPass(Constant.POS_PIN_CODE, Constant.PASS_CODE_PIN_CODE);
                else {
                    Utils.effectVibrate(requireContext());
                    viewPincode.getTvError().setVisibility(View.VISIBLE);
                    viewPincode.getPinLockView().attachIndicatorDots(viewPincode.getIndicatorDots());
                    viewPincode.getPinLockView().resetPinLockView();
                }
            }
        });
    }

    private void setPass(int type, String type_pass) {
        if (type != Constant.POS_SIGNATURE) resetPassLock();

        DataLocalManager.setCheck(Constant.IS_LOCK, true);
        DataLocalManager.setInt(type, Constant.TYPE_LOCK);
        DataLocalManager.setOption("default", Constant.THEME_LOCK);
        Toast.makeText(requireContext(), getString(R.string.done), Toast.LENGTH_SHORT).show();
        if (type != Constant.POS_FINGER_PRINT) {
            DataLocalManager.setOption(pin, type_pass);
            getParentFragmentManager().popBackStack();
        }
        callBackTypeCode.callBackItem(null, type);
    }

    private void resetPassLock() {
        DataLocalManager.setOption("", Constant.PASS_CODE_PASSWORD);
        DataLocalManager.setOption("", Constant.PASS_CODE_PATTERN);
        DataLocalManager.setOption("", Constant.PASS_CODE_PIN_CODE);
        DataLocalManager.setOption("", Constant.PASS_CODE_SIGNATURE);
        DataLocalManager.setOption("", Constant.PASS_CODE_GESTURE_STROKE);
        DataLocalManager.setOption("", Constant.PASS_CODE_FINGERPRINT);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isTurnFingerPrint) {
            checkBiometric();
            isTurnFingerPrint = false;
        }
        if (gesture != null) setGestureLibrary(gesture);
    }

//    public void clickTheme() {
//        ThemeFragment themeFragment = ThemeFragment.newInstance();
//        Utils.replaceFragment(getParentFragmentManager(), themeFragment, false, true, true);
//    }

    private void checkPassCode() {
        if (DataLocalManager.getInt(Constant.TYPE_LOCK) == Constant.POS_FINGER_PRINT) {
            if (BiometricManager.from(requireContext()).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) != BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED)
                initFingerPrint();
            else errorFingerPrint();
        } else {
            CheckPasscodeFragment checkPasscodeFragment = CheckPasscodeFragment.newInstance(false, isTouch -> {
                getParentFragmentManager().popBackStack(CheckPasscodeFragment.class.getSimpleName(), 0);
            });
            Utils.replaceFragment(getParentFragmentManager(), checkPasscodeFragment, true, true, false);
        }
    }

    private void initFingerPrint() {

        View v = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_check_fingerprintf, null);

        Fingerprint fingerprint = v.findViewById(R.id.fingerprint);
        TextView tvError = v.findViewById(R.id.tvError);
        TextView tvUnlock = v.findViewById(R.id.tvUnlock);

        tvUnlock.setText(Utils.underLine(getString(R.string.confirm_fingerprint_to_unlock_apps)));
        tvError.setVisibility(View.GONE);

        AlertDialog dialog = new AlertDialog.Builder(requireContext(), R.style.SheetDialog).create();
        dialog.setView(v);
        dialog.setCancelable(false);
        dialog.show();

        tvUnlock.setOnClickListener(vUnlock -> errorFingerPrint());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            fingerprint.callback(new FingerprintSecureCallback() {
                @Override
                public void onAuthenticationSucceeded() {
                    dialog.cancel();
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
            }, "KeyName2").circleScanningColor(android.R.color.black).fingerprintScanningColor(R.color.orange).authenticate();
    }

    private void errorFingerPrint() {
        Toast.makeText(requireContext(), getString(R.string.fingerPrint_error_none_enrolled), Toast.LENGTH_SHORT).show();
        LockScreenFragment lockScreenFragment = LockScreenFragment.newInstance((o, pos) -> {
        }, Constant.POS_QUESTION_PASS, true, isExitCheck -> {
            getParentFragmentManager().popBackStack();
        });
        Utils.replaceFragment(getParentFragmentManager(), lockScreenFragment, true, true, true);
    }

    public void popBackStack() {
        getParentFragmentManager().popBackStack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (gestureOld != null && !isSetSignature) setGestureLibrary(gestureOld);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (gestureOld != null && !isSetSignature) setGestureLibrary(gestureOld);
    }
}
