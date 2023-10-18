package com.note.mydiary.diarywithlock.journalwithlock.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewhome.ViewHomeAllDiary;
import com.note.mydiary.diarywithlock.journalwithlock.activity.database.DatabaseRealm;
import com.note.mydiary.diarywithlock.journalwithlock.adapter.DiaryHomeAdapter;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICheckTouch;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.previewdiary.PreviewDiaryFragment;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.showalldiary.ShowAllDiaryFragment;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.DiaryModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.remiads.ads.FullManager;

import java.util.List;

import io.realm.Sort;

public class HomeAllDiaryFragment extends Fragment {

    ViewHomeAllDiary viewHomeAllDiary;

    DiaryHomeAdapter diaryHomeAdapter;
    private Sort sort;

    private final ICheckTouch resetCalendar;

    private DiaryModel diaryModel;

    public HomeAllDiaryFragment(Sort sort, ICheckTouch resetCalendar) {
        this.sort = sort;
        this.resetCalendar = resetCalendar;
    }

    public static HomeAllDiaryFragment newInstance(Sort sort, ICheckTouch resetCalendar) {
        Bundle args = new Bundle();
        HomeAllDiaryFragment fragment = new HomeAllDiaryFragment(sort, resetCalendar);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewHomeAllDiary = new ViewHomeAllDiary(requireContext());
        viewHomeAllDiary.setParams();
        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default"))
            viewHomeAllDiary.createTheme(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDataDiaryHome(sort);
        return viewHomeAllDiary;
    }

    public void getDataDiaryHome(Sort sort) {
        this.sort = sort;
        DatabaseRealm.getAll(sort, (o, p) -> {
            List<DiaryModel> lstDiary = (List<DiaryModel>) o;
            if (lstDiary.isEmpty()) {
                viewHomeAllDiary.getvNoData().setVisibility(View.VISIBLE);
                viewHomeAllDiary.getRcvDiary().setVisibility(View.GONE);
                return;
            }

            viewHomeAllDiary.getvNoData().setVisibility(View.GONE);
            viewHomeAllDiary.getRcvDiary().setVisibility(View.VISIBLE);

            if (diaryHomeAdapter != null) {
                diaryHomeAdapter.setDateDefault();
                diaryHomeAdapter.setData(lstDiary);
            } else {
                diaryHomeAdapter = new DiaryHomeAdapter(requireContext(), isDel -> getDataDiaryHome(this.sort), resetCalendar, (ob, pos, isShowAll) -> {
                    diaryHomeAdapter.setDateDefault();
                    if (!isShowAll) {
                        PreviewDiaryFragment previewDiaryFragment = PreviewDiaryFragment.newInstance(((DiaryModel) ob).getId(),
                                isDelete -> getDataDiaryHome(this.sort), resetCalendar);
                        Utils.replaceFragment(getParentFragmentManager(), previewDiaryFragment, true, true, true);
                    } else {
                        diaryModel = (DiaryModel) ob;
                        if (getActivity() != null)
                            FullManager.getInstance().showAds(getActivity(), this::showAll);
                        else
                            showAll();
                    }
                });

                diaryHomeAdapter.setData(lstDiary);
                LinearLayoutManager manager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
                viewHomeAllDiary.getRcvDiary().setLayoutManager(manager);
                viewHomeAllDiary.getRcvDiary().setAdapter(diaryHomeAdapter);
            }
        });
    }

    private void showAll(){
        ShowAllDiaryFragment showAllDiaryFragment = ShowAllDiaryFragment.newInstance(this.sort, diaryModel.getDateTimeStamp(), resetCalendar);
        Utils.replaceFragment(getParentFragmentManager(), showAllDiaryFragment, true, true, true);
    }
}
