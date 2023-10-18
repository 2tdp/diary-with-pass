package com.note.mydiary.diarywithlock.journalwithlock.utils;

import android.os.CountDownTimer;
import android.util.Log;

import com.note.mydiary.diarywithlock.journalwithlock.callback.IOnClickTime;

public class TimeCounter extends CountDownTimer {

    private int countUpTimer;
    private boolean isPause;
    IOnClickTime onclickView;

    public void setOnclickView(IOnClickTime onclickView) {
        this.onclickView = onclickView;
    }

    public TimeCounter(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        countUpTimer = 0;
    }

    public void pause() {
        isPause = true;
    }

    public void resume() {
        isPause = false;
    }

    @Override
    public void onTick(long l) {
        if (!isPause) countUpTimer = countUpTimer + 1;
        onclickView.onClick(countUpTimer);
        Log.d("2tdp", "onTick: " + countUpTimer);
    }

    @Override
    public void onFinish() {
        //reset counter to 0 if you want
        countUpTimer = 0;
    }

}