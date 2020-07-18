package com.app.audiobook.audio.service.player;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.app.audiobook.audio.book.AudioBook;
import com.app.audiobook.audio.book.Bookmark;
import com.app.audiobook.audio.book.Chapter;
import com.app.audiobook.audio.service.IntentBuilder;
import com.app.audiobook.component.JSONManager;

public class PlayerAdapter {

    private Context context;
    private int currentChapter = 0;
    private AudioBook currentAudioBook;

    public PlayerAdapter(Context context) {
        this.context = context;
    }

    public void init() {
        Intent intent = IntentBuilder.getInstance(context)
                .setCommand(IntentBuilder.Command.INIT)
                .build();

        startIntent(intent);
    }

    public int getCurrentChapter() {
        return currentChapter;
    }

    public void setCurrentChapter(int currentChapter) {
        this.currentChapter = currentChapter;
    }

    public void startNextChapter()  {
        currentChapter++;

        if (currentChapter < currentAudioBook.getChapterSize()) {
            setAudioAndStart(
                    currentAudioBook, currentAudioBook.getChapters().get(currentChapter));
        }
    }

    public void setAudio(AudioBook audio, Chapter chapter) {
        currentAudioBook = audio;
        Intent intent = IntentBuilder.getInstance(context)
                .setCommand(IntentBuilder.Command.SET_AUDIO)
                .setMessage(JSONManager.exportToJSON(audio))
                .setMessage2(JSONManager.exportToJSON(chapter))
                .build();

        startIntent(intent);
    }

    public void setAudioAndStart(AudioBook audio, Chapter chapter) {
        Intent intent = IntentBuilder.getInstance(context)
                .setCommand(IntentBuilder.Command.SET_AUDIO_AND_START)
                .setMessage(JSONManager.exportToJSON(audio))
                .setMessage2(JSONManager.exportToJSON(chapter))
                .build();

        startIntent(intent);
    }

    public void startFromBookmark(AudioBook audio, Bookmark bookmark){
        Chapter chapter = bookmark.getChapter();

        Intent intent = IntentBuilder.getInstance(context)
                .setCommand(IntentBuilder.Command.START_FROM_BOOKMARK)
                .setMessage(JSONManager.exportToJSON(audio))
                .setMessage2(JSONManager.exportToJSON(chapter))
                .setMessage3(String.valueOf(bookmark.getDurationInSeconds() * 1000))
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
