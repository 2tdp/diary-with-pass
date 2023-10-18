package com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel;

import com.note.mydiary.diarywithlock.journalwithlock.model.EmojiModel;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DiaryModel extends RealmObject {
    @PrimaryKey
    private int id;
    private String titleDiary;
    private EmojiModel emojiModel;
    private long dateTimeStamp;
    private RealmList<ContentModel> lstContent;
    private boolean isSelected;

    public DiaryModel() {
    }

    public DiaryModel(int id, String titleDiary, EmojiModel emojiModel, long dateTimeStamp,
                      RealmList<ContentModel> lstContent, boolean isSelected) {
        this.id = id;
        this.titleDiary = titleDiary;
        this.emojiModel = emojiModel;
        this.dateTimeStamp = dateTimeStamp;
        this.lstContent = lstContent;
        this.isSelected = isSelected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitleDiary() {
        return titleDiary;
    }

    public void setTitleDiary(String titleDiary) {
        this.titleDiary = titleDiary;
    }

    public EmojiModel getEmojiModel() {
        return emojiModel;
    }

    public void setEmojiModel(EmojiModel emojiModel) {
        this.emojiModel = emojiModel;
    }

    public long getDateTimeStamp() {
        return dateTimeStamp;
    }

    public void setDateTimeStamp(long dateTimeStamp) {
        this.dateTimeStamp = dateTimeStamp;
    }

    public RealmList<ContentModel> getLstContent() {
        return lstContent;
    }

    public void setLstContent(RealmList<ContentModel> lstContent) {
        this.lstContent = lstContent;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
