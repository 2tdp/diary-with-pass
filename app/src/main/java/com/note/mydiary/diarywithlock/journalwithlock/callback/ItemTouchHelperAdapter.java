package com.note.mydiary.diarywithlock.journalwithlock.callback;

public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}