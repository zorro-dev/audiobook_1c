package com.app.audiobook.audio.service;

import android.content.Context;
import android.content.Intent;

public class IntentBuilder {

    public class Command {

        public static final int INVALID = 0;
        public static final int INIT = 100;     // инициализация плеера
        public static final int LAUNCH = 101;   // нажатие по уведомлению
        public static final int CHANGE_PLAY_AND_PAUSE = 102;    // изменение состояние плеера
        public static final int SET_AUDIO = 103;    // установить запись
        public static final int SET_AUDIO_AND_START = 1003;    // установить запись и начать проигрывание
        public static final int SEEK_TO = 104;     // перемотка плеера
        public static final int STOP = 105;     // отключение плеера
        public static final int START_FROM_BOOKMARK = 106;     // начать с закладки

    }

    public static final String KEY_COMMAND = "KEY_COMMAND";
    public static final String KEY_MESSAGE = "KEY_MESSAGE";
    public static final String KEY_MESSAGE2 = "KEY_MESSAGE2";

    private Context mContext;
    private String mMessage;
    private String mMessage2;
    private int mCommandId;

    public static IntentBuilder getInstance(Context context) {
        return new IntentBuilder(context);
    }

    public IntentBuilder(Context context) {
        this.mContext = context;
    }

    public IntentBuilder setMessage(String message) {
        this.mMessage = message;
        return this;
    }

    public IntentBuilder setMessage2(String message) {
        this.mMessage2 = message;
        return this;
    }

    public IntentBuilder setCommand(int command) {
        this.mCommandId = command;
        return this;
    }

    public Intent build() {
        Intent intent = new Intent(mContext, BackgroundSoundService.class);

        if (mCommandId != Command.INVALID) {
            intent.putExtra(KEY_COMMAND, mCommandId);
        }
        if (mMessage != null) {
            intent.putExtra(KEY_MESSAGE, mMessage);
        }
        if (mMessage != null) {
            intent.putExtra(KEY_MESSAGE2, mMessage2);
        }
        return intent;
    }
}
