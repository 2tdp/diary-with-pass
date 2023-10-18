package com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AudioModel extends RealmObject {
    @PrimaryKey
    private int id;
    private int idDiary;
    private String uriAudio;
    private byte[] arrAudio;
    private boolean isPlay;

    public AudioModel() {
    }

    public AudioModel(int id, int idDiary, String uriAudio, byte[] arrAudio) {
        this.id = id;
        this.idDiary = idDiary;
        this.uriAudio = uriAudio;
        this.arrAudio = arrAudio;
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

    public String getUriAudio() {
        return uriAudio;
    }

    public void setUriAudio(String uriAudio) {
        this.uriAudio = uriAudio;
    }

    public byte[] getArrAudio() {
        return arrAudio;
    }

    public void setArrAudio(byte[] arrAudio) {
        this.arrAudio = arrAudio;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }
}
