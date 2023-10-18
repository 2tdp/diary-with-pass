package com.note.mydiary.diarywithlock.journalwithlock.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.MainActivity;
import com.note.mydiary.diarywithlock.journalwithlock.activity.database.DatabaseRealm;
import com.note.mydiary.diarywithlock.journalwithlock.model.EmojiModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.WidgetModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.ContentModel;
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.DiaryModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;
import com.note.mydiary.diarywithlock.journalwithlock.utils.UtilsBitmap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.realm.RealmList;

public class ThirdWidget extends AppWidgetProvider {

    static int w, widthWidget, heightWidget;

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.third_widget);
        w = context.getResources().getDisplayMetrics().widthPixels;

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Constant.CALLBACK_WIDGET, "idThird" + appWidgetId);
        PendingIntent pendingIntent;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        else
            pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.widget_3, pendingIntent);

        int idDiary = DataLocalManager.getInt("idThird" + appWidgetId);
        if (idDiary != -1) setViewWidget(context, idDiary, views, appWidgetManager, appWidgetId);
        else
            setViewWidget(context, DataLocalManager.getInt(Constant.ID_DIARY_WIDGET), views, appWidgetManager, appWidgetId);
    }

    public static void setViewWidget(Context context, int idDiary, RemoteViews views, AppWidgetManager appWidgetManager, int appWidgetId) {
        if (idDiary == -1) {
            Toast.makeText(context, context.getString(R.string.you_need_create_a_diary_to_display_on_the_widget), Toast.LENGTH_SHORT).show();
            DiaryModel diaryModel = new DiaryModel(-1, "",
                    new EmojiModel(-1, "emoji_1.png", "emoji", false),
                    Calendar.getInstance(Constant.LOCALE_DEFAULT).getTime().getTime(), new RealmList<>(), false);
            Bitmap bitmap = getBitmapWidget(context, diaryModel);
            views.setImageViewBitmap(R.id.widget_3, bitmap);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
            DataLocalManager.setInt(idDiary, "idThird" + appWidgetId);
        } else DatabaseRealm.getOtherDiary(idDiary, (o, pos) -> {
            DiaryModel diaryModel = (DiaryModel) o;
            Bitmap bitmap = getBitmapWidget(context, diaryModel);
            views.setImageViewBitmap(R.id.widget_3, bitmap);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
            DataLocalManager.setInt(idDiary, "idThird" + appWidgetId);
        });
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals("android.appwidget.action.APPWIDGET_ENABLED"))
            Toast.makeText(context, R.string.add_widget_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            int check = 0;
            ArrayList<WidgetModel> lstWidget = DataLocalManager.getArrWidget(Constant.LIST_ID_WIDGET);
            for (int i = 0; i < lstWidget.size(); i++) {
                if (lstWidget.get(i).getIdWidget() == appWidgetId) check++;
            }
            if (check == 0) {
                WidgetModel widget = new WidgetModel("idThird" + appWidgetId, appWidgetId);
                lstWidget.add(widget);
            }
            DataLocalManager.setArrWidget(Constant.LIST_ID_WIDGET, lstWidget);
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        int check = -1;
        for (int appWidgetId : appWidgetIds) {
            ArrayList<WidgetModel> lstWidget = DataLocalManager.getArrWidget(Constant.LIST_ID_WIDGET);
            for (int i = 0; i < lstWidget.size(); i++) {
                if (appWidgetId == lstWidget.get(i).getIdWidget()) check = i;
            }
            if (check != -1) lstWidget.remove(check);

            DataLocalManager.setArrWidget(Constant.LIST_ID_WIDGET, lstWidget);
        }
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static Bitmap getBitmapWidget(Context context, DiaryModel diary) {
        widthWidget = w;
        heightWidget = w / 2;
        Bitmap bitmap = Bitmap.createBitmap(widthWidget, heightWidget, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        TextRect textRect;

        Paint paintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBackground.setColor(Color.WHITE);
        paintBackground.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(0, 0, widthWidget, heightWidget, 5f * w / 100, 5f * w / 100, paintBackground);

        Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setSubpixelText(true);
        paintText.setStyle(Paint.Style.FILL);

        Paint paintBitmap = new Paint(Paint.FILTER_BITMAP_FLAG);

        //draw Emoji
        DrawWidget.drawBitmap(canvas, paintBitmap, DrawWidget.getBitmapEmoji(context, UtilsBitmap.getBitmapFromAsset(context, diary.getEmojiModel().getFolder(), diary.getEmojiModel().getNameEmoji()), 8.4f * widthWidget / 100, 6.625f * widthWidget / 100, 0.9375f * widthWidget / 100, 0.9375f * widthWidget / 100), 4.375f * widthWidget / 100, 3.125f * widthWidget / 100);

        //draw view day
        DrawWidget.drawBitmap(canvas, paintBitmap, UtilsBitmap.resizeBitmap(UtilsBitmap.getBitmapFromVectorDrawable(context, R.drawable.ic_calendar), (int) (4.6875f * widthWidget / 100), (int) (4.6875f * widthWidget / 100)), 15.625f * widthWidget / 100, 3.125f * widthWidget / 100);

        DrawWidget.drawText(canvas, paintText, 23.75f * widthWidget / 100, 6.25f * widthWidget / 100, context.getString(R.string.day), Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context), context.getResources().getColor(R.color.black), 2.5f * widthWidget / 100);

        //draw date
        DrawWidget.drawText(canvas, paintText, 25.65f * widthWidget / 100, 10.93f * widthWidget / 100, new SimpleDateFormat(Constant.FULL_DATE_DAY, Constant.LOCALE_DEFAULT).format(diary.getDateTimeStamp()), Utils.getTypeFace("poppins", "poppins_regular.ttf", context), context.getResources().getColor(R.color.text_date), 2.75f * widthWidget / 100);

        //draw title
        DrawWidget.drawText(canvas, paintText, 7.8125f * widthWidget / 100, 18.75f * widthWidget / 100, context.getString(R.string.title), Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context), context.getResources().getColor(R.color.black), 3.5f * widthWidget / 100);
        DrawWidget.drawText(canvas, paintText, 18.75f * widthWidget / 100, 18.75f * widthWidget / 100, "(" + diary.getTitleDiary().length() + "/100)", Utils.getTypeFace("poppins", "poppins_regular.ttf", context), context.getResources().getColor(R.color.gray_2), 3.25f * widthWidget / 100);
        textRect = new TextRect(paintText, Utils.getTypeFace("poppins", "poppins_regular.ttf", context), context.getResources().getColor(R.color.black), 3.5f * widthWidget / 100, Paint.Align.LEFT);
        textRect.prepare(diary.getTitleDiary(), (int) (widthWidget * 78.125f / 100), (int) (widthWidget * 7.125f / 100));
        textRect.draw(canvas, (int) (3.9f * widthWidget / 100), (int) (widthWidget * 20.125f / 100));

        //draw content
        DrawWidget.drawText(canvas, paintText, 16.625f * widthWidget / 100, 31.125f * widthWidget / 100, context.getString(R.string.content), Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context), context.getResources().getColor(R.color.black), 3.5f * widthWidget / 100);
        for (ContentModel content : diary.getLstContent()) {
            if (!content.getContent().equals("")) {
                textRect = new TextRect(paintText, Utils.getTypeFace("poppins", "poppins_regular.ttf", context), context.getResources().getColor(R.color.black), 3.5f * widthWidget / 100, Paint.Align.LEFT);
                textRect.prepare(content.getContent(), (int) (widthWidget * 78.125f / 100), (int) (widthWidget * 34.827f / 100));
                textRect.draw(canvas, (int) (3.9f * widthWidget / 100), (int) (widthWidget * 33.825f / 100));
                break;
            }
        }

        return bitmap;
    }
}