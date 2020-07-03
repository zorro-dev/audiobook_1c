package com.app.audiobook.audio.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.audiobook.audio.book.AudioBook;
import com.app.audiobook.audio.book.Chapter;
import com.app.audiobook.audio.service.notification.NotificationAndroid_O;
import com.app.audiobook.audio.service.notification.NotificationAndroid_PreO;
import com.app.audiobook.component.JSONManager;

import java.io.File;
import java.io.IOException;

import static com.app.audiobook.audio.service.IntentBuilder.KEY_COMMAND;
import static com.app.audiobook.audio.service.IntentBuilder.KEY_MESSAGE;
import static com.app.audiobook.audio.service.IntentBuilder.KEY_MESSAGE2;
import static com.app.audiobook.audio.service.IntentBuilder.KEY_MESSAGE3;

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
        createNotification(null, null);

        Log.i(TAG, "onCreate()");


        Toast.makeText(this, "Service started...", Toast.LENGTH_SHORT).show();
    }

    private void createNotification(AudioBook audioBook, Chapter chapter) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationAndroid_O.createNotification(this, audioBook, chapter);
        } else {
            NotificationAndroid_PreO.createNotification(this, audioBook, chapter);
        }
    }

    private boolean needToStart = false;
    private int bookmarkPoint = 0;

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mSoundServiceCallbacks != null) {
            mSoundServiceCallbacks.onPrepared(mp);
        }

        if (bookmarkPoint != 0) {
            mediaPlayer.seekTo(bookmarkPoint);
            bookmarkPoint = 0;
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
            String jsonAudioBook = intent.getStringExtra(KEY_MESSAGE);
            String jsonChapter = intent.getStringExtra(KEY_MESSAGE2);

            AudioBook audioBook = JSONManager.importFromJSON(jsonAudioBook, AudioBook.class);
            Chapter chapter = JSONManager.importFromJSON(jsonChapter, Chapter.class);

            setAudio(audioBook, chapter, false);
        } else if (command == IntentBuilder.Command.SET_AUDIO_AND_START) {
            Log.i(TAG, "SET_AUDIO");
            String jsonAudioBook = intent.getStringExtra(KEY_MESSAGE);
            String jsonChapter = intent.getStringExtra(KEY_MESSAGE2);

            AudioBook audioBook = JSONManager.importFromJSON(jsonAudioBook, AudioBook.class);
            Chapter chapter = JSONManager.importFromJSON(jsonChapter, Chapter.class);

            setAudio(audioBook, chapter, true);
        } else if (command == IntentBuilder.Command.CHANGE_PLAY_AND_PAUSE) {
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

            mediaPlayer.seekTo(durationPoint);
        } else if (command == IntentBuilder.Command.STOP) {
            Log.i(TAG, "STOP");
            stopPlayer();
        } else if (command == IntentBuilder.Command.START_FROM_BOOKMARK) {
            Log.i(TAG, "STOP");
            String jsonAudioBook = intent.getStringExtra(KEY_MESSAGE);
            String jsonChapter = intent.getStringExtra(KEY_MESSAGE2);
            int bookmarkPoint = Integer.parseInt(intent.getStringExtra(KEY_MESSAGE3));

            AudioBook audioBook = JSONManager.importFromJSON(jsonAudioBook, AudioBook.class);
            Chapter chapter = JSONManager.importFromJSON(jsonChapter, Chapter.class);

            setAudio(audioBook, chapter, true, bookmarkPoint);
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

    private void setAudio(AudioBook audioBook, Chapter chapter, boolean needToStart) {
        setAudio(audioBook, chapter, needToStart, 0);
    }

    private void setAudio(AudioBook audioBook, Chapter chapter, boolean needToStart, int bookmarkPoint) {
        mediaPlayer.reset();

        createNotification(audioBook, chapter);

        this.needToStart = needToStart;
        this.bookmarkPoint = bookmarkPoint;

        try {
            int permissionStatus1 = ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int permissionStatus2 = ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

            boolean isPermissionGranted = permissionStatus1 == PackageManager.PERMISSION_GRANTED ||
                    permissionStatus2 == PackageManager.PERMISSION_GRANTED;

            if (isPermissionGranted) {
                String cacheFilePath = Environment.getExternalStorageDirectory() + "/AudioBook/cache/" + audioBook.getId() + "/" + chapter.getId() + ".lol";

                File file = new File(cacheFilePath);

                if (file.exists()) {
                    // считываем с памяти телефона
                    mediaPlayer.setDataSource(cacheFilePath);
                } else {
                    mediaPlayer.setDataSource(chapter.getUrl());
                }
            } else {
                mediaPlayer.setDataSource(chapter.getUrl());
            }
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