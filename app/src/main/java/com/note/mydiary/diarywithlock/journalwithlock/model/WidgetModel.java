package com.note.mydiary.diarywithlock.journalwithlock.model;

import java.io.Serializable;

public class WidgetModel implements Serializable {
    private String nameWidget;
    private int idWidget;

    public WidgetModel(String nameWidget, int idWidget) {
        this.nameWidget = nameWidget;
        this.idWidget = idWidget;
    }

    public String getNameWidget() {
        return nameWidget;
    }

    public void setNameWidget(String nameWidget) {
        this.nameWidget = nameWidget;
    }

    public int getIdWidget() {
        return idWidget;
    }

    public void setIdWidget(int idWidget) {
        this.idWidget = idWidget;
    }
}
