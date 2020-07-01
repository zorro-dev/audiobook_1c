package com.app.audiobook.audio.catalog;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

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

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public abstract void load();

    void deliverResult(ArrayList<T> t) {
        catalogList = new ArrayList<>(t);
        getCatalogLiveData().postValue(t);
    }

    public void updateListByQuery(String query) {
        this.query = query;

        if (isQueryEmpty()) {
            updateListToDefault();
        } else {
            getCatalogLiveData().setValue(getFilteredListByQuery(query));
        }
    }

    public boolean isQueryEmpty() {
        return query == null || query.isEmpty();
    }

    private ArrayList<T> getFilteredListByQuery(String query) {
        ArrayList<T> filteredAudioBooks = new ArrayList<>();

        Log.v("lol", "size = " + String.valueOf(getCatalogList().size()));

        for (int i = 0; i < getCatalogList().size(); i ++) {
            Log.v("lol", "i = " + String.valueOf(i));
            if (isValidByQuery(getCatalogList().get(i), query)) {
                Log.v("lol", "isValidByQuery = true");
                filteredAudioBooks.add(getCatalogList().get(i));
            }
        }

        return filteredAudioBooks;
    }

    public abstract boolean isValidByQuery(T t, String query);

    public void updateListToDefault() {
        getCatalogLiveData().setValue(catalogList);
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
