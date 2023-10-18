package com.note.mydiary.diarywithlock.journalwithlock.broadcast;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.note.mydiary.diarywithlock.journalwithlock.R;
import com.note.mydiary.diarywithlock.journalwithlock.activity.SplashActivity;
import com.note.mydiary.diarywithlock.journalwithlock.model.NotifyModel;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;
import com.note.mydiary.diarywithlock.journalwithlock.utils.Constant;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmBroadcastNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotifyModel notify = DataLocalManager.getNotify(Constant.KEY_NOTIFY);
        Log.d(Constant.TAG, "onReceive: " + getTimeCurrent() + "..." + notify.getTime());

        if (getTimeCurrent().equals(notify.getTime()) && notify.isRun()) {

            Log.d(Constant.TAG, "onReceive: " + getTimeCurrent());
            Intent taskStack = new Intent(context, SplashActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntentWithParentStack(taskStack);
            PendingIntent taskStackIntent;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S)
                taskStackIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
            else
                taskStackIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constant.ID_CHANNEL)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setContentText(notify.getTitle())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setChannelId(Constant.ID_CHANNEL)
                    .setContentIntent(taskStackIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            notificationManagerCompat.notify(1, builder.build());
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String getTimeCurrent() {
        Date date = Calendar.getInstance(Constant.LOCALE_DEFAULT).getTime();
        return new SimpleDateFormat("HH:mm").format(date);
    }
}
