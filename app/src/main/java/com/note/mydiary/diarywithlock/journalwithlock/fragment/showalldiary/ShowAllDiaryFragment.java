package com.note.mydiary.diarywithlock.journalwithlock.fragment.showalldiary;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewdialog.ViewDialogTextDiary;
import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewshowall.ViewShowAllDiary;
import com.note.mydiary.diarywithlock.journalwithlock.activity.database.DatabaseRealm;
import com.note.mydiary.diarywithlock.journalwithlock.adapter.DiaryShowAllAdapter;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICheckTouch;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.previewdiary.PreviewDiaryFragment;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.DiaryModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import io.realm.RealmList;
import io.realm.Sort;

public class ShowAllDiaryFragment extends Fragment {

    public static String TIME = "time";

    private ViewShowAllDiary viewShowAllDiary;

    private DiaryShowAllAdapter diaryShowAllAdapter;
    private final ICheckTouch resetCalendar;
    private Sort sort;
    private int w;
    private long time;

    public ShowAllDiaryFragment(Sort sort, ICheckTouch resetCalendar) {
        this.sort = sort;
        this.resetCalendar = resetCalendar;
    }

    public static ShowAllDiaryFragment newInstance(Sort sort, long time, ICheckTouch resetCalendar) {
        ShowAllDiaryFragment fragment = new ShowAllDiaryFragment(sort, resetCalendar);
        Bundle args = new Bundle();
        args.putLong(TIME, time);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        w = getResources().getDisplayMetrics().widthPixels;
        if (getArguments() != null) time = getArguments().getLong(TIME);

        viewShowAllDiary = new ViewShowAllDiary(requireContext());
        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default")) {
            viewShowAllDiary.getViewToolbar().createTheme(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
            viewShowAllDiary.createThemeApp(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        init();
        evenClick();
        return viewShowAllDiary;
    }

    private void init() {
        viewShowAllDiary.getViewToolbar().getTvTitle().setText(new SimpleDateFormat(Constant.MONTH_YEAR, Locale.ENGLISH).format(time));

        getData(time);
    }

    private void getData(long time) {
        DatabaseRealm.getAllDiaryInMonth(sort, time, (o, p) -> {
            List<DiaryModel> lstDiary = (List<DiaryModel>) o;
            if (lstDiary.isEmpty()) return;

            diaryShowAllAdapter = new DiaryShowAllAdapter(
                    requireContext(),
                    isDel -> {
                        DatabaseRealm.getAllDiaryInMonth(sort, time, (o1, pos) -> diaryShowAllAdapter.setData((List<DiaryModel>) o1));
                        resetCalendar.checkTouch(true);
                    },
                    isMultiDel -> {
                        viewShowAllDiary.getViewToolbar().getIvVip().setVisibility(View.VISIBLE);
                        viewShowAllDiary.getViewToolbar().getIvVip().setImageResource(R.drawable.ic_delete);
                        viewShowAllDiary.getViewToolbar().getIvBack().setVisibility(View.GONE);
                        viewShowAllDiary.getViewToolbar().getTvCancel().setVisibility(View.VISIBLE);
                    },

                    resetCalendar,

                    (ob, pos) -> {
                        PreviewDiaryFragment previewDiaryFragment = PreviewDiaryFragment.newInstance(((DiaryModel) ob).getId(), isDelete -> getData(time), resetCalendar);
                        Utils.replaceFragment(getParentFragmentManager(), previewDiaryFragment, true, true, true);
                    });

            diaryShowAllAdapter.setData(lstDiary);
            viewShowAllDiary.getRcv().setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
            viewShowAllDiary.getRcv().setAdapter(diaryShowAllAdapter);
        });
    }

    private void evenClick() {
        viewShowAllDiary.getViewToolbar().getIvBack().setOnClickListener(v -> {
            getParentFragmentManager().popBackStack(this.getClass().getSimpleName(), 1);
            resetCalendar.checkTouch(true);
        });
        viewShowAllDiary.getViewToolbar().getTvCancel().setOnClickListener(v -> clickCancelMultiDel());
        viewShowAllDiary.getViewToolbar().getIvVip().setOnClickListener(v -> showDialogMultiDel());
    }

    @SuppressLint("SetTextI18n")
    private void showDialogMultiDel() {
        if (diaryShowAllAdapter != null) {
            RealmList<DiaryModel> lstDelDiary = diaryShowAllAdapter.getSelected();

            ViewDialogTextDiary viewDialogTextDiary = new ViewDialogTextDiary(requireContext());
            viewDialogTextDiary.getTvTitle().setText(requireContext().getResources().getString(R.string.del_diary));
            viewDialogTextDiary.getTvContent().setText("Are you sure want to delete " + lstDelDiary.size() + " diaries?");

            AlertDialog dialog = new AlertDialog.Builder(requireContext(), R.style.SheetDialog).create();
            dialog.setView(viewDialogTextDiary);
            dialog.setCancelable(false);
            dialog.show();
            viewDialogTextDiary.getLayoutParams().width = (int) (84.444f * w / 100);

            viewDialogTextDiary.getViewYesNo().getTvNo().setOnClickListener(vNo -> dialog.cancel());
            viewDialogTextDiary.getViewYesNo().getTvYes().setOnClickListener(vYes -> {
                delMultiDiary(lstDelDiary, 0);
                dialog.cancel();
                clickCancelMultiDel();
            });
        }
    }

    private void delMultiDiary(RealmList<DiaryModel> lstDelDiary, int position) {
        if (position < lstDelDiary.size()) {
            DiaryModel diary = lstDelDiary.get(position);
            if (diary != null)
                DatabaseRealm.delOtherDiary(diary.getId(), isDone -> {
                    delMultiDiary(lstDelDiary, position + 1);
                }, resetCalendar);
        } else
            DatabaseRealm.getAllDiaryInMonth(sort, time, (o, pos) -> diaryShowAllAdapter.setData((List<DiaryModel>) o));
    }

    private void clickCancelMultiDel() {
        diaryShowAllAdapter.unMultiDel();
        viewShowAllDiary.getViewToolbar().getIvVip().setVisibility(View.GONE);
        viewShowAllDiary.getViewToolbar().getIvBack().setVisibility(View.VISIBLE);
        viewShowAllDiary.getViewToolbar().getTvCancel().setVisibility(View.GONE);
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }
}
