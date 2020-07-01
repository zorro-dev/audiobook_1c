package com.app.audiobook.audio.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.app.audiobook.audio.service.notification.NotificationAndroid_O;
import com.app.audiobook.audio.service.notification.NotificationAndroid_PreO;

import java.io.IOException;

import static com.app.audiobook.audio.service.IntentBuilder.KEY_COMMAND;

public class BackgroundSoundService extends Service
        implements MediaPlayer.OnPreparedListener {

    private static final String TAG = "lol";

    private Handler handler;
    private Runnable runnable;

    private static MediaPlayer mediaPlayer;
    private static SoundServiceCallbacks mSoundServiceCallbacks;


    public static void setSoundServiceCallbacks(SoundServiceCallbacks soundServiceCallbacks) {
        mSoundServiceCallbacks = soundServiceCallbacks;
    }

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "onBind()");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationAndroid_O.createNotification(this);
        } else {
            NotificationAndroid_PreO.createNotification(this);
        }

        Log.i(TAG, "onCreate()");



        Toast.makeText(this, "Service started...", Toast.LENGTH_SHORT).show();
    }

    private boolean needToStart = false;

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mSoundServiceCallbacks != null) {
            mSoundServiceCallbacks.onPrepared(mp);
        }

//        startPlayer();
//        Log.v(TAG, "duration : " + String.valueOf(mediaPlayer.getDuration()));
//
//        if(Preferences.getMediaPosition(getApplicationContext())>0){
//            Log.i(TAG, "onStartCommand(), position stored, continue from position : " + Preferences.getMediaPosition(getApplicationContext()));
//            startPlayer();
//            //mediaPlayer.seekTo(Preferences.getMediaPosition(getApplicationContext()));
//        }else {
//            Log.i(TAG, "onStartCommand() Start!...");
//            startPlayer();
//        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mSoundServiceCallbacks != null && mp.getCurrentPosition() == mp.getDuration()) {
                    mSoundServiceCallbacks.onCompleted(mp);
                }
            }
        });

        startSaveHandler();

        if (needToStart) {
            startPlayer();
            needToStart = false;
        }
    }

    private final int SAVE_HANDLER_INTERVAL = 500;

    private void startSaveHandler() {
        handler = new Handler(Looper.getMainLooper());

        runnable = () -> {
            if (mSoundServiceCallbacks != null) {
                mSoundServiceCallbacks.onPlayerUpdate(mediaPlayer);
            }
            Preferences.setMediaPosition(getApplicationContext(), mediaPlayer.getCurrentPosition());
            handler.postDelayed(runnable, SAVE_HANDLER_INTERVAL);
        };

        handler.postDelayed(runnable, SAVE_HANDLER_INTERVAL);
    }

    private void stopSaveHandler() {
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand()");

        int command = intent.getIntExtra(KEY_COMMAND, 0);

        if (command == IntentBuilder.Command.LAUNCH) {
            Log.i(TAG, "LAUNCH");
        } else if (command == IntentBuilder.Command.INIT) {
            Log.i(TAG, "INIT");
            initPlayer();
        } else if (command == IntentBuilder.Command.SET_AUDIO) {
            Log.i(TAG, "SET_AUDIO");
            String url = intent.getStringExtra(IntentBuilder.KEY_MESSAGE);
            setAudio(url, false);
        } else if (command == IntentBuilder.Command.SET_AUDIO_AND_START) {
            Log.i(TAG, "SET_AUDIO");
            String url = intent.getStringExtra(IntentBuilder.KEY_MESSAGE);
            setAudio(url, true);
        }else if (command == IntentBuilder.Command.CHANGE_PLAY_AND_PAUSE) {
            Log.i(TAG, "CHANGE");

            if (mediaPlayer == null) {
                initPlayer();
            }

            Log.i(TAG, "isPlaying : " + String.valueOf(mediaPlayer.isPlaying()));

            if (mediaPlayer.isPlaying()) {
                pausePlayer();
            } else {
                startPlayer();
                startSaveHandler();
            }
        } else if (command == IntentBuilder.Command.SEEK_TO) {
            Log.i(TAG, "SEEK_TO");
            int durationPoint = Integer.parseInt(intent.getStringExtra(IntentBuilder.KEY_MESSAGE));

            Log.i(TAG, "duration point : " + String.valueOf(durationPoint));

            mediaPlayer.seekTo(durationPoint);
        } else if (command == IntentBuilder.Command.STOP) {
            Log.i(TAG, "STOP");
            stopPlayer();
        }

//        if(Preferences.getMediaPosition(getApplicationContext())>0){
//            Log.i(TAG, "onStartCommand(), position stored, continue from position : " + Preferences.getMediaPosition(getApplicationContext()));
//            mediaPlayer.start();
//            //mediaPlayer.seekTo(Preferences.getMediaPosition(getApplicationContext()));
//        }else {
//            Log.i(TAG, "onStartCommand() Start!...");
//            mediaPlayer.start();
//        }
        //re-create the service if it is killed.
        return Service.START_STICKY;
    }

    private void initPlayer() {
        if (mediaPlayer != null) {
            return;
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.v(TAG, "onBufferingUpdate : " + String.valueOf(percent));
                if (mSoundServiceCallbacks != null) {
                    mSoundServiceCallbacks.onBufferingUpdate(mp, percent);
                }
            }
        });
        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                if (mSoundServiceCallbacks != null) {
                    mSoundServiceCallbacks.onSeekTo(mp, mp.getCurrentPosition());
                }
            }
        });
    }

    private void setAudio(String url, boolean needToStart) {
        this.needToStart = needToStart;

        try {
            mediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.prepareAsync();
    }

    private void startPlayer() {
        mediaPlayer.start();
        if (mSoundServiceCallbacks != null) {
            mSoundServiceCallbacks.onPlayerStateChanged(mediaPlayer, true);
        }
    }

    private void pausePlayer() {
        mediaPlayer.pause();

        Preferences.setMediaPosition(getApplicationContext(), mediaPlayer.getCurrentPosition());

        if (mSoundServiceCallbacks != null) {
            mSoundServiceCallbacks.onPlayerStateChanged(mediaPlayer, false);
        }
    }

    private void stopPlayer() {
        if (mSoundServiceCallbacks != null) {
            mSoundServiceCallbacks.onPlayerStop();
        }

        stopSelf();
    }

    public IBinder onUnBind(Intent arg0) {
        Log.i(TAG, "onUnBind()");
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSaveHandler();
        Log.i(TAG, "onDestroy() , service stopped! Media position: " + mediaPlayer.getCurrentPosition());
        //Save current position before destruction.
        Preferences.setMediaPosition(getApplicationContext(), mediaPlayer.getCurrentPosition());
        mediaPlayer.pause();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    @Override
    public void onLowMemory() {
        Log.i(TAG, "onLowMemory()");
        Preferences.setMediaPosition(getApplicationContext(), mediaPlayer.getCurrentPosition());
    }

    //Inside AndroidManifest.xml add android:stopWithTask="false" to the Service definition.
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i(TAG, "onTaskRemoved(), save current position: " + mediaPlayer.getCurrentPosition());
        //instead of stop service, save the current position.
        //stopSelf();
        Preferences.setMediaPosition(getApplicationContext(), mediaPlayer.getCurrentPosition());
    }

}