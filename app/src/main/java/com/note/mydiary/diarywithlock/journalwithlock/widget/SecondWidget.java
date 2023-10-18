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
import android.graphics.RectF;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.MainActivity;
import com.note.mydiary.diarywithlock.journalwithlock.model.WidgetModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SecondWidget extends AppWidgetProvider {

    static int w, sizeWidget, heightBitmap;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        // Construct the RemoteViews object
        w = context.getResources().getDisplayMetrics().widthPixels;

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.second_widget);
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Constant.WIDGET_2, true);
        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        else
            pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.widget_2, pendingIntent);

        Bitmap bitmap = getBitmapWidget(context);
        views.setImageViewBitmap(R.id.widget_2, bitmap);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
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
                WidgetModel widget = new WidgetModel("second", appWidgetId);
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

    public static Bitmap getBitmapWidget(Context context) {
        Calendar calendar = Calendar.getInstance(Constant.LOCALE_DEFAULT);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int totalDayInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        if (dayInWeek > 6 && totalDayInMonth == 31) heightBitmap = 95 * w / 100;
        else heightBitmap = 84 * w / 100;
        sizeWidget = 84 * w / 100;
        Bitmap bitmap = Bitmap.createBitmap(sizeWidget, heightBitmap, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        Paint paintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBackground.setColor(Color.WHITE);
        paintBackground.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(0, 0, sizeWidget, heightBitmap, 5f * w / 100, 5f * w / 100, paintBackground);

        Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setSubpixelText(true);
        paintText.setStyle(Paint.Style.FILL);

        Paint paintBitmap = new Paint(Paint.FILTER_BITMAP_FLAG);

        //draw day current
        DrawWidget.drawText(canvas, paintText, 50f * sizeWidget / 100, 16.667f * sizeWidget / 100,
                new SimpleDateFormat(Constant.FULL_DAY, Constant.LOCALE_DEFAULT).format(Calendar.getInstance(Constant.LOCALE_DEFAULT).getTime().getTime()),
                Utils.getTypeFace("poppins", "poppins_semi_bold.ttf", context),
                context.getResources().getColor(R.color.black), 5.33f * sizeWidget / 100);

        //draw day in week
        DrawWidget.drawText(canvas, paintText, Constant.LIST_DX_POSITION[0] * sizeWidget / 100, 31.33f * sizeWidget / 100,
                "M", Utils.getTypeFace("poppins", "poppins_regular.ttf", context),
                context.getResources().getColor(R.color.text_date), 5.33f * sizeWidget / 100);

        DrawWidget.drawText(canvas, paintText, Constant.LIST_DX_POSITION[1] * sizeWidget / 100, 31.33f * sizeWidget / 100,
                "T", Utils.getTypeFace("poppins", "poppins_regular.ttf", context),
                context.getResources().getColor(R.color.text_date), 5.33f * sizeWidget / 100);

        DrawWidget.drawText(canvas, paintText, Constant.LIST_DX_POSITION[2] * sizeWidget / 100, 31.33f * sizeWidget / 100,
                "W", Utils.getTypeFace("poppins", "poppins_regular.ttf", context),
                context.getResources().getColor(R.color.text_date), 5.33f * sizeWidget / 100);

        DrawWidget.drawText(canvas, paintText, Constant.LIST_DX_POSITION[3] * sizeWidget / 100, 31.33f * sizeWidget / 100,
                "T", Utils.getTypeFace("poppins", "poppins_regular.ttf", context),
                context.getResources().getColor(R.color.text_date), 5.33f * sizeWidget / 100);

        DrawWidget.drawText(canvas, paintText, Constant.LIST_DX_POSITION[4] * sizeWidget / 100, 31.33f * sizeWidget / 100,
                "F", Utils.getTypeFace("poppins", "poppins_regular.ttf", context),
                context.getResources().getColor(R.color.text_date), 5.33f * sizeWidget / 100);

        DrawWidget.drawText(canvas, paintText, Constant.LIST_DX_POSITION[5] * sizeWidget / 100, 31.33f * sizeWidget / 100,
                "S", Utils.getTypeFace("poppins", "poppins_regular.ttf", context),
                context.getResources().getColor(R.color.text_date), 5.33f * sizeWidget / 100);

        DrawWidget.drawText(canvas, paintText, Constant.LIST_DX_POSITION[6] * sizeWidget / 100, 31.33f * sizeWidget / 100,
                "S", Utils.getTypeFace("poppins", "poppins_regular.ttf", context),
                context.getResources().getColor(R.color.text_date), 5.33f * sizeWidget / 100);

        //draw day in month
        setDaysInCalendar(context, canvas, paintText);

        return bitmap;
    }

    public static void setDaysInCalendar(Context context, Canvas canvas, Paint paint) {
        ArrayList<String> lstTimeStamp = DataLocalManager.getListTimeStamp(Constant.LIST_TIME_STAMP);
        Calendar calendar = Calendar.getInstance(Constant.LOCALE_DEFAULT);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int dayCurrent = Integer.parseInt(new SimpleDateFormat("dd", Constant.LOCALE_DEFAULT).format(Calendar.getInstance(Constant.LOCALE_DEFAULT).getTime()));
        int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int totalDayInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) + 1;
        int day = 1, colorRes = context.getResources().getColor(R.color.black);

        Paint paintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCircle.setStyle(Paint.Style.FILL);
        paintCircle.setColor(context.getResources().getColor(R.color.selectedDay));

        for (int i = 1; i < 43; i++) {
            if (i == dayInWeek && day < totalDayInMonth) {
                calendar.set(Calendar.DAY_OF_MONTH, day);
                String time = new SimpleDateFormat(Constant.FULL_DATE, Constant.LOCALE_DEFAULT).format(calendar.getTime());
                for (String timeStamp : lstTimeStamp) {
                    if (timeStamp.equals(time)) {
                        DrawWidget.drawCircle(canvas, paintCircle,
                                DrawWidget.getLocationDay(i)[0] * sizeWidget / 100,
                                (DrawWidget.getLocationDay(i)[1] - 2f) * sizeWidget / 100,
                                5.5f * sizeWidget / 100);
                        colorRes = context.getResources().getColor(R.color.white);
                        break;
                    }
                }

                if (day == dayCurrent) {
                    RectF rectF = new RectF(DrawWidget.getLocationDay(i)[0] * sizeWidget / 100 - (5.5f * sizeWidget / 50) / 2,
                            (DrawWidget.getLocationDay(i)[1] - 2f) * sizeWidget / 100 - (5.5f * sizeWidget / 50) / 2,
                            DrawWidget.getLocationDay(i)[0] * sizeWidget / 100 + 5.5f * sizeWidget / 100,
                            DrawWidget.getLocationDay(i)[1] * sizeWidget / 100 + 5.5f * sizeWidget / 100);
                    paintCircle.setColor(context.getResources().getColor(R.color.orange));
                    DrawWidget.drawRec(canvas, paintCircle, rectF);
                    paintCircle.setColor(context.getResources().getColor(R.color.selectedDay));
                }

                DrawWidget.drawText(canvas, paint, DrawWidget.getLocationDay(i)[0] * sizeWidget / 100, DrawWidget.getLocationDay(i)[1] * sizeWidget / 100,
                        String.valueOf(day), Utils.getTypeFace("poppins", "poppins_medium.ttf", context),
                        colorRes, 5f * sizeWidget / 100);
                colorRes = context.getResources().getColor(R.color.black);
                day++;
                dayInWeek++;
            }
        }
    }
}