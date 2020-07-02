package com.app.audiobook.ux;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.app.audiobook.R;
import com.app.audiobook.adapter.ShopAdapter;
import com.app.audiobook.audio.catalog.CategoryListCatalog;
import com.app.audiobook.audio.book.AudioBook;
import com.app.audiobook.interfaces.ClickListener;

import java.util.ArrayList;

public class CategoryListActivity extends AppCompatActivity {

    private CategoryListCatalog categoryListCatalog;

    private String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        getIntentData();

        categoryListCatalog = new CategoryListCatalog(categoryId);
        categoryListCatalog.load();
        categoryListCatalog.getCatalogLiveData().observe(this, new Observer<ArrayList<AudioBook>>() {
            @Override
            public void onChanged(ArrayList<AudioBook> audioBooks) {
                initRecyclerView(audioBooks);
            }
        });

        initInterface();
    }

    private void initInterface(){
        initSearchView();

        ConstraintLayout back = findViewById(R.id.back);
        back.setOnClickListener(v1 -> {
            onBackPressed();
        });
    }

    private void initSearchView() {
        EditText searchEdit = findViewById(R.id.edit_text);

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = searchEdit.getText().toString();

                categoryListCatalog.updateListByQuery(query);
            }
        });

        /*searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.v("lol", "IME_ACTION_SEARCH");
                    getCatalog().updateListByQuery(searchEdit.getText().toString());

                    return true;
                }
                return false;
            }
        });*/

    }

    private void initRecyclerView(ArrayList<AudioBook> audioBooks){
        RecyclerView recyclerView = findViewById(R.id.recyclerViewCategoryBook);
        ShopAdapter adapter = new ShopAdapter();

        adapter.setAudioBooks(audioBooks);

        adapter.setClickListener(new ClickListener() {
            @Override
            public void onClick(View v, int pos) {
                //TODO нажатие на книгу
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void getIntentData(){
        categoryId = getIntent().getStringExtra("categoryId");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
