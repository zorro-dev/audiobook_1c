package com.app.audiobook.ux;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.audiobook.R;
import com.app.audiobook.adapter.ChapterAdapter;
import com.app.audiobook.audio.book.AudioBook;
import com.app.audiobook.component.JSONManager;
import com.app.audiobook.fragment.DownloadBookFragment;
import com.bumptech.glide.Glide;
import com.joooonho.SelectableRoundedImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import static com.app.audiobook.audio.book.BookPrice.TYPE_DISCOUNT_PRICE;
import static com.app.audiobook.audio.book.BookPrice.TYPE_FREE;
import static com.app.audiobook.audio.book.BookPrice.TYPE_USUAL_PRICE;

public class BookActivity extends AppCompatActivity {

    private AudioBook audioBook;
    private boolean transitionAfterShop = false;
    private boolean isUserHasBook = false;


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
        initDownloadButton();
        initBottomButton();
        initButton();

        ConstraintLayout back = findViewById(R.id.back);
        back.setOnClickListener(v1 -> {
            onBackPressed();
        });


    }

    private void initDownloadButton() {
        ConstraintLayout download = findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDownloadFragment(audioBook);
            }
        });
    }

    private void initBook(){
        SelectableRoundedImageView image = findViewById(R.id.book_cover);
        TextView title = findViewById(R.id.title);
        TextView author = findViewById(R.id.author);
        TextView count_parts = findViewById(R.id.count_parts);
        TextView text_description = findViewById(R.id.text_description);

        ImageView background_button = findViewById(R.id.background_button);
        TextView text_button = findViewById(R.id.text_button);
        ImageView image_button = findViewById(R.id.image_button);

        ConstraintLayout discount_layout = findViewById(R.id.discount_layout);
        TextView discount= findViewById(R.id.discount);

        ConstraintLayout isAdd = findViewById(R.id.isAdd);
        ImageView backgroundIsAdd = findViewById(R.id.backgroundIsAdd);
        ImageView imageIsAdd = findViewById(R.id.imageIsAdd);
        TextView textIsAdd = findViewById(R.id.textIsAdd);

        Glide.with(getBaseContext())
                .load(audioBook.getCoverUrl())
                .placeholder(R.drawable.ic_black_square)
                .into(image);

        title.setText(audioBook.getTitle());
        author.setText(audioBook.getAuthor().getName());
        count_parts.setText(audioBook.getChapterSize() + " частей");
        text_description.setText(audioBook.getDescription());


        if(transitionAfterShop){
            if (isUserHasBook) {
                image_button.setColorFilter(getResources().getColor(R.color.colorWhite));
                background_button.setColorFilter(getResources().getColor(R.color.newColorOrange1));
                image_button.setImageResource(R.drawable.ic_play);
                text_button.setText("Слушать");

                isAdd.setVisibility(View.VISIBLE);
                textIsAdd.setText("Уже в аудиотеке");
                imageIsAdd.setImageResource(R.drawable.ic_check);
                imageIsAdd.setColorFilter(getResources().getColor(R.color.colorOrange));
            } else if (audioBook.getBookPrice().getType().equals(TYPE_FREE)) {
                text_button.setText("Бесплатно");
                background_button.setColorFilter(getResources().getColor(R.color.colorFreePrice));
                text_button.setText("Забрать книгу");
                image_button.setImageResource(R.drawable.ic_shop_bag);
            } else if(audioBook.getBookPrice().getType().equals(TYPE_USUAL_PRICE)){
                background_button.setColorFilter(getResources().getColor(R.color.colorUsualPrice));
                discount_layout.setVisibility(View.GONE);
                text_button.setText(audioBook.getBookPrice().getPrice() + "$");
                image_button.setImageResource(R.drawable.ic_shop_bag);
            } else if (audioBook.getBookPrice().getType().equals(TYPE_DISCOUNT_PRICE)) {
                background_button.setColorFilter(getResources().getColor(R.color.colorDiscountPrice));
                image_button.setImageResource(R.drawable.ic_shop_bag);
                discount_layout.setVisibility(View.VISIBLE);
                text_button.setText(String.valueOf(Integer.parseInt(audioBook.getBookPrice().getPrice()) * audioBook.getBookPrice().getDiscount()) + "$");
                text_button.setTextColor(getResources().getColor(R.color.colorGreen_7));
                discount.setVisibility(View.VISIBLE);
                discount.setText(audioBook.getBookPrice().getPrice() + "$");
            }

        } else {
            image_button.setColorFilter(getResources().getColor(R.color.colorWhite));
            background_button.setColorFilter(getResources().getColor(R.color.newColorOrange1));
            image_button.setImageResource(R.drawable.ic_play);
            text_button.setText("Слушать");
        }

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
        if(getIntent().getStringExtra("path").equals("ShopFragment")){
            transitionAfterShop = true;
        }

        if(getIntent().getBooleanExtra("checkUserBook", false)){
            isUserHasBook = true;
        }
    }

    private void initButton(){
        ConstraintLayout listen = findViewById(R.id.buttonListen);

        if(isUserHasBook){
            listen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    String json = JSONManager.exportToJSON(audioBook);
                    intent.putExtra("audiobook", json);

                    setResult(RESULT_OK, intent);

                    finish();
                }
            });
        }

    }

    public void initDownloadFragment(AudioBook audioBook) {
        DownloadBookFragment fragment = new DownloadBookFragment(audioBook);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack("DownloadBookFragment");

        transaction.add(R.id.frame_layout, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
