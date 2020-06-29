package com.app.audiobook.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.app.audiobook.BaseFragment;
import com.app.audiobook.BookActivity;
import com.app.audiobook.R;
import com.app.audiobook.adapter.AudioLibraryAdapter;
import com.app.audiobook.adapter.AudioLibraryFilterAdapter;
import com.app.audiobook.adapter.ShopAdapter;
import com.app.audiobook.component.FilterParameter;
import com.app.audiobook.component.ShopManager;
import com.app.audiobook.ux.MainActivity;

public class ShopFragment extends BaseFragment {

    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_shop, container, false);

        initInterface();

        return v;
    }

    private void initInterface(){
        initRecyclerViewBooks();
        initRecyclerViewFilterParameters();
    }

    private void initRecyclerViewBooks(){
        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewBook);
        ShopAdapter adapter = new ShopAdapter();

        adapter.setAudioBooks(ShopManager.getBookList(getContext()));

        adapter.setClickListener((v1, pos) -> {
            ((MainActivity) getActivity()).initFragment();
        });

        recyclerView.setAdapter(adapter);
    }

    private void initRecyclerViewFilterParameters(){
        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewFilter);
        AudioLibraryFilterAdapter adapter = new AudioLibraryFilterAdapter();

        FilterParameter filterParameter1 = new FilterParameter();
        filterParameter1.setTitle("Бесплатные");

        FilterParameter filterParameter2 = new FilterParameter();
        filterParameter2.setTitle("Платные");

        FilterParameter filterParameter3 = new FilterParameter();
        filterParameter3.setTitle("Выгодные покупки");

        adapter.getFilterParameters().add(filterParameter1);
        adapter.getFilterParameters().add(filterParameter2);
        adapter.getFilterParameters().add(filterParameter3);

        recyclerView.setAdapter(adapter);
    }
}
