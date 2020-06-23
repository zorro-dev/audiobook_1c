package com.app.audiobook.auth;

import com.app.audiobook.database.SyncListener;

public class UserIntegerValue extends UserValue<Integer> {


    public UserIntegerValue(String name, User user) {
        super(name, user);
    }

    public UserIntegerValue(String name, User user, SyncListener syncListener) {
        super(name, user, syncListener);
    }

    public UserIntegerValue addCount(int count) {
        setValue(getValue() + count);

        if (getValue() < 0) {
            setValue(0);
        }

        return this;
    }


}
