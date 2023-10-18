package com.note.mydiary.diarywithlock.journalwithlock.model;

import java.io.Serializable;

public class NotifyModel implements Serializable {

    private String title;
    private String time;
    private boolean isRun;

    public NotifyModel(String title, String time, boolean isRun) {
        this.title = title;
        this.time = time;
        this.isRun = isRun;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean run) {
        isRun = run;
    }
}
