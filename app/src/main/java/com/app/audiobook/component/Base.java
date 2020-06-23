package com.app.audiobook.component;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

public class Base {

    public static boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

    public static boolean isFirstRun(Context context, String userId, String activityTAG) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);

        return pref.getBoolean("isFirstRun " + activityTAG + " " +userId, true);
    }

    public static void setFirstRun(Context context, String userId, String activityTAG) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);

        pref.edit().putBoolean("isFirstRun " + activityTAG + " " +userId, false).apply();
    }



}
