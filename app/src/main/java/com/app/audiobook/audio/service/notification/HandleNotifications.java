package com.app.audiobook.audio.service.notification;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import com.app.audiobook.R;
import com.app.audiobook.audio.service.IntentBuilder;

public class HandleNotifications {

    public static final int ONGOING_NOTIFICATION_ID = 12312;
    public static final int STOP_ACTION_ICON = R.drawable.ic_play;
    public static final int SMALL_ICON = R.drawable.ic_round;

    public static PendingIntent getLaunchActivityPI(Service context) {
        PendingIntent piService;
        {
            Intent iService = new IntentBuilder(context)
                    .setCommand(IntentBuilder.Command.LAUNCH).build();
            piService = PendingIntent.getService(
                    context, getRandomNumber(), iService, 0);
        }
        return piService;
    }

    public static PendingIntent getStopServicePI(Service context) {
        PendingIntent piStopService;
        {
            Intent iStopService = new IntentBuilder(context)
                    .setCommand(IntentBuilder.Command.STOP).build();
            piStopService = PendingIntent.getService(
                    context, getRandomNumber(), iStopService, 0);
        }
        return piStopService;
    }

    public static String getNotificationTitle(Context context) {
        return "Title";
    }

    public static String getNotificationContent(Context context) {
        return "Content";
    }

    public static String getNotificationStopActionText(Context context) {
        return "StopActionText";
    }

    public static int getRandomNumber() {
        return rnd(0, 1000);
    }

    /**
     * Метод получения псевдослучайного целого числа от min до max (включая max);
     */
    public static int rnd(int min, int max)
    {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

}