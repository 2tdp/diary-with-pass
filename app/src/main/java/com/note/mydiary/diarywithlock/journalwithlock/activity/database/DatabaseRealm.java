package com.note.mydiary.diarywithlock.journalwithlock.activity.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.note.mydiary.diarywithlock.journalwithlock.callback.ICallBackItem;
import com.note.mydiary.diarywithlock.journalwithlock.callback.ICheckTouch;
import com.note.mydiary.diarywithlock.journalwithlock.model.EmojiModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.AudioModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.ContentModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.DiaryModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.picture.PicModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsBitmap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

public class DatabaseRealm {

    static String TAG = "2tdp";

    public static void getAll(Sort sort, ICallBackItem callBack) {
        if (Realm.getDefaultConfiguration() != null) {
            Realm.getInstanceAsync(Realm.getDefaultConfiguration(), new Realm.Callback() {
                @Override
                public void onSuccess(@NonNull Realm realm) {
                    callBack.callBackItem(realm.copyFromRealm(realm.where(DiaryModel.class).sort("dateTimeStamp", sort).findAll()), -1);
                    Log.d(TAG, "onSuccess: get All Done");
                }

                @Override
                public void onError(@NonNull Throwable exception) {
                    super.onError(exception);
                    Log.d(TAG, "onError: " + exception.getMessage());
                }
            });
        }
    }

    public static void getAllPicInDiary(int id, ICallBackItem callBack) {
        if (Realm.getDefaultConfiguration() != null) {
            Realm.getInstanceAsync(Realm.getDefaultConfiguration(), new Realm.Callback() {
                @Override
                public void onSuccess(@NonNull Realm realm) {
                    callBack.callBackItem(realm.copyFromRealm(realm.where(PicModel.class).equalTo("idDiary", id).findAll()), -1);
                    Log.d(TAG, "onSuccess: get All Pic Done");
                }

                @Override
                public void onError(@NonNull Throwable exception) {
                    super.onError(exception);
                    Log.d(TAG, "onError: " + exception.getMessage());
                }
            });
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static void getAllDiaryInMonth(Sort sort, long time, ICallBackItem callBack) {
        if (Realm.getDefaultConfiguration() != null) {
            Realm.getInstanceAsync(Realm.getDefaultConfiguration(), new Realm.Callback() {
                @Override
                public void onSuccess(@NonNull Realm realm) {
                    long[] timeMonth = getTimeInMonth(time);

                    callBack.callBackItem(realm.copyFromRealm(realm.where(DiaryModel.class)
                            .between("dateTimeStamp", timeMonth[0], timeMonth[1])
                            .sort("dateTimeStamp", sort)
                            .findAll()), -1);
                    Log.d(TAG, "onSuccess: get All Pic Done");
                }

                @Override
                public void onError(@NonNull Throwable exception) {
                    super.onError(exception);
                    Log.d(TAG, "onError: " + exception.getMessage());
                }
            });
        }
    }

    public static void getAllDiaryInDay(long time, ICallBackItem callBack) {
        if (Realm.getDefaultConfiguration() != null) {
            Realm.getInstanceAsync(Realm.getDefaultConfiguration(), new Realm.Callback() {
                @Override
                public void onSuccess(@NonNull Realm realm) {
                    long[] timeInDay = getTimeInDay(time);

                    callBack.callBackItem(realm.copyFromRealm(realm.where(DiaryModel.class)
                            .between("dateTimeStamp", timeInDay[0], timeInDay[1])
                            .findAll()), -1);
                    Log.d(TAG, "onSuccess: get All Pic Done");
                }

                @Override
                public void onError(@NonNull Throwable exception) {
                    super.onError(exception);
                    Log.d(TAG, "onError: " + exception.getMessage());
                }
            });
        }
    }

    public static void getOtherDiary(int idDiary, ICallBackItem callBack) {
        if (Realm.getDefaultConfiguration() != null) {
            Realm.getInstanceAsync(Realm.getDefaultConfiguration(), new Realm.Callback() {
                @Override
                public void onSuccess(Realm realm) {
                    DiaryModel diaryModel = realm.where(DiaryModel.class).equalTo("id", idDiary).findFirst();

                    if (diaryModel != null)
                        callBack.callBackItem(realm.copyFromRealm(diaryModel), -1);
                    Log.d(TAG, "onSuccess: get All Pic Done");
                }

                @Override
                public void onError(Throwable exception) {
                    super.onError(exception);
                    Log.d(TAG, "onError: " + exception.getMessage());
                }
            });
        }
    }

    public static void searchOtherDiary(String key, ICallBackItem callBack) {
        if (Realm.getDefaultConfiguration() != null) {
            Realm.getInstanceAsync(Realm.getDefaultConfiguration(), new Realm.Callback() {
                @Override
                public void onSuccess(@NonNull Realm realm) {
                    callBack.callBackItem(realm.copyFromRealm(realm.where(DiaryModel.class).contains("titleDiary", key).findAll()), -1);
                    Log.d(TAG, "onSuccess: get All Pic Done");
                }

                @Override
                public void onError(@NonNull Throwable exception) {
                    super.onError(exception);
                    Log.d(TAG, "onError: " + exception.getMessage());
                }
            });
        }
    }

    public static void insert(Context context, DiaryModel diaryModel, ICheckTouch isDone) {
        if (Realm.getDefaultConfiguration() != null) {
            Realm.getInstanceAsync(Realm.getDefaultConfiguration(), new Realm.Callback() {
                @SuppressLint("SimpleDateFormat")
                @Override
                public void onSuccess(@NonNull Realm realm) {
                    Log.d(TAG, "Successfully opened a realm with reads and writes allowed on the UI thread.");
                    realm.beginTransaction();

                    Number maxKeyDiary = realm.where(DiaryModel.class).max("id");
                    int newKeyDiary = (maxKeyDiary == null) ? 1 : maxKeyDiary.intValue() + 1;

                    EmojiModel emojiRealm = realm.createObject(EmojiModel.class, newKeyDiary);
                    emojiRealm.setNameEmoji(diaryModel.getEmojiModel().getNameEmoji());
                    emojiRealm.setFolder(diaryModel.getEmojiModel().getFolder());
                    emojiRealm.setSelected(diaryModel.getEmojiModel().isSelected());

                    RealmList<ContentModel> lstContentReal = new RealmList<>();
                    if (!diaryModel.getLstContent().isEmpty()) {
                        for (ContentModel content : diaryModel.getLstContent()) {
                            Number maxKeyContent = realm.where(ContentModel.class).max("id");
                            int newKeyContent = (maxKeyContent == null) ? 1 : maxKeyContent.intValue() + 1;

                            ContentModel contentRealm = realm.createObject(ContentModel.class, newKeyContent);
                            contentRealm.setIdDiary(newKeyDiary);
                            contentRealm.setContent(content.getContent());
                            if (content.getAudio() != null)
                                if (content.getAudio().getUriAudio() != null)
                                    if (!content.getAudio().getUriAudio().equals("")) {
                                        Number maxKeyAudio = realm.where(AudioModel.class).max("id");
                                        int newKeyAudio = (maxKeyAudio == null) ? 1 : maxKeyAudio.intValue() + 1;
                                        AudioModel audioRealm = realm.createObject(AudioModel.class, newKeyAudio);
                                        audioRealm.setIdDiary(newKeyDiary);
                                        audioRealm.setUriAudio(content.getAudio().getUriAudio());
                                        UtilsBitmap.createByteAudio(content.getAudio().getUriAudio(), (o, pos) -> audioRealm.setArrAudio((byte[]) o));

                                        contentRealm.setAudio(audioRealm);
                                    }

                            RealmList<PicModel> lstPic = new RealmList<>();
                            if (!content.getLstUriPic().isEmpty()) {
                                for (PicModel pic : content.getLstUriPic()) {
                                    Number maxKeyListPic = realm.where(PicModel.class).max("id");
                                    int newKeyListPic = (maxKeyListPic == null) ? 1 : maxKeyListPic.intValue() + 1;

                                    PicModel picRealm = realm.createObject(PicModel.class, newKeyListPic);
                                    picRealm.setIdContent(newKeyContent);
                                    picRealm.setIdDiary(newKeyDiary);
                                    picRealm.setBucket(pic.getBucket());
                                    picRealm.setRatio(pic.getRatio());
                                    picRealm.setCheck(pic.isCheck());

                                    if (pic.getUri() != null)
                                        UtilsBitmap.createByteImage(context, pic.getUri(), (o, pos) -> {
                                            picRealm.setArrPic((byte[]) o);

                                            lstPic.add(picRealm);
                                            contentRealm.setLstUriPic(lstPic);
                                        });
                                }
                            }

                            lstContentReal.add(contentRealm);
                        }
                    }

                    DiaryModel diaryRealm = realm.createObject(DiaryModel.class, newKeyDiary);
                    diaryRealm.setTitleDiary(diaryModel.getTitleDiary());
                    diaryRealm.setEmojiModel(emojiRealm);
                    diaryRealm.setDateTimeStamp(diaryModel.getDateTimeStamp());
                    diaryRealm.setLstContent(lstContentReal);

                    realm.insert(diaryRealm);
                    realm.commitTransaction();
                    isDone.checkTouch(true);
                    DataLocalManager.setInt(diaryRealm.getId(), Constant.ID_DIARY_WIDGET);
                    addTimeStamp(diaryModel.getDateTimeStamp());
                    Log.d(TAG, "onSuccess: upLoad Done");
                }

                @Override
                public void onError(@NonNull Throwable exception) {
                    Log.d(TAG, "onError: " + exception.getMessage());
                }
            });
        }
    }

    public static void update(Context context, DiaryModel diaryModel, ICheckTouch isDone) {
        if (Realm.getDefaultConfiguration() != null) {
            Realm.getInstanceAsync(Realm.getDefaultConfiguration(), new Realm.Callback() {
                @SuppressLint("SimpleDateFormat")
                @Override
                public void onSuccess(@NonNull Realm realm) {
                    Log.d(TAG, "Successfully opened a realm with reads and writes allowed on the UI thread.");
                    int idDiary = diaryModel.getId();
                    DiaryModel diaryRealm = realm.where(DiaryModel.class).equalTo("id", idDiary).findFirst();
                    EmojiModel emojiRealm = realm.where(EmojiModel.class).equalTo("id", idDiary).findFirst();
                    RealmResults<PicModel> picRealms = realm.where(PicModel.class).equalTo("idDiary", idDiary).findAll();
                    RealmResults<ContentModel> contentRealms = realm.where(ContentModel.class).equalTo("idDiary", idDiary).findAll();

                    realm.beginTransaction();
                    if (diaryRealm != null) {
                        diaryRealm.setTitleDiary(diaryModel.getTitleDiary());
                        long[] timeInDay = getTimeInDay(diaryRealm.getDateTimeStamp());

                        List<DiaryModel> lstDiary = realm.copyFromRealm(realm.where(DiaryModel.class).between("dateTimeStamp", timeInDay[0], timeInDay[1]).findAll());
                        removeTimeStamp(diaryRealm.getDateTimeStamp(), lstDiary.size());

                        diaryRealm.setDateTimeStamp(diaryModel.getDateTimeStamp());
                    }
                    if (emojiRealm != null) {
                        emojiRealm.setFolder(diaryModel.getEmojiModel().getFolder());
                        emojiRealm.setNameEmoji(diaryModel.getEmojiModel().getNameEmoji());
                        diaryRealm.setEmojiModel(emojiRealm);
                    }

                    picRealms.deleteAllFromRealm();
                    contentRealms.deleteAllFromRealm();

                    RealmList<ContentModel> lstContentRealm = new RealmList<>();
                    if (!diaryModel.getLstContent().isEmpty()) {
                        for (ContentModel content : diaryModel.getLstContent()) {
                            Number maxKeyContent = realm.where(ContentModel.class).max("id");
                            int newKeyContent = (maxKeyContent == null) ? 1 : maxKeyContent.intValue() + 1;

                            ContentModel contentRealm = realm.createObject(ContentModel.class, newKeyContent);
                            contentRealm.setIdDiary(idDiary);
                            contentRealm.setContent(content.getContent());
                            if (content.getAudio() != null)
                                if (content.getAudio().getUriAudio() != null)
                                    if (!content.getAudio().getUriAudio().equals("")) {
                                        Number maxKeyAudio = realm.where(AudioModel.class).max("id");
                                        int newKeyAudio = (maxKeyAudio == null) ? 1 : maxKeyAudio.intValue() + 1;
                                        AudioModel audioRealm = realm.createObject(AudioModel.class, newKeyAudio);
                                        audioRealm.setIdDiary(idDiary);
                                        audioRealm.setUriAudio(content.getAudio().getUriAudio());
                                        UtilsBitmap.createByteAudio(content.getAudio().getUriAudio(), (o, pos) -> audioRealm.setArrAudio((byte[]) o));

                                        contentRealm.setAudio(audioRealm);
                                    }

                            RealmList<PicModel> lstPic = new RealmList<>();
                            if (!content.getLstUriPic().isEmpty()) {
                                for (PicModel pic : content.getLstUriPic()) {
                                    Number maxKeyListPic = realm.where(PicModel.class).max("id");
                                    int newKeyListPic = (maxKeyListPic == null) ? 1 : maxKeyListPic.intValue() + 1;

                                    PicModel picRealm = realm.createObject(PicModel.class, newKeyListPic);
                                    picRealm.setIdContent(newKeyContent);
                                    picRealm.setIdDiary(idDiary);
                                    picRealm.setBucket(pic.getBucket());
                                    picRealm.setRatio(pic.getRatio());
                                    picRealm.setCheck(pic.isCheck());
                                    if (pic.getUri() != null)
                                        UtilsBitmap.createByteImage(context, pic.getUri(), (o, pos) -> {
                                            picRealm.setArrPic((byte[]) o);

                                            lstPic.add(picRealm);
                                            contentRealm.setLstUriPic(lstPic);
                                        });
                                    else {
                                        picRealm.setArrPic(pic.getArrPic());

                                        lstPic.add(picRealm);

                                        contentRealm.setLstUriPic(lstPic);
                                    }
                                }
                            }

                            lstContentRealm.add(contentRealm);
                        }
                    }

                    if (diaryRealm != null)
                        diaryRealm.setLstContent(lstContentRealm);

                    realm.commitTransaction();
                    isDone.checkTouch(true);

                    addTimeStamp(diaryModel.getDateTimeStamp());
                    Log.d(TAG, "onSuccess: upLoad Done");
                }

                @Override
                public void onError(@NonNull Throwable exception) {
                    Log.d(TAG, "onError: " + exception.getMessage());
                }
            });
        }
    }

    public static void delOtherDiary(int idDiary, ICheckTouch isDone, ICheckTouch resetCalendar) {
        if (Realm.getDefaultConfiguration() != null) {
            Realm.getInstanceAsync(Realm.getDefaultConfiguration(), new Realm.Callback() {
                @Override
                public void onSuccess(@NonNull Realm realm) {
                    DiaryModel diaryModel = realm.where(DiaryModel.class).equalTo("id", idDiary).findFirst();
                    EmojiModel emojiModel = realm.where(EmojiModel.class).equalTo("id", idDiary).findFirst();

                    RealmResults<PicModel> picModels = realm.where(PicModel.class).equalTo("idDiary", idDiary).findAll();
                    RealmResults<ContentModel> contentModels = realm.where(ContentModel.class).equalTo("idDiary", idDiary).findAll();

                    realm.beginTransaction();
                    picModels.deleteAllFromRealm();
                    contentModels.deleteAllFromRealm();
                    if (emojiModel != null) emojiModel.deleteFromRealm();
                    if (diaryModel != null) {
                        long[] timeInDay = getTimeInDay(diaryModel.getDateTimeStamp());

                        List<DiaryModel> lstDiary = realm.copyFromRealm(realm.where(DiaryModel.class).between("dateTimeStamp", timeInDay[0], timeInDay[1]).findAll());
                        removeTimeStamp(diaryModel.getDateTimeStamp(), lstDiary.size());
                        resetCalendar.checkTouch(true);

                        diaryModel.deleteFromRealm();
                    }

                    realm.commitTransaction();
                    isDone.checkTouch(true);
                    Log.d(TAG, "onSuccess: get All Pic Done");
                }

                @Override
                public void onError(Throwable exception) {
                    super.onError(exception);
                    Log.d(TAG, "onError: " + exception.getMessage());
                }
            });
        }
    }

    public static void addTimeStamp(long timeStamp) {
        int check = 0;
        String time = new SimpleDateFormat(Constant.FULL_DATE, Constant.LOCALE_DEFAULT).format(timeStamp);
        ArrayList<String> lstTimeStamp = DataLocalManager.getListTimeStamp(Constant.LIST_TIME_STAMP);
        for (int i = 0; i < lstTimeStamp.size(); i++) {
            if (lstTimeStamp.get(i).equals(time)) {
                check++;
                break;
            }
        }
        if (check == 0) lstTimeStamp.add(time);
        DataLocalManager.setListTimeStamp(Constant.LIST_TIME_STAMP, lstTimeStamp);
    }

    public static void removeTimeStamp(long timeStamp, int total) {
        int check = -1;
        String time = new SimpleDateFormat(Constant.FULL_DATE, Constant.LOCALE_DEFAULT).format(timeStamp);
        ArrayList<String> lstTimeStamp = DataLocalManager.getListTimeStamp(Constant.LIST_TIME_STAMP);
        for (int i = 0; i < lstTimeStamp.size(); i++) {
            if (lstTimeStamp.get(i).equals(time) && total == 1) {
                check = i;
                break;
            }
        }
        if (check != -1) lstTimeStamp.remove(check);

        DataLocalManager.setListTimeStamp(Constant.LIST_TIME_STAMP, lstTimeStamp);
    }

    @SuppressLint("SimpleDateFormat")
    public static long[] getTimeInMonth(long time) {
        int month = Integer.parseInt(new SimpleDateFormat("MM", Constant.LOCALE_DEFAULT).format(time)) + 1;
        Calendar calendar = Calendar.getInstance(Constant.LOCALE_DEFAULT);
        calendar.set(Calendar.MONTH, month);
        int timeStart = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        int timeLast = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        SimpleDateFormat formatToDate = new SimpleDateFormat("HH:mm " + Constant.FULL_DATE, Constant.LOCALE_DEFAULT);

        SimpleDateFormat format = new SimpleDateFormat("MM/yyyy", Constant.LOCALE_DEFAULT);
        String timeBegin = "00:00 " + timeStart + "/" + format.format(time);
        String timeEnd = "23:59 " + timeLast + "/" + format.format(time);

        try {
            return new long[]{formatToDate.parse(timeBegin).getTime(), formatToDate.parse(timeEnd).getTime()};
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new long[]{0, 0};
    }

    @SuppressLint("SimpleDateFormat")
    public static long[] getTimeInDay(long time) {
        SimpleDateFormat formatToDate = new SimpleDateFormat("HH:mm " + Constant.FULL_DATE, Constant.LOCALE_DEFAULT);

        SimpleDateFormat format = new SimpleDateFormat(Constant.FULL_DATE, Constant.LOCALE_DEFAULT);
        String timeBegin = "00:00 " + format.format(time);
        String timeEnd = "23:59 " + format.format(time);

        try {
            return new long[]{formatToDate.parse(timeBegin).getTime(), formatToDate.parse(timeEnd).getTime()};
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new long[]{0, 0};
    }
}
