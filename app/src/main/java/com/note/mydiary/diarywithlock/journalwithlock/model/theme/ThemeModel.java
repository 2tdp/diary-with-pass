package com.note.mydiary.diarywithlock.journalwithlock.model.theme;

public class ThemeModel {

    private String name;
    private String url;
    private String preview;
    private String price;
    private String color;
    private boolean isOnl;
    private boolean isSelected;

    public ThemeModel(String name, String url, String preview, String price, boolean isOnl, boolean isSelected) {
        this.name = name;
        this.url = url;
        this.preview = preview;
        this.price = price;
        this.isOnl = isOnl;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isOnl() {
        return isOnl;
    }

    public void setOnl(boolean onl) {
        isOnl = onl;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
