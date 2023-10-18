package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.patternview.customView;

import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.patternview.bean.CellBean;

import java.util.List;

/**
 */

public interface IIndicatorLinkedLineView {
    /**
     * 绘制指示器连接线
     *
     * @param canvas
     * @param hitList
     * @param cellBeanList
     * @param isError
     */
    void draw(@NonNull Canvas canvas,
              @Nullable List<Integer> hitList,
              @NonNull List<CellBean> cellBeanList,
              boolean isError);
}