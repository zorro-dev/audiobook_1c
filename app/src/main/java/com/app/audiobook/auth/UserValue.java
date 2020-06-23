package com.app.audiobook.auth;

import com.app.audiobook.database.DatabaseValue;
import com.app.audiobook.database.SyncListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class UserValue<T> extends DatabaseValue<T> {

    private User user;

    public UserValue(String name, User user) {
        super(name);
        this.user = user;
    }

    public UserValue(String name, User user, SyncListener syncListener) {
        super(name, syncListener);
        this.user = user;
    }

    @Override
    public Task<Void> syncWithDatabase() {
        if (getSyncListener() != null) {
            getSyncListener().onSync();
        }

        return FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(user.getId())
                .child(getName()).setValue(getValue());
    }


}
