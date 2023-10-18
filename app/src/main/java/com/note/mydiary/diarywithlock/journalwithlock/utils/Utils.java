package com.note.mydiary.diarywithlock.journalwithlock.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.IntRange;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.note.mydiary.diarywithlock.journalwithlock.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

public class Utils {

    public static final int res = android.R.id.content;
    public static final int enter = R.anim.slide_in_right;
    public static final int exit = R.anim.slide_out_left;
    public static final int popEnter = R.anim.slide_in_left_small;
    public static final int popExit = R.anim.slide_out_right;

    public static void setStatusBarTransparent(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);

        int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            flags = flags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            flags = flags | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;

        decorView.setSystemUiVisibility(flags);
    }

    public static void hideKeyboard(Context context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }

    public static void showSoftKeyboard(Context context, View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void replaceFragment(final FragmentManager manager, final Fragment fragment, boolean isAdd,
                                       final boolean addBackStack, boolean isAnimation) {
        try {
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            if (isAnimation)
                fragmentTransaction.setCustomAnimations(enter, exit, popEnter, popExit);
            if (addBackStack)
                fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
            if (isAdd) {
                fragmentTransaction.add(res, fragment, fragment.getClass().getSimpleName());
            } else {
                fragmentTransaction.replace(res, fragment, fragment.getClass().getSimpleName());
            }
            fragmentTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static GradientDrawable createBackground(int[] colors, int border, int stroke, int colorStroke) {
        Log.d(Constant.TAG, "createBackground: " + colors.length);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(border);
        if (stroke != -1) drawable.setStroke(stroke, colorStroke);
        if (colors[0] != -1) {
            if (colors.length >= 2) {
                drawable.setColors(colors);
                drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            } else drawable.setColor(colors[0]);
        }

        return drawable;
    }

    public static ColorStateList createColorState(int colorOn, int colorOff) {
        int[][] states = new int[][]{
//                new int[] { android.R.attr.state_enabled}, // enabled
//                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{android.R.attr.state_checked}  // pressed
        };

        int[] colors = new int[]{colorOn, colorOff};

        return new ColorStateList(states, colors);
    }

    @IntRange(from = 0, to = 3)
    public static int getConnectionType(Context context) {
        int result = 0; // Returns connection type. 0: none; 1: mobile data; 2: wifi
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (cm != null) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                        result = 2;
                    else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                        result = 1;
                    else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN))
                        result = 3;
                }
            }
        } else {
            if (cm != null) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                        result = 2;
                    else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                        result = 1;
                    else if (activeNetwork.getType() == ConnectivityManager.TYPE_VPN)
                        result = 3;
                }
            }
        }
        return result;
    }

    public static void effectVibrate(Context context) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        else v.vibrate(500);
    }

    public static TypedValue effectPressRectangle(Context context) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);

        return outValue;
    }

    public static TypedValue effectPressOval(Context context) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, outValue, true);

        return outValue;
    }

    public static void setIntent(Context context, String nameActivity) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(context, nameActivity));
        context.startActivity(intent, ActivityOptions.makeCustomAnimation(context, R.anim.slide_in_right, R.anim.slide_out_left).toBundle());
    }

    public static void setAnimExit(Activity activity) {
        activity.overridePendingTransition(R.anim.slide_in_left_small, R.anim.slide_out_right);
    }

    public static void clearBackStack(FragmentManager manager) {
        int count = manager.getBackStackEntryCount();
        for (int i = 0; i < count; ++i) {
            manager.popBackStack();
        }
    }

    public static String readFromFile(Context context, String nameFile) {

        File file = new File(getStore(context), nameFile);

        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }

        return text.toString();
    }

    public static FileDescriptor createFileDescriptorFromByteArray(Context context, byte[] mp3SoundByteArray) {
        try {
            // create temp file that will hold byte array
            File tempMp3 = File.createTempFile(Constant.LINK_APP, "mp3", context.getCacheDir());
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(mp3SoundByteArray);
            fos.close();

            FileInputStream fis = new FileInputStream(tempMp3);
            return fis.getFD();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String convertListToString(List<Integer> lstInt) {
        StringBuilder str = new StringBuilder();
        Iterator<Integer> iterator = lstInt.iterator();
        while (iterator.hasNext()) {
            str.append(iterator.next());
            if (iterator.hasNext()) {
                str.append("_");
            }
        }
        return str.toString();
    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName()))
                return true;
        }
        return false;
    }

    public static SpannableString underLine(String strUnder) {
        SpannableString underLine = new SpannableString(strUnder);
        underLine.setSpan(new UnderlineSpan(), 0, underLine.length(), 0);

        return underLine;
    }

    public static String getMIMEType(String url) {
        String mType = null;
        String mExtension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (mExtension != null) {
            mType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(mExtension);
        }
        return mType;
    }

    public static Typeface getTypeFace(String fontFolder, String nameFont, Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/" + fontFolder + "/" + nameFont);
    }

    public static String encodeQuestionPass(String questionPass) {
        byte[] encodedBytes = Base64.encode(questionPass.getBytes(), Base64.DEFAULT);
        return new String(encodedBytes, StandardCharsets.UTF_8);
    }

    public static String getStore(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File f = c.getExternalFilesDir(null);
            if (f != null) return f.getAbsolutePath();
            else return "/storage/emulated/0/Android/data/" + c.getPackageName();
        } else
            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + c.getPackageName();
    }

    public static String makeFolder(Context c, String nameFolder) {
        String path = getStore(c) + "/" + nameFolder;
        File f = new File(path);
        if (!f.exists()) f.mkdirs();
        return path;
    }

    public static void openFacebook(Context c) {
        try {
            ApplicationInfo applicationInfo = c.getPackageManager().getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                Uri uri = Uri.parse("fb://page/" + "111335474750038");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                c.startActivity(intent);
            }
        } catch (Exception ignored) {
            openLink(c, "https://www.facebook.com/REMI-Studio-111335474750038");
        }
    }

    public static void openLink(Context c, String url) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url.replace("HTTPS", "https")));
            c.startActivity(i);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(c, c.getString(R.string.error), Toast.LENGTH_SHORT).show();
        }
    }

    public static void openInstagram(Context c) {
        Uri appUri = Uri.parse("https://instagram.com/_u/" + "remi_studio_app/");
        Uri browserUri = Uri.parse("https://instagram.com/" + "remi_studio_app/");

        try { //first try to open in instagram app
            Intent appIntent = c.getPackageManager().getLaunchIntentForPackage("com.instagram.android");
            if (appIntent != null) {
                appIntent.setAction(Intent.ACTION_VIEW);
                appIntent.setData(appUri);
                c.startActivity(appIntent);
            } else {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, browserUri);
                c.startActivity(browserIntent);
            }
        } catch (Exception e) { //or else open in browser
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, browserUri);
            c.startActivity(browserIntent);
        }
    }

    public static void rateApp(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName()));
        context.startActivity(intent);
    }

    public static void shareApp(Context context) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
            String shareMessage = "Let me recommend you this application\nDownload now:\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + context.getPackageName();
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            context.startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendFeedback(Context context) {
        Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
        selectorIntent.setData(Uri.parse("mailto:"));

        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{Constant.GMAIL});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name) + " feedback");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "The email body...");
        emailIntent.setSelector(selectorIntent);
        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(context, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void forgotQuestionPass(Context context, String encode) {
        Log.d(Constant.TAG, "forgotQuestionPass: " + encode);
        String des = "We will send you a confidential answer.\n" + "Please consider before sending!!!";
        String secureCode = "\nSecure Code: " + encode;
        Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
        selectorIntent.setData(Uri.parse("mailto:"));

        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{Constant.GMAIL});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name) + " help");
        emailIntent.putExtra(Intent.EXTRA_TEXT, des + secureCode);
        emailIntent.setSelector(selectorIntent);
        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(context, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void moreApps(Context context) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:" + Constant.NAME_DEV)));
        } catch (ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/search?q=pub:" + Constant.NAME_DEV)));
        }
    }

    public static void privacyApp(Context context) {
        final String linkPrivacy = "https://gbb-mydiary.blogspot.com";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkPrivacy));
        context.startActivity(browserIntent);
    }

    public static void shareFile(Context context, Bitmap bitmap, String application) {
        Uri uri = saveImageExternal(context, bitmap);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setPackage(application);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setDataAndType(uri, "image/*");

        context.startActivity(Intent.createChooser(shareIntent, context.getResources().getString(R.string.app_name)));
    }

    public static Uri saveImageExternal(Context context, Bitmap image) {
        //TODO - Should be processed in another thread
        Uri uri = null;
        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "remi-diary-maker.png");
            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
            uri = FileProvider.getUriForFile(context, "com.remi.datnt.diarymaker", file);
        } catch (IOException e) {
            Log.d("TAG", "IOException while trying to write file for sharing: " + e.getMessage());
        }
        return uri;
    }
}
