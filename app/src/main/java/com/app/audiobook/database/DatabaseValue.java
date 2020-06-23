package com.app.audiobook.database;

import com.google.android.gms.tasks.Task;

public abstract class DatabaseValue<T>{

    private String name;
    private T value;
    private SyncListener syncListener;

    public DatabaseValue(String name) {
        this.name = name;
    }

    public DatabaseValue(String name, SyncListener syncListener) {
        this.name = name;
        this.syncListener = syncListener;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public DatabaseValue setValue(T value) {
        this.value = value;

        return this;
    }

    public SyncListener getSyncListener() {
        return syncListener;
    }

    public void setSyncListener(SyncListener syncListener) {
        this.syncListener = syncListener;
    }

    public abstract Task<Void> syncWithDatabase();


}
