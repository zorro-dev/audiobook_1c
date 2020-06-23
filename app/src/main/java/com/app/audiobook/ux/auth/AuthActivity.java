package com.app.audiobook.ux.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.audiobook.R;
import com.app.audiobook.auth.IAuthEvents;
import com.app.audiobook.auth.User;
import com.app.audiobook.ux.BaseActivity;
import com.app.audiobook.ux.MainActivity;


public class AuthActivity extends BaseActivity implements IAuthEvents {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        getAuthManager().setEvents(this);

        initInterface();

        getAuthManager().initUser();
    }

    private void initInterface(){
        ConstraintLayout button = findViewById(R.id.buttonAuth);
        button.setOnClickListener(v1 -> {
            getAuthManager().startAuthWithGoogle(this);
        });
    }

    @Override
    public void onStartAuth() {
        Log.v("lol", "onStartAuth");
        showProgress();
    }

    @Override
    public void onStartLoadingUser() {
        Log.v("lol", "onStartLoadingUser");
        showProgress();
    }

    @Override
    public void onAuthCompleted(User user) {
        Log.v("lol", "onAuthCompleted");
        hideProgress();

        if (user.getName().getValue().length() == 0) {
            //Intent intent = new Intent(this, EnterUserDataActivity.class);
            //startActivity(intent);
            //finish();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onAuthCanceled() {
        Log.v("lol", "onAuthCanceled");
        hideProgress();
    }

    @Override
    protected void onDestroy() {
        getAuthManager().setEvents(null);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getAuthManager().onActivityResult(requestCode, resultCode, data);
    }

    private void showProgress() {
        View progress = findViewById(R.id.progress_layout);
        progress.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        View progress = findViewById(R.id.progress_layout);
        progress.setVisibility(View.GONE);
    }

}
