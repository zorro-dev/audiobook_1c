package com.app.audiobook.database;

public abstract class Loader<T> {

    private T t;

    public T getResult() {
        return t;
    }


    public interface OnLoadListener<T> {
        void onLoaded(T t);
    }

    private OnLoadListener<T> loadListener;

    public OnLoadListener<T> getLoadListener() {
        return loadListener;
    }

    public void setLoadListener(OnLoadListener<T> loadListener) {
        this.loadListener = loadListener;
    }

    protected void deliverResult(T t) {
        this.t = t;
        if (loadListener != null) {
            loadListener.onLoaded(t);
        }
    }

    public abstract void load();

    public void load(OnLoadListener<T> loadListener) {
        setLoadListener(loadListener);
        load();
    }

}
