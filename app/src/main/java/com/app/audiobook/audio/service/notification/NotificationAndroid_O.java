package com.app.audiobook.audio.service.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;

import androidx.annotation.NonNull;

import com.app.audiobook.audio.book.AudioBook;
import com.app.audiobook.audio.book.Chapter;
import com.app.audiobook.audio.service.BackgroundSoundService;

import static com.app.audiobook.audio.service.BackgroundSoundService.*;

@TargetApi(26)
public class NotificationAndroid_O {
    public static final String CHANNEL_ID =
            String.valueOf(HandleNotifications.getRandomNumber());

    public static void createNotification(Service context, AudioBook audioBook, Chapter chapter) {
        String channelId = createChannel(context);
        Notification notification =
                buildNotification(context, channelId, audioBook, chapter);
        context.startForeground(
                HandleNotifications.ONGOING_NOTIFICATION_ID, notification);
    }

    private static Notification buildNotification(
            Service context, String channelId, AudioBook audioBook, Chapter chapter) {
        // Create Pending Intents.
        PendingIntent piLaunchMainActivity =
                HandleNotifications.getLaunchActivityPI(context);
        PendingIntent piStopService =
                HandleNotifications.getStopServicePI(context);

        // Action to stop the service.
        Notification.Action stopAction =
                new Notification.Action.Builder(
                        HandleNotifications.STOP_ACTION_ICON,
                        HandleNotifications.getNotificationStopActionText(context),
                        piStopService)
                        .build();

        // Create a notification.
        return new Notification.Builder(context, channelId)
                .setContentTitle(HandleNotifications.getNotificationTitle(context))
                .setContentText(HandleNotifications.getNotificationContent(context, audioBook, chapter))
                .setSmallIcon(HandleNotifications.SMALL_ICON)
                .setContentIntent(piLaunchMainActivity)
                .setActions(stopAction)
                .setStyle(new Notification.BigTextStyle())
                .build();
    }

    @NonNull
    private static String createChannel(Service ctx) {
        // Create a channel.
        NotificationManager notificationManager =
                (NotificationManager)
                        ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        CharSequence channelName = "Playback channel";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel notificationChannel =
                new NotificationChannel(
                        CHANNEL_ID, channelName, importance);

        notificationManager.createNotificationChannel(
                notificationChannel);
        return CHANNEL_ID;
    }
}