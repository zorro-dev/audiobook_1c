package com.app.audiobook.audio.catalog;

import com.app.audiobook.audio.book.AudioBook;
import com.app.audiobook.audio.loader.UserCatalogLoader;
import com.app.audiobook.database.Loader;

import java.util.ArrayList;

public class UserCatalog extends Catalog<AudioBook> {

    private String userId;

    public UserCatalog(String userId) {
        this.userId = userId;
    }


    @Override
    public void load() {
        UserCatalogLoader loader = new UserCatalogLoader(userId);
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

    public boolean contains(AudioBook audioBook) {
        for (int i = 0; i < getCatalogList().size(); i ++) {
            if (getCatalogList().get(i).getId().equals(audioBook.getId())) {
                return true;
            }
        }

        return false;
    }

}
