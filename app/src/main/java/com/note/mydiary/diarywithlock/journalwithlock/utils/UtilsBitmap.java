package com.note.mydiary.diarywithlock.journalwithlock.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;

import com.note.mydiary.diarywithlock.journalwithlock.callback.ICallBackItem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UtilsBitmap {

    public static String toRGBString(int color) {
        // format: #RRGGBB
        String red = Integer.toHexString(Color.red(color));
        String green = Integer.toHexString(Color.green(color));
        String blue = Integer.toHexString(Color.blue(color));
        if (red.length() == 1)
            red = "0" + red;
        if (green.length() == 1)
            green = "0" + green;
        if (blue.length() == 1)
            blue = "0" + blue;
        return "#" + red + green + blue;
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int width, int height) {
        Bitmap img = Bitmap.createScaledBitmap(bitmap, width, height, true);
        bitmap.recycle();
        return img;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int cornerSizePx) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        paint.setColor(0xFFFFFFFF);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);

        // draw bitmap
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        Bitmap bitmap = null;
        if (drawable != null)
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicWidth(), Bitmap.Config.ARGB_8888);
        if (bitmap != null) {
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }
        return null;
    }

    public static Bitmap loadBitmapFromView(Context context, View view) {
        Bitmap b;
        b = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());

        Canvas c = new Canvas(b);

        c.rotate(view.getRotation(), (float) view.getWidth() / 2, (float) view.getHeight() / 2);
        view.draw(c);
        return b;
    }

    public static float[] getImageSize(Context context, Uri uri) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            InputStream input = context.getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(input, null, options);
            input.close();
            return new float[]{options.outWidth, options.outHeight};
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return new float[]{0, 0};
    }

    public static void createByteImage(Context context, String uriPic, ICallBackItem callBack) {
        Bitmap bmp;
        try {
            bmp = modifyOrientation(context, getBitmapFromUri(context, Uri.parse(uriPic)), Uri.parse(uriPic));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 84, stream);
            callBack.callBackItem(stream.toByteArray(), -1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createByteAudio(String uriAudio, ICallBackItem callBack) {
        try {
            InputStream inputStream = new FileInputStream(uriAudio);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buffer = new byte[0xFFFF];
            for (int len = inputStream.read(buffer); len != -1; len = inputStream.read(buffer)) {
                os.write(buffer, 0, len);
            }

            callBack.callBackItem(os.toByteArray(), -1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String saveBitmapToApp(Context context, Bitmap bitmap, String nameFolder, String nameFile) {
        String directory = Utils.getStore(context) + "/" + nameFolder + "/";
        File myPath = new File(directory, nameFile + ".png");

        try {
            FileOutputStream fos = new FileOutputStream(myPath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return myPath.getPath();
        } catch (Exception e) {
            Log.e("SAVE_IMAGE", e.getMessage(), e);
        }
        return "";
    }

    public static Bitmap getBitmapFromUri(Context context, Uri selectedFileUri) {
        Bitmap image = null;
        try {
            ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static Bitmap getBitmapFromAsset(Context context, String nameFolder, String name) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(nameFolder + "/" + name);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
            e.printStackTrace();
        }

        return bitmap;
    }

    public static Bitmap modifyOrientation(Context context, Bitmap bitmap, Uri uri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(uri);
        ExifInterface ei = new ExifInterface(is);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateBitmap(bitmap, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateBitmap(bitmap, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateBitmap(bitmap, 270);
//            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
//                ivFilter.setRotation(180);
//                break;
//            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
//                return flip(bitmap, false, true);
            default:
                return bitmap;
        }
    }

    public static Bitmap modifyOrientation(Bitmap bitmap, FileDescriptor fileDescriptor) throws IOException {
        ExifInterface ei = new ExifInterface(fileDescriptor);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateBitmap(bitmap, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateBitmap(bitmap, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateBitmap(bitmap, 270);
//            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
//                ivFilter.setRotation(180);
//                break;
//            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
//                return flip(bitmap, false, true);
            default:
                return bitmap;
        }
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
