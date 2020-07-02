package com.app.audiobook.audio.catalog;

import android.util.Log;

import com.app.audiobook.audio.book.AudioBook;
import com.app.audiobook.audio.loader.BookCatalogLoader;
import com.app.audiobook.component.FilterParameter;
import com.app.audiobook.database.Loader;

import java.util.ArrayList;

public class ShopCatalog extends Catalog<AudioBook> {

    @Override
    public void load() {
        BookCatalogLoader loader = new BookCatalogLoader();
        loader.load(new Loader.OnLoadListener<ArrayList<AudioBook>>() {
            @Override
            public void onLoaded(ArrayList<AudioBook> audioBooks) {
                Log.v("lol", "audioBooks size = " + String.valueOf(audioBooks.size()));
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

    public void updateListByFilter(ArrayList<FilterParameter> params) {
        updateListByQuery(getQuery());

        ArrayList<AudioBook> filteredList = new ArrayList<>();

        ArrayList<String> filters = new ArrayList<>();

        for (int i = 0; i < params.size(); i ++) {
            filters.add(params.get(i).getId());
        }

        for (int i = 0; i < getCatalogLiveData().getValue().size(); i ++) {
            AudioBook audioBook = getCatalogLiveData().getValue().get(i);
            String priceType = audioBook.getBookPrice().getType();
            if (filters.contains(priceType)) {
                filteredList.add(audioBook);
            }
        }

        getCatalogLiveData().setValue(filteredList);
    }

}
