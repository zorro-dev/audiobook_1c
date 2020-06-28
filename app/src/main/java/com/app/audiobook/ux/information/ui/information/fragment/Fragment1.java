package com.app.audiobook.ux.information.ui.information.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.audiobook.BaseFragment;
import com.app.audiobook.R;

public class Fragment1 extends BaseFragment {

    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_information, container, false);

        initInterface();

        return v;
    }

    private void initInterface(){
        TextView text = v.findViewById(R.id.text);
        text.setText("Данное приложение предназначено для прослушивания аудиокниг");

        ImageView image = v.findViewById(R.id.image);
        image.setImageResource(R.drawable.icon1);
    }

}
