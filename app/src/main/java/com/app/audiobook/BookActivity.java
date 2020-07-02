package com.app.audiobook;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.audiobook.adapter.ChapterAdapter;
import com.app.audiobook.audio.book.AudioBook;
import com.app.audiobook.component.JSONManager;
import com.bumptech.glide.Glide;
import com.joooonho.SelectableRoundedImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class BookActivity extends AppCompatActivity {

    private AudioBook audioBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        getIntentData();

        initInterface();
    }

    private void initInterface(){
        initRecyclerView();
        initBook();
        initBottomButton();
        initButtonListen();

        ConstraintLayout back = findViewById(R.id.back);
        back.setOnClickListener(v1 -> {
            onBackPressed();
        });
    }

    private void initBook(){
        SelectableRoundedImageView image = findViewById(R.id.book_cover);
        TextView title = findViewById(R.id.title);
        TextView author = findViewById(R.id.author);
        TextView count_parts = findViewById(R.id.count_parts);
        TextView text_description = findViewById(R.id.text_description);

        Glide.with(getBaseContext())
                .load(audioBook.getCoverUrl())
                .placeholder(R.drawable.ic_black_square)
                .into(image);

        title.setText(audioBook.getTitle());
        author.setText(audioBook.getAuthor().getName());
        count_parts.setText(audioBook.getChapterSize() + " частей");
        text_description.setText(audioBook.getDescription());
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recyclerViewBookActivity);
        ChapterAdapter adapter = new ChapterAdapter();

        adapter.setChapters(audioBook.getChapters());

        recyclerView.setAdapter(adapter);
    }

    private void initBottomButton(){
        ConstraintLayout buttonDescription = findViewById(R.id.description);
        ConstraintLayout buttonChapters = findViewById(R.id.chapter);

        ImageView selector_description = findViewById(R.id.selector_description);
        ImageView selector_chapter = findViewById(R.id.selector_chapter);

        LinearLayout layout_description = findViewById(R.id.layout_description);
        RecyclerView layout_chapter = findViewById(R.id.recyclerViewBookActivity);

        buttonDescription.setOnClickListener(v1 -> {
            selector_description.setVisibility(View.VISIBLE);
            selector_chapter.setVisibility(View.INVISIBLE);
            //Открытие описания
            layout_description.setVisibility(View.VISIBLE);
            layout_chapter.setVisibility(View.INVISIBLE);
        });

        buttonChapters.setOnClickListener(v1 -> {
            selector_description.setVisibility(View.INVISIBLE);
            selector_chapter.setVisibility(View.VISIBLE);
            //Открытие глав
            layout_description.setVisibility(View.INVISIBLE);
            layout_chapter.setVisibility(View.VISIBLE);
        });
    }

    private void getIntentData(){
        String gson = getIntent().getStringExtra("audioBook");
        audioBook = JSONManager.importFromJSON(gson, AudioBook.class);
    }

    private void initButtonListen(){

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
