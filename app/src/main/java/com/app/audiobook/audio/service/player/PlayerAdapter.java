package com.app.audiobook.audio.service.player;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.app.audiobook.audio.service.IntentBuilder;
import com.app.audiobook.audio.service.book.Chapter;

public class PlayerAdapter {

    private Context context;

    public PlayerAdapter(Context context) {
        this.context = context;
    }

    public void init() {
        Intent intent = IntentBuilder.getInstance(context)
                .setCommand(IntentBuilder.Command.INIT)
                .build();

        startIntent(intent);
    }

    public void setAudio(String url) {
        Intent intent = IntentBuilder.getInstance(context)
                .setCommand(IntentBuilder.Command.SET_AUDIO)
                .setMessage(url)
                .build();

        startIntent(intent);
    }

    public void setAudioAndStart(String url) {
        Intent intent = IntentBuilder.getInstance(context)
                .setCommand(IntentBuilder.Command.SET_AUDIO_AND_START)
                .setMessage(url)
                .build();

        startIntent(intent);
    }

    public void changePlayAndPause() {
        Intent intent = IntentBuilder.getInstance(context)
                .setCommand(IntentBuilder.Command.CHANGE_PLAY_AND_PAUSE)
                .build();

        startIntent(intent);
    }

    public void seekTo(int durationPoint) {
        Intent intent = IntentBuilder.getInstance(context)
                .setCommand(IntentBuilder.Command.SEEK_TO)
                .setMessage(String.valueOf(durationPoint))
                .build();

        startIntent(intent);
    }

    private void startIntent(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }

    }

}
