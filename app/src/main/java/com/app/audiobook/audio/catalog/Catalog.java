package com.app.audiobook.audio.catalog;

import androidx.lifecycle.MutableLiveData;

import com.app.audiobook.audio.AudioBook;
import com.app.audiobook.database.Loader;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Queue;

public abstract class Catalog<T> {

    private String query;
    private ArrayList<T> catalogList = new ArrayList<>();
    private MutableLiveData<ArrayList<T>> catalogLiveData = new MutableLiveData<>();

    public ArrayList<T> getCatalogList() {
        return catalogList;
    }

    public void setCatalogList(ArrayList<T> catalogList) {
        this.catalogList = catalogList;
    }

    public MutableLiveData<ArrayList<T>> getCatalogLiveData() {
        return catalogLiveData;
    }

    public void setCatalogLiveData(MutableLiveData<ArrayList<T>> catalogLiveData) {
        this.catalogLiveData = catalogLiveData;
    }

    public abstract void load();

    public void deliverResult(ArrayList<T> t) {
        catalogList = t;
        getCatalogLiveData().postValue(catalogList);
    }

    public void updateListByQuery(String query) {
        this.query = query;

        if (isQueryEmpty()) {
            updateListToDefault();
        } else {
            getCatalogLiveData().postValue(getFilteredListByQuery(query));
        }
    }

    public boolean isQueryEmpty() {
        return query == null || query.isEmpty();
    }

    private ArrayList<T> getFilteredListByQuery(String query) {
        ArrayList<T> filteredAudioBooks = new ArrayList<>();

        for (int i = 0; i < getCatalogList().size(); i ++) {
            if (isValidByQuery(getCatalogList().get(i), query)) {
                filteredAudioBooks.add(getCatalogList().get(i));
            }
        }

        return filteredAudioBooks;
    }

    public abstract boolean isValidByQuery(T t, String query);

    public void updateListToDefault() {
        getCatalogLiveData().postValue(catalogList);
    }

    public void addToCatalog(T t) {
        catalogList.add(t);

        if (isQueryEmpty()) {
            updateListByQuery(query);
        } else {
            updateListToDefault();
        }
    }

    public void removeFromCatalog(T t) {
        catalogList.remove(t);

        if (isQueryEmpty()) {
            updateListByQuery(query);
        } else {
            updateListToDefault();
        }
    }

}
