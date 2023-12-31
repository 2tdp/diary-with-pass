package com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.patternview.customView;

import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.note.mydiary.diarywithlock.journalwithlock.activity.addview.viewnavigation.viewpasscode.patternview.bean.CellBean;

import java.util.List;

/**
 */

public interface ILockerLinkedLineView {
    /**
     * 绘制图案密码连接线
     *
     * @param canvas
     * @param hitList
     * @param cellBeanList
     * @param endX
     * @param endY
     * @param isError
     */
    void draw(@NonNull Canvas canvas,
              @Nullable List<Integer> hitList,
              @NonNull List<CellBean> cellBeanList,
              float endX,
              float endY,
              boolean isError);
}