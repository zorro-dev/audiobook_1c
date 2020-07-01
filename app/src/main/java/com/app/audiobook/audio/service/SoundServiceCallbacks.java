package com.app.audiobook.audio.service;

import android.media.MediaPlayer;

public interface SoundServiceCallbacks {

    void onPlayerStateChanged(MediaPlayer mp, boolean isPlaying);

    void onPlayerUpdate(MediaPlayer mp);

    void onBufferingUpdate(MediaPlayer mp, int percent);

    void onPrepared(MediaPlayer mp);

    void onCompleted(MediaPlayer mp);

    void onSeekTo(MediaPlayer mp, int currentPosition);

    void onPlayerStop();

}
