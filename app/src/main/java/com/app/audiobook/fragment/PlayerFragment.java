package com.app.audiobook.fragment;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.app.audiobook.BaseFragment;
import com.app.audiobook.R;
import com.app.audiobook.adapter.ChapterAdapter;
import com.app.audiobook.audio.service.BackgroundSoundService;
import com.app.audiobook.audio.service.SoundServiceCallbacks;
import com.app.audiobook.audio.service.player.PlayerAdapter;
import com.app.audiobook.ux.MainActivity;

import static com.app.audiobook.ux.MainActivity.URL;

public class PlayerFragment extends BaseFragment implements SoundServiceCallbacks {

    private View v;
    private PlayerAdapter playerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_player, container, false);

        initInterface();

        BackgroundSoundService.setSoundServiceCallbacks(this);
        playerAdapter = getParent().playerAdapter;

        initPlayerControls();

        if (getMediaPlayer() == null) {
            playerAdapter.init();
            playerAdapter.setAudio(URL);
        }

        return v;
    }

    private void initInterface() {
        initChapterRecyclerView();
    }

    private void initChapterRecyclerView() {
        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewChapter);
        ChapterAdapter adapter = new ChapterAdapter();

        recyclerView.setAdapter(adapter);
    }

    private void initPlayerControls() {
        ConstraintLayout play = v.findViewById(R.id.play);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getMediaPlayer() == null) {
                    Log.v("lol", "media player == null");
                    playerAdapter.init();
                    playerAdapter.setAudioAndStart(URL);
                } else {
                    Log.v("lol", "media player != null");
                    playerAdapter.changePlayAndPause();
                }
            }
        });

        SeekBar seekBar = v.findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    playerAdapter.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setSpeed(MediaPlayer mp, float speed) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mp.setPlaybackParams(mp.getPlaybackParams().setSpeed(speed));
        }
    }

    private MainActivity getParent() {
        return (MainActivity) getActivity();
    }

    @Override
    public void onPlayerStateChanged(MediaPlayer mp, boolean isPlaying) {
        Log.v("lol", "onPlayerStateChanged");
        ImageView playIcon = v.findViewById(R.id.play_icon);
        playIcon.setImageResource(isPlaying ?
                R.drawable.ic_pause : R.drawable.ic_play);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.v("lol", "onBufferingUpdate");
        SeekBar seekBar = v.findViewById(R.id.seek_bar);

        seekBar.setSecondaryProgress(percent);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.v("lol", "onPrepared");
        TextView chapterTitle = v.findViewById(R.id.chapter_name);
        chapterTitle.setText("name");
    }

    @Override
    public void onCompleted(MediaPlayer mp) {
        Log.v("lol", "onCompleted");
    }

    @Override
    public void onSeekTo(MediaPlayer mp, int currentPosition) {
        Log.v("lol", "onSeekTo");
        SeekBar seekBar = v.findViewById(R.id.seek_bar);
        TextView timer = v.findViewById(R.id.timer);

        timer.setText(getFormattedTime(mp.getCurrentPosition() / 1000) +
                " / " + getFormattedTime(mp.getDuration() / 1000));

        seekBar.setMax(mp.getDuration());
        seekBar.setProgress(mp.getCurrentPosition());
    }

    @Override
    public void onPlayerStop() {
        Log.v("lol", "onPlayerStop");
        ImageView playIcon = v.findViewById(R.id.play_icon);
        playIcon.setImageResource(R.drawable.ic_play);
    }

    @Override
    public void onPlayerUpdate(MediaPlayer mp) {
        SeekBar seekBar = v.findViewById(R.id.seek_bar);
        TextView timer = v.findViewById(R.id.timer);
        ImageView playIcon = v.findViewById(R.id.play_icon);

        timer.setText(getFormattedTime(mp.getCurrentPosition() / 1000) +
                " / " + getFormattedTime(mp.getDuration() / 1000));

        seekBar.setMax(mp.getDuration());
        seekBar.setProgress(mp.getCurrentPosition());

        playIcon.setImageResource(mp.isPlaying() ?
                R.drawable.ic_pause : R.drawable.ic_play);
    }

    public static String getFormattedTime(int time) {

        int minutes = time / 60;

        int seconds = time - (60 * minutes);

        String minutesStr = String.valueOf(minutes);
        String secondsStr;

        if (seconds < 10) {
            secondsStr = "0" + String.valueOf(seconds);
        } else {
            secondsStr = String.valueOf(seconds);
        }

        return minutesStr + ":" + secondsStr;
    }

    private MediaPlayer getMediaPlayer() {
        return BackgroundSoundService.getMediaPlayer();
    }

}
