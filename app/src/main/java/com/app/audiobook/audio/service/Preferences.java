package com.app.audiobook.audio.service;

import android.content.Context;
import android.content.SharedPreferences;


public class Preferences {

    private static final String PREFS = "SoundPreferences";

    public static void setMediaPosition(Context ctx, int position) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        prefs.edit().putInt("position", position).commit();
    }

    public static int getMediaPosition(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return prefs.getInt("position", 0);
    }

}