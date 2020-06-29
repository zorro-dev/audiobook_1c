package com.app.audiobook.ux.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.audiobook.R;
import com.app.audiobook.auth.IAuthEvents;
import com.app.audiobook.auth.User;
import com.app.audiobook.ux.BaseActivity;
import com.app.audiobook.ux.MainActivity;


public class AuthActivity extends BaseActivity implements IAuthEvents {

    private int[] colors = new int[] {
            R.color.colorCategory1,
            R.color.colorCategory2,
            R.color.colorCategory3,
            R.color.colorCategory4,
            R.color.colorCategory5,
            R.color.colorCategory6,
            R.color.colorCategory7,
            R.color.colorCategory8,
    };

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
        initTextView();
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

    //Анимация текста
    TextView[] latters;

    private void initTextView() {
        latters = new TextView[]{
                findViewById(R.id.latter1),
                findViewById(R.id.latter2),
                findViewById(R.id.latter3),
                findViewById(R.id.latter4),
                findViewById(R.id.latter5),
                findViewById(R.id.latter6),
                findViewById(R.id.latter7),
                findViewById(R.id.latter8),
                findViewById(R.id.latter9),
        };

        doSomething(0, latters.length);
    }

    private void doSomething(int i, int n) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (i < n) {
                    runTextAnim(latters[i]);
                    //latters[i].setTextColor(getResources().getColor(R.color.colorOrange));
                    latters[i].setTextColor(getResources().getColor(colors[i -  (i / colors.length) * colors.length ]));
                    doSomething(i + 1, n);
                }
            }
        }, 150);
    }

    private void runTextAnim(TextView latter) {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.translate);
        a.reset();
        latter.clearAnimation();
        latter.startAnimation(a);
    }
}
