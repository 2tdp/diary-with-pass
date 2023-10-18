package com.note.mydiary.diarywithlock.journalwithlock.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class EmojiModel extends RealmObject {
    @PrimaryKey
    private int id;
    private String nameEmoji;
    private String folder;
    private boolean isSelected;

    public EmojiModel() {
    }

    public EmojiModel(int id, String nameEmoji, String folder, boolean isSelected) {
        this.id = id;
        this.nameEmoji = nameEmoji;
        this.folder = folder;
        this.isSelected = isSelected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameEmoji() {
        return nameEmoji;
    }

    public void setNameEmoji(String nameEmoji) {
        this.nameEmoji = nameEmoji;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
