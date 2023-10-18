package com.note.mydiary.diarywithlock.journalwithlock.fragment.passcode;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.fragment.app.Fragment;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.ViewPasscode;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICallBackItem;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICheckTouch;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

public class PasscodeFragment extends Fragment {

    public static final String IS_CHANGE = "isChangePasscode";
    public static final String NAME_THEME = "nameTheme";

    private final ICheckTouch isLock;
    private final ICallBackItem callBackTypeCode;

    private String nameTheme;
    private boolean isChange;
    ViewPasscode viewPasscode;

    private LockScreenFragment lockScreenFragment;
    private boolean isFirst;
    private int typeLock = -1;

    public PasscodeFragment(ICallBackItem callBackTypeCode, ICheckTouch isLock) {
        this.callBackTypeCode = callBackTypeCode;
        this.isLock = isLock;
    }

    public static PasscodeFragment newInstance(String nameTheme, ICallBackItem callBackTypeCode, ICheckTouch isLock, boolean isChangePasscode) {
        PasscodeFragment passcodeFragment = new PasscodeFragment(callBackTypeCode, isLock);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_CHANGE, isChangePasscode);
        bundle.putString(NAME_THEME, nameTheme);
        passcodeFragment.setArguments(bundle);
        return passcodeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nameTheme = getArguments().getString(NAME_THEME);
            isChange = getArguments().getBoolean(IS_CHANGE);
        }

        viewPasscode = new ViewPasscode(requireContext());
        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
            viewPasscode.getViewToolbar().createTheme(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
            viewPasscode.createTheme(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        checkView();
        evenClick();
        return viewPasscode;
    }

    private void evenClick() {
        viewPasscode.getViewToolbar().getIvBack().setOnClickListener(v ->
                getParentFragmentManager().popBackStack(PasscodeFragment.class.getSimpleName(), 1));
        viewPasscode.getSwitchCompat().setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (DataLocalManager.getInt(Constant.TYPE_LOCK) != -1)
                if (isChecked) DataLocalManager.setCheck(Constant.IS_LOCK, true);
                else {
                    CheckPasscodeFragment checkPasscodeFragment = CheckPasscodeFragment.newInstance(false, isTouch -> {
                        getParentFragmentManager().popBackStack(CheckPasscodeFragment.class.getSimpleName(), 0);
                        DataLocalManager.setCheck(Constant.IS_LOCK, false);
                        viewPasscode.getSwitchCompat().setChecked(false);
                        Utils.effectVibrate(requireContext());
                    });
                    Utils.replaceFragment(getParentFragmentManager(), checkPasscodeFragment, true, true, false);
                }
            else {
                viewPasscode.getSwitchCompat().setChecked(false);
                Utils.effectVibrate(requireContext());
                Toast.makeText(requireContext(), getString(R.string.you_need_create_a_passcode), Toast.LENGTH_SHORT).show();
            }
        });

        viewPasscode.getViewPassword().setOnClickListener(v -> {
            if (DataLocalManager.getOption(Constant.PASS_QUESTION_ONE).equals("") || DataLocalManager.getOption(Constant.PASS_QUESTION_TWO).equals("")) {
                isFirst = true;
                typeLock = Constant.POS_PASSWORD;
                createFragment(Constant.POS_QUESTION_PASS);
            } else createFragment(Constant.POS_PASSWORD);
        });
        viewPasscode.getViewPincode().setOnClickListener(v -> {
            if (DataLocalManager.getOption(Constant.PASS_QUESTION_ONE).equals("") || DataLocalManager.getOption(Constant.PASS_QUESTION_TWO).equals("")) {
                isFirst = true;
                typeLock = Constant.POS_PIN_CODE;
                createFragment(Constant.POS_QUESTION_PASS);
            } else createFragment(Constant.POS_PIN_CODE);
        });
        viewPasscode.getViewPattern().setOnClickListener(v -> {
            if (DataLocalManager.getOption(Constant.PASS_QUESTION_ONE).equals("") || DataLocalManager.getOption(Constant.PASS_QUESTION_TWO).equals("")) {
                isFirst = true;
                typeLock = Constant.POS_PATTERN;
                createFragment(Constant.POS_QUESTION_PASS);
            } else createFragment(Constant.POS_PATTERN);
        });
        viewPasscode.getViewFingerprint().setOnClickListener(v -> {
            if (DataLocalManager.getOption(Constant.PASS_QUESTION_ONE).equals("") || DataLocalManager.getOption(Constant.PASS_QUESTION_TWO).equals("")) {
                isFirst = true;
                typeLock = Constant.POS_FINGER_PRINT;
                createFragment(Constant.POS_QUESTION_PASS);
            } else createFragment(Constant.POS_FINGER_PRINT);
        });
        viewPasscode.getViewSignature().setOnClickListener(v -> {
            if (DataLocalManager.getOption(Constant.PASS_QUESTION_ONE).equals("") || DataLocalManager.getOption(Constant.PASS_QUESTION_TWO).equals("")) {
                isFirst = true;
                typeLock = Constant.POS_SIGNATURE;
                createFragment(Constant.POS_QUESTION_PASS);
            } else createFragment(Constant.POS_SIGNATURE);
        });
    }

    private void createFragment(int positionView) {
        lockScreenFragment = LockScreenFragment.newInstance(callBackTypeCode, positionView, isChange, isExitCheck -> {
            lockScreenFragment.popBackStack();
        });
        Utils.replaceFragment(getParentFragmentManager(), lockScreenFragment, false, true, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPasscode.getViewFingerprint().setVisibility(checkBiometric() ? View.VISIBLE : View.GONE);


        if (DataLocalManager.getCheck(Constant.IS_LOCK))
            viewPasscode.getSwitchCompat().setChecked(true);

        if ((!DataLocalManager.getOption(Constant.PASS_QUESTION_ONE).equals("") || !DataLocalManager.getOption(Constant.PASS_QUESTION_TWO).equals("")) && isFirst) {
            viewPasscode.getSwitchCompat().setChecked(true);
            switch (typeLock) {
                case Constant.POS_PASSWORD:
                    createFragment(Constant.POS_PASSWORD);
                    break;
                case Constant.POS_PIN_CODE:
                    createFragment(Constant.POS_PIN_CODE);
                    break;
                case Constant.POS_PATTERN:
                    createFragment(Constant.POS_PATTERN);
                    break;
                case Constant.POS_FINGER_PRINT:
                    createFragment(Constant.POS_FINGER_PRINT);
                    break;
                case Constant.POS_SIGNATURE:
                    createFragment(Constant.POS_SIGNATURE);
                    break;
            }
            isFirst = false;
        }
    }

    private boolean checkBiometric() {
        BiometricManager biometricManager = BiometricManager.from(requireContext());
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(requireContext(), "BIOMETRIC_ERROR_NO_HARDWARE", Toast.LENGTH_SHORT).show();
                return false;
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
                Toast.makeText(requireContext(), "BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED", Toast.LENGTH_SHORT).show();
                return false;
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
                Toast.makeText(requireContext(), "BIOMETRIC_ERROR_UNSUPPORTED", Toast.LENGTH_SHORT).show();
                return false;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(requireContext(), "BIOMETRIC_ERROR_HW_UNAVAILABLE", Toast.LENGTH_SHORT).show();
                return false;
            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
            case BiometricManager.BIOMETRIC_SUCCESS:
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                return true;
        }
        return false;
    }

    private void checkView() {
        if (isChange) {
            viewPasscode.getRlSwitch().setVisibility(View.GONE);
            viewPasscode.getViewToolbar().getTvTitle().setText(getResources().getString(R.string.menu_1));
            viewPasscode.getTvDes().setText(getResources().getString(R.string.select_the_passcode_you_want_to_change));
            viewPasscode.getViewFingerprint().setVisibility(View.GONE);

            viewPasscode.getViewPassword().setVisibility(DataLocalManager.getInt(Constant.TYPE_LOCK) == Constant.POS_PASSWORD ? View.VISIBLE : View.GONE);
            viewPasscode.getViewPincode().setVisibility(DataLocalManager.getInt(Constant.TYPE_LOCK) == Constant.POS_PIN_CODE ? View.VISIBLE : View.GONE);
            viewPasscode.getViewPattern().setVisibility(DataLocalManager.getInt(Constant.TYPE_LOCK) == Constant.POS_PATTERN ? View.VISIBLE : View.GONE);
            viewPasscode.getViewSignature().setVisibility(DataLocalManager.getInt(Constant.TYPE_LOCK) == Constant.POS_SIGNATURE ? View.VISIBLE : View.GONE);
        } else {
            isLock.checkTouch(true);
            viewPasscode.getRlSwitch().setVisibility(View.VISIBLE);
            viewPasscode.getViewToolbar().getTvTitle().setText(getResources().getString(R.string.passcode));
            viewPasscode.getTvDes().setText(getResources().getString(R.string.choose_your_passcode_style));

            viewPasscode.getViewFingerprint().setVisibility(View.VISIBLE);

            viewPasscode.getViewPassword().getIvTick().setVisibility(DataLocalManager.getInt(Constant.TYPE_LOCK) == Constant.POS_PASSWORD ? View.VISIBLE : View.GONE);
            viewPasscode.getViewPincode().getIvTick().setVisibility(DataLocalManager.getInt(Constant.TYPE_LOCK) == Constant.POS_PIN_CODE ? View.VISIBLE : View.GONE);
            viewPasscode.getViewPattern().getIvTick().setVisibility(DataLocalManager.getInt(Constant.TYPE_LOCK) == Constant.POS_PATTERN ? View.VISIBLE : View.GONE);
            viewPasscode.getViewFingerprint().getIvTick().setVisibility(DataLocalManager.getInt(Constant.TYPE_LOCK) == Constant.POS_FINGER_PRINT ? View.VISIBLE : View.GONE);
            viewPasscode.getViewSignature().getIvTick().setVisibility(DataLocalManager.getInt(Constant.TYPE_LOCK) == Constant.POS_SIGNATURE ? View.VISIBLE : View.GONE);
        }
    }
}