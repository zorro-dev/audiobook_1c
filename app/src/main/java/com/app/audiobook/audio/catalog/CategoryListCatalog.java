package com.app.audiobook.audio.catalog;

import com.app.audiobook.audio.loader.CategoryListLoader;
import com.app.audiobook.audio.book.AudioBook;
import com.app.audiobook.database.Loader;

import java.util.ArrayList;

public class CategoryListCatalog extends Catalog<AudioBook>{

    private String categoryId;

    public CategoryListCatalog(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public void load() {
        CategoryListLoader loader = new CategoryListLoader(categoryId);
        loader.load(new Loader.OnLoadListener<ArrayList<AudioBook>>() {
            @Override
            public void onLoaded(ArrayList<AudioBook> audioBooks) {
                deliverResult(audioBooks);
            }
        });

    }

    @Override
    public boolean isValidByQuery(AudioBook audioBook, String query) {
        boolean isNameValid = audioBook.getTitle().toLowerCase().contains(query.toLowerCase());
        boolean isAuthorValid = audioBook.getAuthor().getName().toLowerCase().contains(query.toLowerCase());

        return isNameValid || isAuthorValid;
    }

}
