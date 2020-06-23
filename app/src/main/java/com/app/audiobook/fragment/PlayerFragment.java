package com.app.audiobook.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.app.audiobook.BaseFragment;
import com.app.audiobook.R;
import com.app.audiobook.adapter.ChapterAdapter;
import com.app.audiobook.component.Chapter;

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
        chapter1.setTitle("Предисловие");
        chapter1.setTime("5:59");
        chapter1.setListen(true);
        chapter1.setDownload(true);

        Chapter chapter2 = new Chapter();
        chapter2.setTitle("Колдун и прыгливый горшок");
        chapter2.setTime("14:51");
        chapter2.setListen(false);
        chapter2.setDownload(true);

        Chapter chapter3 = new Chapter();
        chapter3.setTitle("Фонтан феи Фортуны");
        chapter3.setTime("16:24");
        chapter3.setListen(false);
        chapter3.setDownload(false);

        adapter.getChapters().add(chapter1);
        adapter.getChapters().add(chapter2);
        adapter.getChapters().add(chapter3);

        recyclerView.setAdapter(adapter);
    }

}
