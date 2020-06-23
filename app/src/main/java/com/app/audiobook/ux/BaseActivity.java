package com.app.audiobook.ux;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.audiobook.App;
import com.app.audiobook.auth.AuthManager;
import com.app.audiobook.ux.auth.AuthActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getAuthManager().getUser() == null) {

            if (this instanceof AuthActivity) {

            } else if (this instanceof MainActivity) {
                Intent intent = new Intent(this, AuthActivity.class);
                startActivity(intent);
                System.exit(0);
                finish();
            } else {
                System.exit(0);
                finish();
            }
        }
    }

    public AuthManager getAuthManager() {
        return App.authManager;
    }

}
