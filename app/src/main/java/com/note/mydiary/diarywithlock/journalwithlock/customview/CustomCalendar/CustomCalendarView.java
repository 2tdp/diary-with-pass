/*
 * Copyright (c) 2016 Stacktips {link: http://stacktips.com}.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.note.mydiary.diarywithlock.journalwithlock.customview.CustomCalendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.customview.CustomCalendar.utils.CalendarUtils;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsTheme;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomCalendarView extends LinearLayout {
    private Context mContext;
    private LinearLayout llCalendarView;
    private View view;
    private TextView dateTitle;
    private CalendarListener calendarListener;
    private Calendar currentCalendar;
    private Date lastSelectedDay;
    private Typeface customTypeface;

    private int firstDayOfWeek = Calendar.SUNDAY;
    private List<DayDecorator> decorators = null;

    private static final String DAY_OF_WEEK = "dayOfWeek";
    private static final String DAY_OF_MONTH_TEXT = "dayOfMonthText";
    private static final String DAY_OF_MONTH_CONTAINER = "dayOfMonthContainer";

    private int disabledDayBackgroundColor;
    private int disabledDayTextColor;
    private int calendarBackgroundColor;
    private int weekLayoutBackgroundColor;
    private int calendarTitleBackgroundColor;
    private int selectedDayTextColor;
    private Drawable selectedDayBackgroundColor;
    private Drawable diaryDayBackgroundColor;
    private int titleDayOfWeekTextColor;
    private int dayOfWeekTextColor;
    private int currentDayOfMonthColor;
    private int dayOfMonthTextColor;

    private int currentMonthIndex = 0;
    private boolean isOverflowDateVisible = true;

    public CustomCalendarView(Context context) {
        this(context, null);
        init(context);
    }

    public CustomCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        if (isInEditMode()) return;

        init(context);

        initializeCalendar();
    }

    private void init(Context context) {
        int w = getResources().getDisplayMetrics().widthPixels;
        calendarBackgroundColor = context.getResources().getColor(R.color.white);

        calendarTitleBackgroundColor = context.getResources().getColor(R.color.white);

        weekLayoutBackgroundColor = context.getResources().getColor(R.color.white);
        titleDayOfWeekTextColor = context.getResources().getColor(R.color.titleDayOfWeek);

        dayOfWeekTextColor = context.getResources().getColor(R.color.black);
        dayOfMonthTextColor = context.getResources().getColor(R.color.black);
        currentDayOfMonthColor = context.getResources().getColor(R.color.white);

        disabledDayBackgroundColor = context.getResources().getColor(R.color.white);
        disabledDayTextColor = context.getResources().getColor(R.color.white);
        selectedDayTextColor = context.getResources().getColor(R.color.white);

        selectedDayBackgroundColor = Utils.createBackground(new int[]{Color.parseColor("#FFAF7D")}, (int) (1.5f * w / 100), -1, -1);
        diaryDayBackgroundColor = Utils.createBackground(new int[]{Color.parseColor("#00B389")}, (int) (3.5f * w / 100), -1, -1);
    }

    public void initializeCalendar() {
        final LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflate.inflate(R.layout.custom_calendar_layout, this, true);
        llCalendarView = view.findViewById(R.id.llCalendarView);
        ImageView previousMonthButton = view.findViewById(R.id.leftButton);
        ImageView nextMonthButton = view.findViewById(R.id.rightButton);

        UtilsTheme.changeIcon(mContext, "nextMonth", 1,
                R.drawable.button_next_month_selector, nextMonthButton, dayOfWeekTextColor, dayOfWeekTextColor);

        UtilsTheme.changeIcon(mContext, "preMonth", 1,
                R.drawable.button_previous_month_selector, previousMonthButton, dayOfWeekTextColor, dayOfWeekTextColor);

        previousMonthButton.setOnClickListener(v -> preMonth());
        nextMonthButton.setOnClickListener(v -> nextMonth());

        // Initialize calendar for current month
        Calendar currentCalendar = Calendar.getInstance(Constant.LOCALE_DEFAULT);

        setFirstDayOfWeek(Calendar.MONDAY);
        refreshCalendar(currentCalendar);
    }

    private void preMonth() {
        currentMonthIndex--;
        currentCalendar = Calendar.getInstance(Constant.LOCALE_DEFAULT);
        currentCalendar.add(Calendar.MONTH, currentMonthIndex);

        refreshCalendar(currentCalendar);
        if (calendarListener != null)
            calendarListener.onMonthChanged(currentCalendar.getTime());
    }

    private void nextMonth() {
        currentMonthIndex++;
        currentCalendar = Calendar.getInstance(Constant.LOCALE_DEFAULT);
        currentCalendar.add(Calendar.MONTH, currentMonthIndex);
        refreshCalendar(currentCalendar);

        if (calendarListener != null)
            calendarListener.onMonthChanged(currentCalendar.getTime());
    }

    /**
     * Display calendar title with next previous month button
     */
    @SuppressLint("SetTextI18n")
    private void initializeTitleLayout() {
        View titleLayout = view.findViewById(R.id.titleLayout);
        titleLayout.setBackgroundColor(calendarTitleBackgroundColor);
        TextView tvDateTitle = titleLayout.findViewById(R.id.dateTitle);
        tvDateTitle.setTextColor(dayOfWeekTextColor);

        String dateText = new DateFormatSymbols(Constant.LOCALE_DEFAULT).getMonths()[currentCalendar.get(Calendar.MONTH)];

        dateTitle = view.findViewById(R.id.dateTitle);

        dateTitle.setText(currentCalendar.get(Calendar.DATE) + " " + dateText + " " + currentCalendar.get(Calendar.YEAR));
    }

    /**
     * Initialize the calendar week layout, considers start day
     */
    @SuppressLint("DefaultLocale")
    private void initializeWeekLayout() {
        TextView dayOfWeek;
        String dayOfTheWeekString;

        //Setting background color white
        View titleLayout = view.findViewById(R.id.weekLayout);
        titleLayout.setBackgroundColor(weekLayoutBackgroundColor);

        final String[] weekDaysArray = new DateFormatSymbols(Constant.LOCALE_DEFAULT).getWeekdays();
        for (int i = 1; i < weekDaysArray.length; i++) {
            dayOfTheWeekString = weekDaysArray[i];
            if (dayOfTheWeekString.length() > 1)
                dayOfTheWeekString = dayOfTheWeekString.substring(0, 1).toUpperCase();

            dayOfWeek = view.findViewWithTag(DAY_OF_WEEK + getWeekIndex(i, currentCalendar));
            dayOfWeek.setText(dayOfTheWeekString);
            dayOfWeek.setTextColor(titleDayOfWeekTextColor);
        }
    }

    private void setDaysInCalendar() {
        Calendar calendar = Calendar.getInstance(Constant.LOCALE_DEFAULT);
        calendar.setTime(currentCalendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.setFirstDayOfWeek(getFirstDayOfWeek());
        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);

        // Calculate dayOfMonthIndex
        int dayOfMonthIndex = getWeekIndex(firstDayOfMonth, calendar);
        int actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        final Calendar startCalendar = (Calendar) calendar.clone();
        //Add required number of days
        startCalendar.add(Calendar.DATE, -(dayOfMonthIndex - 1));
        int monthEndIndex = 42 - (actualMaximum + dayOfMonthIndex - 1);

        DayView dayView;
        ViewGroup dayOfMonthContainer;
        for (int i = 1; i < 43; i++) {
            dayOfMonthContainer = view.findViewWithTag(DAY_OF_MONTH_CONTAINER + i);
            dayView = view.findViewWithTag(DAY_OF_MONTH_TEXT + i);
            if (dayView == null) continue;

            //Apply the default styles
            dayOfMonthContainer.setOnClickListener(null);
            dayView.bind(startCalendar.getTime(), getDecorators());
            dayView.setVisibility(View.VISIBLE);

            if (CalendarUtils.isSameMonth(calendar, startCalendar)) {
                dayOfMonthContainer.setOnClickListener(onDayOfMonthClickListener);
                dayView.setBackgroundColor(calendarBackgroundColor);
                dayView.setTextColor(dayOfMonthTextColor);
                //Set the current day color
                markDayAsCurrentDay(startCalendar);
//                checkViewDiary(dayView);
            } else {
                dayView.setBackgroundColor(disabledDayBackgroundColor);
                dayView.setTextColor(disabledDayTextColor);

                if (!isOverflowDateVisible()) dayView.setVisibility(View.GONE);
                else if (i >= 36 && ((float) monthEndIndex / 7.0f) >= 1)
                    dayView.setVisibility(View.GONE);
            }
            dayView.decorate();

            startCalendar.add(Calendar.DATE, 1);
            dayOfMonthIndex++;
        }

        // If the last week row has no visible days, hide it or show it in case
        ViewGroup weekRow = view.findViewWithTag("weekRow6");
        dayView = view.findViewWithTag("dayOfMonthText36");
        if (dayView.getVisibility() != VISIBLE) weekRow.setVisibility(GONE);
        else weekRow.setVisibility(VISIBLE);
    }

    private void clearDayOfTheMonthStyle(Date currentDate) {
        if (currentDate != null) {
            final Calendar calendar = getTodaysCalendar();
            calendar.setFirstDayOfWeek(getFirstDayOfWeek());
            calendar.setTime(currentDate);

            final DayView dayView = getDayOfMonthText(calendar);
            String day = new SimpleDateFormat(Constant.FULL_DAY, Constant.LOCALE_DEFAULT).format(dayView.getDate().getTime());
            String dayCurrent = new SimpleDateFormat(Constant.FULL_DAY, Constant.LOCALE_DEFAULT).format(Calendar.getInstance(Constant.LOCALE_DEFAULT).getTime());
            if (day.equals(dayCurrent))
                dayView.setBackgroundResource(R.drawable.boder_current_day);
            else checkViewDiary(dayView);

            dayView.decorate();
        }
    }

    private void checkViewDiary(DayView dayView) {
        ArrayList<String> lstTimeStamp = DataLocalManager.getListTimeStamp(Constant.LIST_TIME_STAMP);

        String time = new SimpleDateFormat(Constant.FULL_DATE, Constant.LOCALE_DEFAULT).format(dayView.getDate().getTime());
        if (!lstTimeStamp.isEmpty()) {
            for (String timeStamp : lstTimeStamp) {
                if (timeStamp.equals(time)) {
                    dayView.setBackground(diaryDayBackgroundColor);
                    dayView.setTextColor(selectedDayTextColor);
                    break;
                } else {
                    dayView.setBackgroundColor(calendarBackgroundColor);
                    dayView.setTextColor(dayOfWeekTextColor);
                }
            }
        } else {
            dayView.setBackgroundColor(calendarBackgroundColor);
            dayView.setTextColor(dayOfWeekTextColor);
        }
    }

    private DayView getDayOfMonthText(Calendar currentCalendar) {
        return (DayView) getView(currentCalendar);
    }

    private int getDayIndexByDate(Calendar currentCalendar) {
        int monthOffset = getMonthOffset(currentCalendar);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        return currentDay + monthOffset;
    }

    private int getMonthOffset(Calendar currentCalendar) {
        final Calendar calendar = Calendar.getInstance(Constant.LOCALE_DEFAULT);
        calendar.setFirstDayOfWeek(getFirstDayOfWeek());
        calendar.setTime(currentCalendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayWeekPosition = calendar.getFirstDayOfWeek();
        int dayPosition = calendar.get(Calendar.DAY_OF_WEEK);

        if (firstDayWeekPosition == 1) return dayPosition - 1;
        else {
            if (dayPosition == 1) return 6;
            else return dayPosition - 2;
        }
    }

    private int getWeekIndex(int weekIndex, Calendar currentCalendar) {
        int firstDayWeekPosition = currentCalendar.getFirstDayOfWeek();
        if (firstDayWeekPosition == 1) return weekIndex;
        else {
            if (weekIndex == 1) return 7;
            else return weekIndex - 1;
        }
    }

    private View getView(Calendar currentCalendar) {
        int index = getDayIndexByDate(currentCalendar);
        return view.findViewWithTag(CustomCalendarView.DAY_OF_MONTH_TEXT + index);
    }

    private Calendar getTodaysCalendar() {
        Calendar currentCalendar = Calendar.getInstance(Constant.LOCALE_DEFAULT);
        currentCalendar.setFirstDayOfWeek(getFirstDayOfWeek());
        return currentCalendar;
    }

    @SuppressLint("DefaultLocale")
    public void refreshCalendar(Calendar currentCalendar) {
        this.currentCalendar = currentCalendar;
        this.currentCalendar.setFirstDayOfWeek(getFirstDayOfWeek());

        // Set date title
        initializeTitleLayout();

        // Set weeks days titles
        initializeWeekLayout();

        // Initialize and set days in calendar
        setDaysInCalendar();
    }

    public int getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    public void markDayAsCurrentDay(Calendar calendar) {
        if (calendar != null) {
            DayView dayView = getDayOfMonthText(calendar);
            String day = new SimpleDateFormat(Constant.FULL_DAY, Constant.LOCALE_DEFAULT).format(dayView.getDate().getTime());
            String dayCurrent = new SimpleDateFormat(Constant.FULL_DAY, Constant.LOCALE_DEFAULT).format(Calendar.getInstance(Constant.LOCALE_DEFAULT).getTime());
            if (day.equals(dayCurrent)) {
                dayView.setBackgroundResource(R.drawable.boder_current_day);
                markDayAsSelectedDay(Calendar.getInstance(Constant.LOCALE_DEFAULT).getTime());
            } else checkViewDiary(dayView);
        }
    }

    public void markDayAsSelectedDay(Date currentDate) {
        final Calendar currentCalendar = getTodaysCalendar();
        currentCalendar.setFirstDayOfWeek(getFirstDayOfWeek());
        currentCalendar.setTime(currentDate);

        // Clear previous marks
        clearDayOfTheMonthStyle(lastSelectedDay);

        // Store current values as last values
        storeLastValues(currentDate);

        // Mark current day as selected
        DayView daySelected = getDayOfMonthText(currentCalendar);
        daySelected.setBackground(selectedDayBackgroundColor);
        daySelected.setTextColor(currentDayOfMonthColor);

        dateTitle.setText(new SimpleDateFormat("dd MMMM yyyy", Constant.LOCALE_DEFAULT).format(currentDate.getTime()));
    }

    public void setSelectedDayOfMonth(Date dateSelected) {
        final Calendar calendar = Calendar.getInstance(Constant.LOCALE_DEFAULT);
        calendar.setFirstDayOfWeek(getFirstDayOfWeek());
        calendar.setTime(currentCalendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(new SimpleDateFormat("dd", Constant.LOCALE_DEFAULT).format(dateSelected)));
        markDayAsSelectedDay(calendar.getTime());
    }

    private void storeLastValues(Date currentDate) {
        lastSelectedDay = currentDate;
    }

    public void setCalendarListener(CalendarListener calendarListener) {
        this.calendarListener = calendarListener;
    }

    private final OnClickListener onDayOfMonthClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            // Extract day selected
            ViewGroup dayOfMonthContainer = (ViewGroup) view;
            String tagId = (String) dayOfMonthContainer.getTag();
            tagId = tagId.substring(DAY_OF_MONTH_CONTAINER.length());
            final TextView dayOfMonthText = view.findViewWithTag(DAY_OF_MONTH_TEXT + tagId);

            // Fire event
            final Calendar calendar = Calendar.getInstance(Constant.LOCALE_DEFAULT);
            calendar.setFirstDayOfWeek(getFirstDayOfWeek());
            calendar.setTime(currentCalendar.getTime());
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayOfMonthText.getText().toString()));

            //Set the current day color
            markDayAsCurrentDay(currentCalendar);
            markDayAsSelectedDay(calendar.getTime());

            if (calendarListener != null) calendarListener.onDateSelected(calendar.getTime());
        }
    };

    public List<DayDecorator> getDecorators() {
        return decorators;
    }

    public void setDecorators(List<DayDecorator> decorators) {
        this.decorators = decorators;
    }

    public boolean isOverflowDateVisible() {
        return isOverflowDateVisible;
    }

    public void setShowOverflowDate(boolean isOverFlowEnabled) {
        isOverflowDateVisible = isOverFlowEnabled;
    }

    public void setCustomTypeface(Typeface customTypeface) {
        this.customTypeface = customTypeface;
    }

    public Typeface getCustomTypeface() {
        return customTypeface;
    }

    public Calendar getCurrentCalendar() {
        return currentCalendar;
    }

    public Date getDayCurrent() {
        return Calendar.getInstance(Constant.LOCALE_DEFAULT).getTime();
    }

    public void setThemeBackgroundColor(int color, int colorTextTitleDayWeek, int colorText, Drawable selectedDayBackgroundColor, Drawable diaryDayBackgroundColor) {
        this.selectedDayBackgroundColor = selectedDayBackgroundColor;
        this.diaryDayBackgroundColor = diaryDayBackgroundColor;

        this.calendarTitleBackgroundColor = color;
        this.weekLayoutBackgroundColor = color;
        this.titleDayOfWeekTextColor = colorTextTitleDayWeek;
        this.dayOfWeekTextColor = colorText;
        this.calendarBackgroundColor = color;
        llCalendarView.setBackgroundColor(color);
        initializeCalendar();
    }
}
