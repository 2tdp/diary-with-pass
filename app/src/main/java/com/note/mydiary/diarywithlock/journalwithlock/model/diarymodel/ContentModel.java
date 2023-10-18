package com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel;

import com.note.mydiary.diarywithlock.journalwithlock.model.picture.PicModel;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ContentModel extends RealmObject {
    @PrimaryKey
    private int id;
    private int idDiary;
    private String content;
    private AudioModel audio;
    private RealmList<PicModel> lstUriPic;

    public ContentModel() {
    }

    public ContentModel(int id, int idDiary, String content, AudioModel audio, RealmList<PicModel> lstUriPic) {
        this.id = id;
        this.idDiary = idDiary;
        this.content = content;
        this.audio = audio;
        this.lstUriPic = lstUriPic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdDiary() {
        return idDiary;
    }

    public void setIdDiary(int idDiary) {
        this.idDiary = idDiary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AudioModel getAudio() {
        return audio;
    }

    public void setAudio(AudioModel audio) {
        this.audio = audio;
    }

    public RealmList<PicModel> getLstUriPic() {
        return lstUriPic;
    }

    public void setLstUriPic(RealmList<PicModel> lstUriPic) {
        this.lstUriPic = lstUriPic;
    }
}
