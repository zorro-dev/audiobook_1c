package com.app.audiobook;

import android.app.Application;

import com.app.audiobook.auth.AuthManager;

public class App extends Application {

    public static AuthManager authManager;

    @Override
    public void onCreate() {
        super.onCreate();

        if (authManager == null) {
            authManager = new AuthManager(getBaseContext());
        }
    }

}
