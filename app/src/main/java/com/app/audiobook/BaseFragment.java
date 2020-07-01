package com.app.audiobook;

import androidx.fragment.app.Fragment;

import com.app.audiobook.auth.AuthManager;

public class BaseFragment extends Fragment {

    public AuthManager getAuthManager() {
        return App.authManager;
    }

}
