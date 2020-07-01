package com.app.audiobook.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.app.audiobook.BaseFragment;
import com.app.audiobook.R;
import com.app.audiobook.adapter.TimerAdapter;
import com.app.audiobook.component.TimerLabel;
import com.app.audiobook.interfaces.ClickListener;

public class SettingFragment extends BaseFragment {

    View v;
    private int sec = 0;
    private int lastSelectedPos = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_setting, container, false);

        initInterface();

        return v;
    }

    private ImageView start_background;
    private TextView start_text;

    private void initInterface() {
        initUserValues();
        initRecyclerViewTimerLabels();

        ConstraintLayout start = v.findViewById(R.id.start);
        start_background = v.findViewById(R.id.start_background);
        start_text = v.findViewById(R.id.start_text);

        start.setOnClickListener(v1 -> {
            if(sec == 0){
                Toast.makeText(getContext(), "Выберите время", Toast.LENGTH_SHORT).show();
            } else {
                startTimer();
                start_text.setText("Таймер запущен");
                start_background.setColorFilter(getResources().getColor(R.color.colorGreen_2));
            }
        });
    }

    private void initUserValues() {
        TextView name = v.findViewById(R.id.name);
        TextView mail = v.findViewById(R.id.mail);
        TextView authType = v.findViewById(R.id.authType);

        name.setText(String.valueOf(getAuthManager().getUser().getName().getValue()));
        mail.setText(String.valueOf(getAuthManager().getUser().getMail().getValue()));
        authType.setText(String.valueOf("Вы зарегистрированны с помощью " + getAuthManager().getUser().getAuthType().getValue()));
    }

    private void initRecyclerViewTimerLabels() {
        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewTimer);
        TimerAdapter adapter = new TimerAdapter();

        TimerLabel timerLabel1 = new TimerLabel("", 10);
        TimerLabel timerLabel2 = new TimerLabel("", 120);
        TimerLabel timerLabel3 = new TimerLabel("", 360);
        TimerLabel timerLabel4 = new TimerLabel("", 660);
        TimerLabel timerLabel5 = new TimerLabel("", 1200);
        TimerLabel timerLabel6 = new TimerLabel("", 4600);

        adapter.setClickListener((v, pos) -> {
            if(lastSelectedPos != pos){
                sec = adapter.getTimerLabels().get(pos).getTime();
                lastSelectedPos = pos;
            } else {
                lastSelectedPos = -1;
                sec = 0;
            }
        });

        adapter.getTimerLabels().add(timerLabel1);
        adapter.getTimerLabels().add(timerLabel2);
        adapter.getTimerLabels().add(timerLabel3);
        adapter.getTimerLabels().add(timerLabel4);
        adapter.getTimerLabels().add(timerLabel5);
        adapter.getTimerLabels().add(timerLabel6);

        recyclerView.setAdapter(adapter);
    }

    private void startTimer(){
        new Handler().postDelayed(() -> {
            //TODO Закрытие ...
            Toast.makeText(getContext(), "Таймер выкл", Toast.LENGTH_SHORT).show();
            finishTimer();
        }, sec * 1000);
    }

    private void finishTimer(){
        start_text.setText("Включить таймер");
        start_background.setColorFilter(getResources().getColor(R.color.colorGray_1));
    }
}
