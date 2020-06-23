package com.app.audiobook.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.audiobook.BaseFragment;
import com.app.audiobook.R;

public class SettingFragment extends BaseFragment {

    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_setting, container, false);

        initInterface();

        return v;
    }

    private void initInterface(){
        TextView text = v.findViewById(R.id.text);

        text.setText("Настройки");
    }

}
