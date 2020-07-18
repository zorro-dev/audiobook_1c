package com.app.audiobook.fragment;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.app.audiobook.BaseFragment;
import com.app.audiobook.R;
import com.app.audiobook.adapter.ChapterAdapter;
import com.app.audiobook.audio.BookmarkManager;
import com.app.audiobook.utils.PreferenceUtil;
import com.app.audiobook.audio.book.AudioBook;
import com.app.audiobook.audio.book.Bookmark;
import com.app.audiobook.audio.book.Chapter;
import com.app.audiobook.audio.service.BackgroundSoundService;
import com.app.audiobook.audio.service.SoundServiceCallbacks;
import com.app.audiobook.audio.service.player.PlayerAdapter;
import com.app.audiobook.interfaces.ClickListener;
import com.app.audiobook.adapter.BookmarkAdapter;
import com.app.audiobook.ux.MainActivity;
import com.bumptech.glide.Glide;
import com.joooonho.SelectableRoundedImageView;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class PlayerFragment extends BaseFragment implements SoundServiceCallbacks {

    private View v;
    private PlayerAdapter playerAdapter;

    private AudioBook currentAudioBook;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_player, container, false);

        initInterface();

        BackgroundSoundService.setSoundServiceCallbacks(this);
        playerAdapter = getParent().playerAdapter;

        initPlayerControls();

        if (getParent().userCatalog.getCatalogList().size() == 0) {
            showProgress();
        }

        getParent().userCatalog.getCatalogLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<AudioBook>>() {
            @Override
            public void onChanged(ArrayList<AudioBook> audioBooks) {
                if (PreferenceUtil.getCurrentAudioBookId(getContext()) == null) {
                    // устанавливаем на прослушивание подаренную книгу
                    PreferenceUtil.setCurrentAudioBookId(getContext(), audioBooks.get(0).getId());
                }

                getParent().initCurrentAudioBook();
            }
        });

        getParent().currentBook.observe(getViewLifecycleOwner(), new Observer<AudioBook>() {
            @Override
            public void onChanged(AudioBook audioBook) {
                currentAudioBook = audioBook;

                playerAdapter.init();
                playerAdapter.setAudio(
                        currentAudioBook, audioBook.getChapters().get(0));

                initChapterRecyclerView();
                initBookmarkList(audioBook);
                initBookData();

                hideProgress();
            }
        });

        return v;
    }

    private void initInterface() {
        initFragmentDescription();
        initSelector();
        initAddBookmarkButton();
        initDownloadButton();
    }

    private void initDownloadButton() {
        ConstraintLayout download = v.findViewById(R.id.donwload);

        download.setOnClickListener(v -> getParent().initDownloadFragment(currentAudioBook));
    }

    private void initFragmentDescription() {
        LinearLayout descriptions = v.findViewById(R.id.description);
        descriptions.setOnClickListener(v1 -> {
            if (currentAudioBook != null) {
                getParent().initDescriptionFragment(currentAudioBook);
            }
        });

    }

    private void initChapterRecyclerView() {
        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewChapter);
        ChapterAdapter adapter = new ChapterAdapter();

        adapter.setChapters(currentAudioBook.getChapters());

        adapter.setClickListener(new ClickListener() {
            @Override
            public void onClick(View v, int pos) {
                playerAdapter.setCurrentChapter(pos);

                playerAdapter.setAudioAndStart(currentAudioBook,
                        currentAudioBook.getChapters().get(pos));
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void initBookmarkList(AudioBook audioBook) {
        RecyclerView recyclerViewBookMark = v.findViewById(R.id.recyclerViewBookmarks);

        BookmarkAdapter adapter = new BookmarkAdapter();
        ArrayList<Bookmark> bookmarks = BookmarkManager.getBookmarksList(getContext(), audioBook.getId());
        Collections.reverse(bookmarks);

        adapter.setBookmarks(bookmarks);
        adapter.setClickListener((v, pos) -> {
            Bookmark bookmark = adapter.getBookmarks().get(pos);
            Chapter chapter = bookmark.getChapter();

            for (int i = 0; i < audioBook.getChapters().size(); i++) {
                if (audioBook.getChapters().get(i).getId().equals(chapter.getId())) {
                    playerAdapter.setCurrentChapter(i);
                }
            }

            playerAdapter.startFromBookmark(currentAudioBook, bookmark);
        });

        adapter.setLongClickListener((v, pos) -> {
            Bookmark bookmark = adapter.getBookmarks().get(pos);

            BookmarkManager.removeBookmark(getContext(), currentAudioBook.getId(), bookmark.getId());

            adapter.getBookmarks().remove(pos);
            adapter.notifyItemRemoved(pos);
        });

        recyclerViewBookMark.setAdapter(adapter);
    }

    private void initAddBookmarkButton() {
        ConstraintLayout addBookmark = v.findViewById(R.id.add_bookmark);
        addBookmark.setOnClickListener(v1 -> {
            Chapter chapter = currentAudioBook.getChapters().get(playerAdapter.getCurrentChapter());
            Bookmark bookmark = new Bookmark(
                    String.valueOf(System.currentTimeMillis()),
                    getMediaPlayer().getCurrentPosition() / 1000,
                    getStampTime(),
                    chapter);

            BookmarkManager.addBookmark(getContext(), currentAudioBook.getId(), bookmark);
            initBookmarkList(currentAudioBook);
            Toast.makeText(getContext(), "Закладка добавлена", Toast.LENGTH_SHORT).show();
        });
    }

    private void initSelector() {
        ImageView indicatorChapter = v.findViewById(R.id.indicator_chapter);
        ImageView indicatorBookmarks = v.findViewById(R.id.indicator_bookmarks);

        ConstraintLayout chapterLayout = v.findViewById(R.id.chapter);
        ConstraintLayout bookmarksLayout = v.findViewById(R.id.bookmark);

        ConstraintLayout constraintChapter = v.findViewById(R.id.constraintChapter);
        ConstraintLayout constraintBookmarks = v.findViewById(R.id.constraintBookmarks);

        RecyclerView chapterList = v.findViewById(R.id.recyclerViewChapter);
        RecyclerView bookmarkList = v.findViewById(R.id.recyclerViewBookmarks);

        chapterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indicatorChapter.setVisibility(View.VISIBLE);
                indicatorBookmarks.setVisibility(View.INVISIBLE);

                constraintChapter.setVisibility(View.VISIBLE);
                constraintBookmarks.setVisibility(View.INVISIBLE);
            }
        });

        bookmarksLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indicatorChapter.setVisibility(View.INVISIBLE);
                indicatorBookmarks.setVisibility(View.VISIBLE);

                constraintChapter.setVisibility(View.INVISIBLE);
                constraintBookmarks.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initPlayerControls() {
        ConstraintLayout play = v.findViewById(R.id.play);
        ConstraintLayout prevSeek = v.findViewById(R.id.prev_20);
        ConstraintLayout nextSeek = v.findViewById(R.id.next_20);
        ConstraintLayout nextChapter = v.findViewById(R.id.next);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentAudioBook != null) {
                    if (getMediaPlayer() == null) {
                        Log.v("lol", "media player == null");
                        playerAdapter.init();
                        Chapter currentChapter = currentAudioBook.getChapters().get(playerAdapter.getCurrentChapter());
                        playerAdapter.setAudioAndStart(
                                currentAudioBook, currentChapter);
                    } else {
                        Log.v("lol", "media player != null");
                        playerAdapter.changePlayAndPause();
                    }
                }
            }
        });

        prevSeek.setOnClickListener(v1 -> {
            int currentPosition = getMediaPlayer().getCurrentPosition();
            int seekIntervalInSecond = 20;
            int seekTo = currentPosition - seekIntervalInSecond * 1000;
            playerAdapter.seekTo(Math.max(seekTo, 0));
        });

        nextSeek.setOnClickListener(v1 -> {
            int currentPosition = getMediaPlayer().getCurrentPosition();
            int seekIntervalInSecond = 20;
            int seekTo = currentPosition + seekIntervalInSecond * 1000;
            int maxDuration = getMediaPlayer().getDuration();
            playerAdapter.seekTo(Math.min(seekTo, maxDuration));
        });

        nextChapter.setOnClickListener(v1 -> {
            playerAdapter.startNextChapter();
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

    private void initBookData(){
        SelectableRoundedImageView cover = v.findViewById(R.id.book_cover);

        ConstraintLayout coverCountLayout = v.findViewById(R.id.cover_count_layout);
        TextView coverCountText = v.findViewById(R.id.cover_count_text);

        TextView title = v.findViewById(R.id.title);
        TextView author = v.findViewById(R.id.author);
        TextView count_parts = v.findViewById(R.id.count_parts);

        title.setText(currentAudioBook.getTitle());
        author.setText(currentAudioBook.getAuthor().getName());
        count_parts.setText(currentAudioBook.getChapterSize() + " частей");

        Glide.with(getContext())
                .load(currentAudioBook.getCoverUrl())
                .placeholder(R.drawable.ic_black_square)
                .into(cover);

        final String[] coverList = new String[] {
                currentAudioBook.getCoverUrl(),
                currentAudioBook.getCoverUrl(),
                currentAudioBook.getCoverUrl(),
        };

        cover.setOnClickListener(v1 -> {


            new ImageViewer.Builder(getContext(), coverList)
                    .setStartPosition(0)
                    .show();
        });

        if (coverList.length == 1) {
            coverCountLayout.setVisibility(View.GONE);
        } else {
            coverCountText.setText("Еще " + String.valueOf(coverList.length - 1));
        }

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

        if (currentAudioBook != null) {
            TextView chapterTitle = v.findViewById(R.id.chapter_name);
            chapterTitle.setText(currentAudioBook.getChapters().get(playerAdapter.getCurrentChapter()).getName());
        }
    }

    @Override
    public void onCompleted(MediaPlayer mp) {
        Log.v("lol", "onCompleted");
        playerAdapter.startNextChapter();
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

    private void showProgress() {
        ConstraintLayout progressLayout = v.findViewById(R.id.progress_layout);
        progressLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        ConstraintLayout progressLayout = v.findViewById(R.id.progress_layout);
        progressLayout.setVisibility(View.GONE);
    }

    private String getStampTime() {
        // Текущее время
        Date currentDate = new Date();
        // Форматирование времени как "день.месяц.год"
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        // Форматирование времени как "часы:минуты:секунды"
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        return dateText + " " + timeText;
    }

}
