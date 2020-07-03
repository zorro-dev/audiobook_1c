package com.app.audiobook.audio;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.StringRes;

public class PreferenceUtil {

    public static String getCurrentAudioBookId(Context context) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

        return pref.getString("current_audio_book", null);
    }

    public static void setCurrentAudioBookId(Context context, String audioBookId) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

        pref.edit().putString("current_audio_book", audioBookId).apply();
    }

}
