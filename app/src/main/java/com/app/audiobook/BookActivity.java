package com.app.audiobook;

import android.os.Bundle;

import com.app.audiobook.adapter.ChapterAdapter;
import com.app.audiobook.audio.Chapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class BookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        initInterface();
    }

    private void initInterface(){
        initRecyclerView();

        ConstraintLayout back = findViewById(R.id.back);
        back.setOnClickListener(v1 -> {
            onBackPressed();
        });
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recyclerViewBookActivity);
        ChapterAdapter adapter = new ChapterAdapter();

        /*Chapter chapter1 = new Chapter();
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

        recyclerView.setAdapter(adapter);*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
