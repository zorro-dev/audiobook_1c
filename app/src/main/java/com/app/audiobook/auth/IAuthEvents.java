package com.app.audiobook.auth;

public interface IAuthEvents {

    void onStartAuth();

    void onStartLoadingUser();

    void onAuthCompleted(User user);

    void onAuthCanceled();

}
