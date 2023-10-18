package com.note.mydiary.diarywithlock.journalwithlock.data;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.model.picture.BucketPicModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.picture.PicModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsBitmap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DataPic {

    private static final String[] EXTERNAL_COLUMNS_PIC = new String[]{
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            "\"" + MediaStore.Images.Media.EXTERNAL_CONTENT_URI + "\""
    };

    private static final String[] EXTERNAL_COLUMNS_PIC_API_Q = new String[]{
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA
    };

    public static void getBucketPictureList(Context context) {

        ArrayList<BucketPicModel> lstBucket = new ArrayList<>();
        ArrayList<PicModel> lstPic, lstAll = new ArrayList<>();
        Uri CONTENT_URI;
        String[] COLUMNS;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            CONTENT_URI = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
            COLUMNS = EXTERNAL_COLUMNS_PIC_API_Q;
        } else {
            CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            COLUMNS = EXTERNAL_COLUMNS_PIC;
        }

        try (Cursor cursor = context.getContentResolver().query(
                CONTENT_URI,
                COLUMNS,
                null,
                null,
                MediaStore.Images.Media.DEFAULT_SORT_ORDER)) {
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int bucketColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            while (cursor.moveToNext()) {
                int id = cursor.getInt(idColumn);
                String bucket = cursor.getString(bucketColumn);
                String data = cursor.getString(dataColumn);

                Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                float[] size = UtilsBitmap.getImageSize(context, Uri.parse(contentUri.toString()));
                float ratio = size[0] / size[1];
                File file = new File(data);

                if (file.canRead()) {
                    lstAll.add(new PicModel(id, -1, -1, bucket, ratio, contentUri.toString(), new byte[]{}, false));
                    boolean check = false;
                    if (lstBucket.isEmpty()) {
                        lstPic = new ArrayList<>();
                        if (bucket != null) {
                            lstBucket.add(new BucketPicModel(lstPic, bucket));
                            lstPic.add(new PicModel(id, -1, -1, bucket, ratio, contentUri.toString(), new byte[]{}, false));
                        } else {
                            lstBucket.add(new BucketPicModel(lstPic, ""));
                            lstPic.add(new PicModel(id, -1, -1, "", ratio, contentUri.toString(), new byte[]{}, false));
                        }
                    } else {
                        for (int i = 0; i < lstBucket.size(); i++) {
                            if (bucket == null) {
                                lstBucket.get(i).getLstPic().add(new PicModel(id, -1, -1, "", ratio, contentUri.toString(), new byte[]{}, false));
                                check = true;
                                break;
                            }
                            if (bucket.equals(lstBucket.get(i).getBucket())) {
                                lstBucket.get(i).getLstPic().add(new PicModel(id, -1, -1, bucket, ratio, contentUri.toString(), new byte[]{}, false));
                                check = true;
                                break;
                            }
                        }
                        if (!check) {
                            lstPic = new ArrayList<>();
                            lstPic.add(new PicModel(id, -1, -1, bucket, ratio, contentUri.toString(), new byte[]{}, false));
                            lstBucket.add(new BucketPicModel(lstPic, bucket));
                        }
                    }
                }
            }

            lstBucket.add(0, new BucketPicModel(lstAll, context.getString(R.string.all)));
        }
        DataLocalManager.setListBucket(lstBucket, Constant.LIST_ALL_PIC);
    }

//    public static ArrayList<PicModel> getAllPictureList(Context context) {
//
//        ArrayList<PicModel> lstPic = new ArrayList<>();
//        Uri CONTENT_URI;
//        String[] COLUMNS;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            CONTENT_URI = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
//            COLUMNS = EXTERNAL_COLUMNS_PIC_API_Q;
//        } else {
//            CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//            COLUMNS = EXTERNAL_COLUMNS_PIC;
//        }
//
//        try (Cursor cursor = context.getContentResolver().query(
//                CONTENT_URI,
//                COLUMNS,
//                null,
//                null,
//                MediaStore.Images.Media.DEFAULT_SORT_ORDER)) {
//            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
//            int bucketColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
//            int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//
//            while (cursor.moveToNext()) {
//                String id = cursor.getString(idColumn);
//                String bucket = cursor.getString(bucketColumn);
//                String data = cursor.getString(dataColumn);
//
//                Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Long.parseLong(id));
//
//                File file = new File(data);
//                if (file.canRead())
//                    lstPic.add(new PicModel(id, bucket, contentUri.toString(), false));
//            }
//        }
//        return lstPic;
//    }

    public static ArrayList<String> getPicAssets(Context context, String name) {
        ArrayList<String> lstPicAsset = new ArrayList<>();
        try {
            String[] f = context.getAssets().list(name);
            lstPicAsset.addAll(Arrays.asList(f));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lstPicAsset;
    }
}
