package com.note.mydiary.diarywithlock.journalwithlock.fragment.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewwidget.ViewPickDiaryWidget;
import com.note.mydiary.diarywithlock.journalwithlock.activity.database.DatabaseRealm;
import com.note.mydiary.diarywithlock.journalwithlock.adapter.PickDiaryWidgetAdapter;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICallBackItem;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.DiaryModel;

import java.util.ArrayList;

import io.realm.Sort;

public class PickDiaryWidgetFragment extends Fragment {

    ViewPickDiaryWidget viewPickDiaryWidget;

    private final ICallBackItem callBack;

    public PickDiaryWidgetFragment(ICallBackItem callBack) {
        this.callBack = callBack;
    }

    public static PickDiaryWidgetFragment newInstance(ICallBackItem callBack) {

        Bundle args = new Bundle();

        PickDiaryWidgetFragment fragment = new PickDiaryWidgetFragment(callBack);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewPickDiaryWidget = new ViewPickDiaryWidget(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        init();
        return viewPickDiaryWidget;
    }

    private void init() {
        viewPickDiaryWidget.getViewToolbar().getIvBack().setOnClickListener(v -> getParentFragmentManager().popBackStack());
        DatabaseRealm.getAll(Sort.ASCENDING, (o, pos) -> {
            ArrayList<DiaryModel> lstDiary = (ArrayList<DiaryModel>) o;
            if (lstDiary.isEmpty()) return;

            PickDiaryWidgetAdapter pickDiaryWidgetAdapter = new PickDiaryWidgetAdapter(requireContext(), (ob, p) -> {
                getParentFragmentManager().popBackStack();
                callBack.callBackItem(ob, p);
            });

            pickDiaryWidgetAdapter.setData(lstDiary);
            LinearLayoutManager manager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
            viewPickDiaryWidget.getRcv().setLayoutManager(manager);
            viewPickDiaryWidget.getRcv().setAdapter(pickDiaryWidgetAdapter);
        });
    }
}
