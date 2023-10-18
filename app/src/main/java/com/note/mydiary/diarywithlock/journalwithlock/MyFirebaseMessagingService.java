package com.note.mydiary.diarywithlock.journalwithlock;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.note.mydiary.diarywithlock.journalwithlock.activity.SplashActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String CHANNEL_ID = "notify_channel";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String title = "", message = "";
        Uri imgUrl = null;

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (notification != null) {
            title = notification.getTitle();
            message = notification.getBody();
            imgUrl = notification.getImageUrl();
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "My Firebase Notification"
                    , NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent openAppIntent = new Intent(this, SplashActivity.class);
        openAppIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            pendingIntent = PendingIntent.getActivity(this, 222, openAppIntent, PendingIntent.FLAG_IMMUTABLE);
        else
            pendingIntent = PendingIntent.getActivity(this, 222, openAppIntent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setContentText(message)
                .setSmallIcon(com.note.remiads.R.drawable.ic_notification);

        Handler handler = new Handler(Looper.getMainLooper(), msg -> {
            Bitmap bm = (Bitmap) msg.obj;
            if (bm != null) {
                notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bm));
            } else {
                notificationBuilder.setStyle(new NotificationCompat.BigTextStyle());
            }
            notificationManager.notify(1030, notificationBuilder.build());
            return true;
        });

        Uri finalImgUrl = imgUrl;
        new Thread(() -> {
            Message m = new Message();
            if (finalImgUrl != null) {
                try {
                    Bitmap bm = Glide.with(MyFirebaseMessagingService.this).asBitmap().load(finalImgUrl).submit().get();
                    if (bm != null)
                        m.obj = bm;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            handler.sendMessage(m);
        }).start();
    }
}
