package com.note.mydiary.diarywithlock.journalwithlock.fragment.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewsearch.ViewSearchDiary;
import com.note.mydiary.diarywithlock.journalwithlock.activity.database.DatabaseRealm;
import com.note.mydiary.diarywithlock.journalwithlock.adapter.DiarySearchAdapter;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICheckTouch;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.previewdiary.PreviewDiaryFragment;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.DiaryModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

import java.util.List;

public class SearchFragment extends Fragment {

    ViewSearchDiary viewSearch;

    private final ICheckTouch isDel;
    private final ICheckTouch resetCalendar;

    public SearchFragment(ICheckTouch isDel, ICheckTouch resetCalendar) {
        this.isDel = isDel;
        this.resetCalendar = resetCalendar;
    }

    public static SearchFragment newInstance(ICheckTouch isDel, ICheckTouch resetCalendar) {
        return new SearchFragment(isDel, resetCalendar);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewSearch = new ViewSearchDiary(requireContext());
        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
            viewSearch.createTheme(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
            viewSearch.getViewToolbar().createTheme(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        init();
        return viewSearch;
    }

    private void init() {
        viewSearch.getViewToolbar().getIvBack().setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
            Utils.hideKeyboard(requireContext(), v);
        });

        viewSearch.getEtSearch().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getData(String key) {
        DatabaseRealm.searchOtherDiary(key, (o, p) -> {
            List<DiaryModel> lstDiary = (List<DiaryModel>) o;
            if (lstDiary.isEmpty()) {
                viewSearch.getTvNoData().setVisibility(View.VISIBLE);
                viewSearch.getRcvSearch().setVisibility(View.GONE);
                return;
            }

            viewSearch.getTvNoData().setVisibility(View.GONE);
            viewSearch.getRcvSearch().setVisibility(View.VISIBLE);
            DiarySearchAdapter diarySearchAdapter = new DiarySearchAdapter(requireContext(), isDelete -> {
                isDel.checkTouch(true);
                getData(key);
            }, resetCalendar, (ob, pos) -> {
                PreviewDiaryFragment previewDiaryFragment = PreviewDiaryFragment.newInstance(((DiaryModel) ob).getId(), isDelete -> getData(key), resetCalendar);
                Utils.replaceFragment(getParentFragmentManager(), previewDiaryFragment, true, true, true);
            });

            diarySearchAdapter.setData(lstDiary);

            viewSearch.getRcvSearch().setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
            viewSearch.getRcvSearch().setAdapter(diarySearchAdapter);
        });
    }
}
