package com.app.audiobook.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.app.audiobook.BaseFragment;
import com.app.audiobook.R;
import com.app.audiobook.adapter.FilterAdapter;
import com.app.audiobook.adapter.TimerAdapter;
import com.app.audiobook.component.FilterParameter;
import com.app.audiobook.component.TimerLabel;
import com.app.audiobook.interfaces.ClickListener;
import com.app.audiobook.utils.PreferenceUtil;
import com.app.audiobook.ux.MainActivity;
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;

import java.util.ArrayList;

public class SettingFragment extends BaseFragment {

    View v;
    private int sec = 0;

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
        initTimerLayout();
        initStartPageLayout();
        initPreferencesList();
    }

    private void initPreferencesList() {
        RecyclerView recyclerView = v.findViewById(R.id.preferences_list);

        FilterAdapter filterAdapter = new FilterAdapter();
        filterAdapter.setFilterParameters(getPreferenceList());
        filterAdapter.selectAll();

        filterAdapter.setClickListener(new ClickListener() {
            @Override
            public void onClick(View v, int pos) {
                //TODO отправлять запрос на сохранение предпочтения на сервер
            }
        });

        recyclerView.setAdapter(filterAdapter);

        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(getContext())
                //set vertical gravity for all items in a row. Default = Gravity.CENTER_VERTICAL
                .setChildGravity(Gravity.TOP)
                .setScrollingEnabled(true)
                //set maximum views count in a particular row
                .setMaxViewsInRow(3)
                //set gravity resolver where you can determine gravity for item in position.
                //This method have priority over previous one
                .setGravityResolver(new IChildGravityResolver() {
                    @Override
                    public int getItemGravity(int position) {
                        return Gravity.CENTER;
                    }
                })
                //you are able to break row due to your conditions. Row breaker should return true for that views
                //a layoutOrientation of layout manager, could be VERTICAL OR HORIZONTAL. HORIZONTAL by default
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                // row strategy for views in completed row, could be STRATEGY_DEFAULT, STRATEGY_FILL_VIEW,
                //STRATEGY_FILL_SPACE or STRATEGY_CENTER
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                // whether strategy is applied to last row. FALSE by default
                .withLastRow(true)
                .build();
        recyclerView.setLayoutManager(chipsLayoutManager);
    }

    private ArrayList<FilterParameter> getPreferenceList() {
        ArrayList<FilterParameter> list = new ArrayList<>();

        list.add(new FilterParameter("1", "Фантастика"));
        list.add(new FilterParameter("2", "Детское"));
        list.add(new FilterParameter("3", "Путешествия"));
        list.add(new FilterParameter("4", "Аппокалипсис"));
        list.add(new FilterParameter("5", "Утопия"));
        list.add(new FilterParameter("6", "Фантастика"));
        list.add(new FilterParameter("7", "Детское"));
        list.add(new FilterParameter("8", "Путешествия"));
        list.add(new FilterParameter("9", "Аппокалипсис"));
        list.add(new FilterParameter("10", "Утопия"));
        list.add(new FilterParameter("11", "Фантастика"));
        list.add(new FilterParameter("12", "Детское"));
        list.add(new FilterParameter("13", "Путешествия"));
        list.add(new FilterParameter("14", "Аппокалипсис"));
        list.add(new FilterParameter("15", "Утопия"));

        return list;
    }

    private void initStartPageLayout() {
        int[] backgrounds = new int[]{
                R.id.background_nav_1,
                R.id.background_nav_2,
                R.id.background_nav_3,
                R.id.background_nav_4,
                R.id.background_nav_5,
        };

        int[] layouts = new int[]{
                R.id.start_page_1,
                R.id.start_page_2,
                R.id.start_page_3,
                R.id.start_page_4,
                R.id.start_page_5,
        };

        int startPage = PreferenceUtil.getStartPage(getContext());

        for (int i = 0; i < backgrounds.length; i++) {
            ImageView background = v.findViewById(backgrounds[i]);
            if (startPage == i) {
                background.setColorFilter(getResources().getColor(R.color.newColorBackgroundGray5));
            } else {
                background.setColorFilter(getResources().getColor(android.R.color.transparent));
            }

            LinearLayout layout = v.findViewById(layouts[i]);
            int finalI = i;
            layout.setOnClickListener(v1 -> {
                PreferenceUtil.setStartPage(getContext(), finalI);
                initStartPageLayout();
            });
        }


    }

    private void initTimerLayout() {
        initRecyclerViewTimerLabels();

        ConstraintLayout start = v.findViewById(R.id.start);
        start_background = v.findViewById(R.id.start_background);
        start_text = v.findViewById(R.id.start_text);

        if (getParent().stopPlayerTimer.isStarted()) {
            start_text.setText("Остановить таймер");
            start_background.setColorFilter(getResources().getColor(R.color.newColorOrange1));
        } else {
            start_text.setText("Включить таймер");
            start_background.setColorFilter(getResources().getColor(R.color.newColorBackgroundGray2));
        }

        start.setOnClickListener(v1 -> {
            if (getParent().stopPlayerTimer.isStarted()) {
                getParent().stopPlayerTimer.stopTimer();
                onTimerFinished();
            } else {
                if (sec == 0) {
                    Toast.makeText(getContext(), "Выберите время", Toast.LENGTH_SHORT).show();
                } else {
                    getParent().stopPlayerTimer.startTimer(sec);
                    start_text.setText("Остановить таймер");
                    start_background.setColorFilter(getResources().getColor(R.color.newColorOrange1));
                }
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

        TimerLabel timerLabel1 = new TimerLabel("", 600);
        TimerLabel timerLabel2 = new TimerLabel("", 1200);
        TimerLabel timerLabel3 = new TimerLabel("", 1800);
        TimerLabel timerLabel4 = new TimerLabel("", 2400);
        TimerLabel timerLabel5 = new TimerLabel("", 3000);
        TimerLabel timerLabel6 = new TimerLabel("", 3600);

        adapter.setClickListener((v, pos) -> {
            if (adapter.getSelectedTimerLabel() == -1) {
                sec = 0;
            } else {
                sec = adapter.getTimerLabels().get(pos).getTime();
            }

            getParent().stopPlayerTimer.setSelectedTimeIndex(adapter.getSelectedTimerLabel());
        });

        adapter.getTimerLabels().add(timerLabel1);
        adapter.getTimerLabels().add(timerLabel2);
        adapter.getTimerLabels().add(timerLabel3);
        adapter.getTimerLabels().add(timerLabel4);
        adapter.getTimerLabels().add(timerLabel5);
        adapter.getTimerLabels().add(timerLabel6);

        adapter.setSelectedTimerLabel(getParent().stopPlayerTimer.getSelectedTimeIndex());

        recyclerView.setAdapter(adapter);
    }

    public void onTimerFinished() {
        if (v != null) {
            ImageView start_background = v.findViewById(R.id.start_background);
            TextView start_text = v.findViewById(R.id.start_text);
            start_text.setText("Включить таймер");
            start_background.setColorFilter(getResources().getColor(R.color.newColorBackgroundGray2));

            RecyclerView recyclerView = v.findViewById(R.id.recyclerViewTimer);
            TimerAdapter adapter = (TimerAdapter) recyclerView.getAdapter();
            if (adapter != null) {
                adapter.setSelectedTimerLabel(getParent().stopPlayerTimer.getSelectedTimeIndex());
                adapter.notifyDataSetChanged();
            }
        }
    }

    private MainActivity getParent() {
        return (MainActivity) getActivity();
    }
}
