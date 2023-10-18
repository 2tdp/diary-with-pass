package com.note.mydiary.diarywithlock.journalwithlock.model.theme;

public class ConfigPinThemeModel {

    private int typeTheme;
    private String colorTextTitle;
    private String colorText;
    private String colorDot;
    private String nameFont;
    private float sizeButton;
    private boolean isDot;
    private boolean isFont;
    private boolean hideText;

    public int getTypeTheme() {
        return typeTheme;
    }

    public String getColorTextTitle() {
        return colorTextTitle;
    }

    public String getColorText() {
        return colorText;
    }

    public String getColorDot() {
        return colorDot;
    }

    public String getNameFont() {
        return nameFont;
    }

    public float getSizeButton() {
        return sizeButton;
    }

    public boolean isDot() {
        return isDot;
    }

    public boolean isFont() {
        return isFont;
    }

    public boolean isHideText() {
        return hideText;
    }
}
