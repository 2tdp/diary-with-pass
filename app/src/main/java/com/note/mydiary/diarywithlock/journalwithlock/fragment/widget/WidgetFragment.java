package com.note.mydiary.diarywithlock.journalwithlock.fragment.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewwidget.ViewWidget;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICallBackItem;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;

public class WidgetFragment extends Fragment {

    ViewWidget viewWidget;

    private final ICallBackItem callBack;

    public WidgetFragment(ICallBackItem callBack) {
        this.callBack = callBack;
    }

    public static WidgetFragment newInstance(ICallBackItem callBack) {
        return new WidgetFragment(callBack);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewWidget = new ViewWidget(requireContext());
        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
            viewWidget.getViewToolbar().createTheme(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
            viewWidget.createThemeApp(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        evenClick();
        return viewWidget;
    }

    private void evenClick() {
        viewWidget.getViewToolbar().getIvBack().setOnClickListener(v -> getParentFragmentManager().popBackStack(WidgetFragment.class.getSimpleName(), 1));
        viewWidget.getIvWidget1().setOnClickListener(v -> callBack.callBackItem(null, 1));
        viewWidget.getIvWidget2().setOnClickListener(v -> callBack.callBackItem(null, 2));
        viewWidget.getIvWidget3().setOnClickListener(v -> callBack.callBackItem(null, 3));
    }
}