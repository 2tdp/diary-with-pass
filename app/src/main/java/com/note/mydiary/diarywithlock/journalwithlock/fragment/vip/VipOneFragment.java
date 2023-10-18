package com.note.mydiary.diarywithlock.journalwithlock.fragment.vip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.vip.ViewVipOne;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

public class VipOneFragment extends Fragment {

    ViewVipOne viewVipOne;

    public VipOneFragment() {
    }

    public static VipOneFragment newInstance() {
        return new VipOneFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewVipOne = new ViewVipOne(requireContext());
        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
            viewVipOne.getViewToolbar().createTheme(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
            viewVipOne.createTheme(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        evenClick();
        return viewVipOne;
    }

    private void evenClick() {
        viewVipOne.getTvContinue().setOnClickListener(v -> {
            VipTwoFragment vipTwoFragment = VipTwoFragment.newInstance();
            Utils.replaceFragment(requireActivity().getSupportFragmentManager(), vipTwoFragment, true, true, true);
        });

        viewVipOne.getViewToolbar().getIvBack().setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }
}