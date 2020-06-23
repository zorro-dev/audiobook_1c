package com.app.audiobook.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.app.audiobook.BaseFragment;
import com.app.audiobook.R;
import com.app.audiobook.adapter.AudioLibraryAdapter;
import com.app.audiobook.adapter.AudioLibraryFilterAdapter;
import com.app.audiobook.component.AudioLibraryManager;
import com.app.audiobook.component.FilterParameter;

public class AudioLibraryFragment extends BaseFragment {

    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_audio_library, container, false);

        initInterface();

        return v;
    }

    private void initInterface(){
        initRecyclerViewBooks();
        initRecyclerViewFilterParameters();
    }

    private void initRecyclerViewBooks(){
        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewBook);
        AudioLibraryAdapter adapter = new AudioLibraryAdapter();

        adapter.setAudioBooks(AudioLibraryManager.getBookList(getContext()));

        recyclerView.setAdapter(adapter);
    }

    private void initRecyclerViewFilterParameters(){
        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewFilter);
        AudioLibraryFilterAdapter adapter = new AudioLibraryFilterAdapter();

        FilterParameter filterParameter1 = new FilterParameter();
        filterParameter1.setTitle("Мои аудиокниги");

        FilterParameter filterParameter2 = new FilterParameter();
        filterParameter2.setTitle("Купленные книги");

        FilterParameter filterParameter3 = new FilterParameter();
        filterParameter3.setTitle("Подаренные книги");

        adapter.getFilterParameters().add(filterParameter1);
        adapter.getFilterParameters().add(filterParameter2);
        adapter.getFilterParameters().add(filterParameter3);

        recyclerView.setAdapter(adapter);
    }

}
