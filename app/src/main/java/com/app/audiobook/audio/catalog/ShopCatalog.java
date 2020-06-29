package com.app.audiobook.audio.catalog;

import com.app.audiobook.audio.AudioBook;
import com.app.audiobook.audio.loader.BookCatalogLoader;
import com.app.audiobook.database.Loader;

import java.util.ArrayList;

public class ShopCatalog extends Catalog<AudioBook> {

    @Override
    public void load() {
        BookCatalogLoader loader = new BookCatalogLoader();
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
