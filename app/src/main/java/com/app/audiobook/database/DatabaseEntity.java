package com.app.audiobook.database;

import com.google.android.gms.tasks.Task;

import java.util.Map;

public abstract class DatabaseEntity {

    public DatabaseEntity() {
    }

    public DatabaseEntity(Map<String, Object> entityMap) {
    }

    public abstract Task<Void> syncWithDatabase();

    public abstract Map getEntityMap();

}
