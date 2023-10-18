package com.note.mydiary.diarywithlock.journalwithlock.fragment.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewhome.ViewHomeDiaryInDay;
import com.note.mydiary.diarywithlock.journalwithlock.activity.database.DatabaseRealm;
import com.note.mydiary.diarywithlock.journalwithlock.adapter.DiaryInDayCalendarAdapter;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICallBackItem;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICheckTouch;
import com.note.mydiary.diarywithlock.journalwithlock.customview.CustomCalendar.CalendarListener;
import com.note.mydiary.diarywithlock.journalwithlock.fragment.previewdiary.PreviewDiaryFragment;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.DiaryModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeDiaryInDayFragment extends Fragment {

    ViewHomeDiaryInDay viewHomeDiaryInDay;

    private DiaryInDayCalendarAdapter dayCalendarAdapter;

    private final ICheckTouch resetCalendar;
    private final ICallBackItem callBackDate;
    private Date dateSelected;

    public HomeDiaryInDayFragment(ICheckTouch resetCalendar, ICallBackItem callBackDate) {
        this.resetCalendar = resetCalendar;
        this.callBackDate = callBackDate;
    }

    public static HomeDiaryInDayFragment newInstance(ICheckTouch resetCalendar, ICallBackItem callBackDate) {

        Bundle args = new Bundle();

        HomeDiaryInDayFragment fragment = new HomeDiaryInDayFragment(resetCalendar, callBackDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewHomeDiaryInDay = new ViewHomeDiaryInDay(requireContext());
        viewHomeDiaryInDay.setParams();
        if (!DataLocalManager.getOption(Constant.THEME_APP).equals("default"))
            viewHomeDiaryInDay.createTheme(requireContext(), "/theme_app/" + DataLocalManager.getOption(Constant.THEME_APP));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        init();
        return viewHomeDiaryInDay;
    }

    private void init() {
        if (dateSelected == null)
            getDataDiaryCalendar(Calendar.getInstance(Constant.LOCALE_DEFAULT).getTime().getTime());
        else {
            getDataDiaryCalendar(dateSelected.getTime());
            setSelectedDay(dateSelected);
        }

        viewHomeDiaryInDay.getCalendarView().setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                getDataDiaryCalendar(date.getTime());
                dateSelected = date;
                callBackDate.callBackItem(dateSelected, -1);
            }

            @Override
            public void onMonthChanged(Date date) {
                Calendar calendar = Calendar.getInstance(Constant.LOCALE_DEFAULT);
                int monthCurrent = calendar.get(Calendar.MONTH) + 1;
                int month = Integer.parseInt(new SimpleDateFormat("MM", Constant.LOCALE_DEFAULT).format(date));

                if (month == monthCurrent) getDataDiaryCalendar(calendar.getTime().getTime());
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    public void getDataDiaryCalendar(long dayCurrent) {
        DatabaseRealm.getAllDiaryInDay(dayCurrent, (o, p) -> {
            List<DiaryModel> lstDiary = (List<DiaryModel>) o;
            if (lstDiary.isEmpty()) {
                viewHomeDiaryInDay.getvNoData().setVisibility(View.VISIBLE);
                viewHomeDiaryInDay.getRcvDiary().setVisibility(View.GONE);
                return;
            }

            viewHomeDiaryInDay.getvNoData().setVisibility(View.GONE);
            viewHomeDiaryInDay.getRcvDiary().setVisibility(View.VISIBLE);

            if (dayCalendarAdapter != null) dayCalendarAdapter.setData(lstDiary);
            else {
                dayCalendarAdapter = new DiaryInDayCalendarAdapter(requireContext(),
                        isDel -> Toast.makeText(requireContext(), "Delete Done", Toast.LENGTH_SHORT).show(),
                        resetCalendar,
                        (ob, pos) -> {
                            PreviewDiaryFragment previewDiaryFragment = PreviewDiaryFragment.newInstance(((DiaryModel) ob).getId()
                                    , isDelete -> {
                                        if (dateSelected == null)
                                            getDataDiaryCalendar(Calendar.getInstance(Constant.LOCALE_DEFAULT).getTime().getTime());
                                        else getDataDiaryCalendar(dateSelected.getTime());
                                    }, resetCalendar);

                            Utils.replaceFragment(getParentFragmentManager(), previewDiaryFragment, true, true, true);
                        });

                dayCalendarAdapter.setData(lstDiary);

                viewHomeDiaryInDay.getRcvDiary().setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
                viewHomeDiaryInDay.getRcvDiary().setAdapter(dayCalendarAdapter);
            }
        });
    }

    public void resetCalendar() {
        viewHomeDiaryInDay.getCalendarView().refreshCalendar(viewHomeDiaryInDay.getCalendarView().getCurrentCalendar());
    }

    public void setSelectedDay(Date date) {
        viewHomeDiaryInDay.getCalendarView().markDayAsSelectedDay(date);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dateSelected = null;
    }
}
