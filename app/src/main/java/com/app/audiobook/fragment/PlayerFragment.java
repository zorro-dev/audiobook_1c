package com.app.audiobook.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.app.audiobook.BaseFragment;
import com.app.audiobook.R;
import com.app.audiobook.adapter.ChapterAdapter;
import com.app.audiobook.audio.Chapter;
import com.app.audiobook.audio.loader.BookLoader;

public class PlayerFragment extends BaseFragment {

    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_player, container, false);

        initInterface();

        return v;
    }

    private void initInterface(){
        initChapterRecyclerView();
    }

    private void initChapterRecyclerView(){
        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewChapter);
        ChapterAdapter adapter = new ChapterAdapter();

        Chapter chapter1 = new Chapter();
        chapter1.setName("Предисловие");
        chapter1.setDurationInSeconds(174);
        chapter1.setState(Chapter.STATE_READ);
        chapter1.setCached(true);

        Chapter chapter2 = new Chapter();
        chapter2.setName("Колдун и прыгливый горшок");
        chapter2.setDurationInSeconds(574);
        chapter2.setState(Chapter.STATE_NOT_READ);
        chapter2.setCached(true);

        Chapter chapter3 = new Chapter();
        chapter3.setName("Фонтан феи Фортуны");
        chapter3.setDurationInSeconds(604);
        chapter3.setState(Chapter.STATE_NOT_READ);
        chapter3.setCached(false);

        adapter.getChapters().add(chapter1);
        adapter.getChapters().add(chapter2);
        adapter.getChapters().add(chapter3);

        recyclerView.setAdapter(adapter);
    }

}
