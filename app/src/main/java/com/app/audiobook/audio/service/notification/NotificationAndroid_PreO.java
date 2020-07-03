package com.app.audiobook.audio.service.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;

import androidx.core.app.NotificationCompat;

import com.app.audiobook.audio.book.AudioBook;
import com.app.audiobook.audio.book.Chapter;

import static com.app.audiobook.audio.service.BackgroundSoundService.*;

@TargetApi(25)
public class NotificationAndroid_PreO {
    public static void createNotification(Service context, AudioBook audioBook, Chapter chapter) {
        // Create Pending Intents.
        PendingIntent piLaunchMainActivity =
                HandleNotifications.getLaunchActivityPI(context);
        PendingIntent piStopService = HandleNotifications.getStopServicePI(context);

        // Action to stop the service.
        NotificationCompat.Action stopAction =
                new NotificationCompat.Action.Builder(
                        HandleNotifications.STOP_ACTION_ICON,
                        HandleNotifications.getNotificationStopActionText(context),
                        piStopService)
                        .build();

        // Create a notification.
        Notification mNotification =
                new NotificationCompat.Builder(context)
                        .setContentTitle(HandleNotifications.getNotificationTitle(context))
                        .setContentText(HandleNotifications.getNotificationContent(context, audioBook, chapter))
                        .setSmallIcon(HandleNotifications.SMALL_ICON)
                        .setContentIntent(piLaunchMainActivity)
                        .addAction(stopAction)
                        .setStyle(new NotificationCompat.BigTextStyle())
                        .build();

        context.startForeground(
                HandleNotifications.ONGOING_NOTIFICATION_ID, mNotification);
    }
}