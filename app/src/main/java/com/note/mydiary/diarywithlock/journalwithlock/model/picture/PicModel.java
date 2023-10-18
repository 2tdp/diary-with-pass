package com.note.mydiary.diarywithlock.journalwithlock.model.picture;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PicModel extends RealmObject {

    @PrimaryKey
    private int id;
    private int idContent;
    private int idDiary;
    private String bucket;
    private float ratio;
    private String uri;
    private byte[] arrPic;
    private boolean isCheck;

    public PicModel() {
    }

    public PicModel(int id, int idContent, int idDiary, String bucket, float ratio, String uri,
                    byte[] arrPic, boolean isCheck) {
        this.id = id;
        this.idContent = idContent;
        this.idDiary = idDiary;
        this.bucket = bucket;
        this.ratio = ratio;
        this.uri = uri;
        this.arrPic = arrPic;
        this.isCheck = isCheck;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdContent() {
        return idContent;
    }

    public void setIdContent(int idContent) {
        this.idContent = idContent;
    }

    public int getIdDiary() {
        return idDiary;
    }

    public void setIdDiary(int idDiary) {
        this.idDiary = idDiary;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public byte[] getArrPic() {
        return arrPic;
    }

    public void setArrPic(byte[] arrPic) {
        this.arrPic = arrPic;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
