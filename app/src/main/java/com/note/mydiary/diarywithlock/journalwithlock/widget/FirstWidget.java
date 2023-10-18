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
import com.note.mydiary.diarywithlock.journalwithlock.model.diarymodel.AudioModel;
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

public class FirstWidget extends AppWidgetProvider {

    static int w, sizeWidget;

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews fistView = new RemoteViews(context.getPackageName(), R.layout.first_widget);
        w = context.getResources().getDisplayMetrics().widthPixels;
        PendingIntent pendingIntent;
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Constant.CALLBACK_WIDGET, "idFirst" + appWidgetId);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        else
            pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        fistView.setOnClickPendingIntent(R.id.widget_1, pendingIntent);

        int idDiary = DataLocalManager.getInt("idFirst" + appWidgetId);

        if (idDiary != -1)
            setViewWidget(context, idDiary, fistView, appWidgetManager, appWidgetId);
        else
            setViewWidget(context, DataLocalManager.getInt(Constant.ID_DIARY_WIDGET), fistView, appWidgetManager, appWidgetId);
    }

    public static void setViewWidget(Context context, int idDiary, RemoteViews views, AppWidgetManager appWidgetManager, int appWidgetId) {
        if (idDiary == -1) {
            Toast.makeText(context, context.getString(R.string.you_need_create_a_diary_to_display_on_the_widget), Toast.LENGTH_SHORT).show();
            RealmList<ContentModel> lstContent = new RealmList<>();
            lstContent.add(new ContentModel(-1, -1, "(no content)", new AudioModel(), new RealmList<>()));
            DiaryModel diaryModel = new DiaryModel(-1, "(no title)",
                    new EmojiModel(-1, "emoji_1.png", "emoji", false),
                    Calendar.getInstance(Constant.LOCALE_DEFAULT).getTime().getTime(), lstContent, false);
            Bitmap bitmap = getBitmapWidget(context, diaryModel);
            views.setImageViewBitmap(R.id.widget_1, bitmap);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
            DataLocalManager.setInt(idDiary, "idFirst" + appWidgetId);
        } else
            DatabaseRealm.getOtherDiary(idDiary, (o, pos) -> {
                DiaryModel diaryModel = (DiaryModel) o;
                Bitmap bitmap = getBitmapWidget(context, diaryModel);
                views.setImageViewBitmap(R.id.widget_1, bitmap);

                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views);
                DataLocalManager.setInt(idDiary, "idFirst" + appWidgetId);
            });
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
                WidgetModel widget = new WidgetModel("idFirst" + appWidgetId, appWidgetId);
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
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals("android.appwidget.action.APPWIDGET_ENABLED"))
            Toast.makeText(context, R.string.add_widget_success, Toast.LENGTH_SHORT).show();
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
        sizeWidget = 84 * w / 100;
        Bitmap bitmap = Bitmap.createBitmap(sizeWidget, sizeWidget, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        TextRect textRect;

        Paint paintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBackground.setColor(Color.WHITE);
        paintBackground.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(0, 0, sizeWidget, sizeWidget, 5f * w / 100, 5f * w / 100, paintBackground);

        Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setSubpixelText(true);
        paintText.setStyle(Paint.Style.FILL);

        Paint paintBitmap = new Paint(Paint.FILTER_BITMAP_FLAG);

        //draw Emoji
        DrawWidget.drawBitmap(canvas, paintBitmap, DrawWidget.getBitmapEmoji(context,
                        UtilsBitmap.getBitmapFromAsset(context, diary.getEmojiModel().getFolder(), diary.getEmojiModel().getNameEmoji()),
                        16f * sizeWidget / 100, 12f * sizeWidget / 100, 2f * sizeWidget / 100, 2f * sizeWidget / 100),
                5.33f * sizeWidget / 100, 6.667f * sizeWidget / 100);

        //draw view day
        DrawWidget.drawBitmap(canvas, paintBitmap, UtilsBitmap.getBitmapFromVectorDrawable(context, R.drawable.ic_calendar),
                29.33f * sizeWidget / 100, 6.679f * sizeWidget / 100);

        DrawWidget.drawText(canvas, paintText,
                43.33f * sizeWidget / 100, 11.678f * sizeWidget / 100,
                context.getString(R.string.day), Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context),
                context.getResources().getColor(R.color.black), 5.33f * sizeWidget / 100);

        //draw date
        DrawWidget.drawText(canvas, paintText,
                50.33f * sizeWidget / 100, 20f * sizeWidget / 100,
                new SimpleDateFormat(Constant.FULL_DATE_DAY, Constant.LOCALE_DEFAULT).format(diary.getDateTimeStamp()),
                Utils.getTypeFace("poppins", "poppins_regular.ttf", context),
                context.getResources().getColor(R.color.text_date), 5.33f * sizeWidget / 100);

        //draw title
        DrawWidget.drawText(canvas, paintText, 12.33f * sizeWidget / 100, 35f * sizeWidget / 100,
                context.getString(R.string.title), Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context),
                context.getResources().getColor(R.color.black), 5.33f * sizeWidget / 100);
        DrawWidget.drawText(canvas, paintText, 28.667f * sizeWidget / 100, 35f * sizeWidget / 100,
                "(" + diary.getTitleDiary().length() + "/100)", Utils.getTypeFace("poppins", "poppins_regular.ttf", context),
                context.getResources().getColor(R.color.gray_2), 4.99f * sizeWidget / 100);
        textRect = new TextRect(paintText,
                Utils.getTypeFace("poppins", "poppins_regular.ttf", context),
                context.getResources().getColor(R.color.black),
                5.33f * sizeWidget / 100, Paint.Align.LEFT);
        textRect.prepare(diary.getTitleDiary(), sizeWidget * 84 / 100, (int) (sizeWidget * 8.667f / 100));
        textRect.draw(canvas, (int) (7.56f * sizeWidget / 100), (int) (sizeWidget * 37.5f / 100));

        //draw content
        DrawWidget.drawText(canvas, paintText, 26f * sizeWidget / 100, 55f * sizeWidget / 100,
                context.getString(R.string.content), Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context),
                context.getResources().getColor(R.color.black), 5.33f * sizeWidget / 100);
        for (ContentModel content : diary.getLstContent()) {
            if (!content.getContent().equals("")) {
                textRect = new TextRect(paintText,
                        Utils.getTypeFace("poppins", "poppins_regular.ttf", context),
                        context.getResources().getColor(R.color.black),
                        5.33f * sizeWidget / 100, Paint.Align.LEFT);
                textRect.prepare(content.getContent(), sizeWidget * 84 / 100, sizeWidget * 26 / 100);
                textRect.draw(canvas, (int) (7.56f * sizeWidget / 100), (int) (sizeWidget * 58f / 100));
                break;
            }
        }

        return bitmap;
    }
}