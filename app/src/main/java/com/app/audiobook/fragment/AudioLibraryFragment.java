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
import com.app.audiobook.component.AudioLibraryManager;

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
        initRecyclerView();
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewBook);
        AudioLibraryAdapter adapter = new AudioLibraryAdapter();

        adapter.setAudioBooks(AudioLibraryManager.getBookList(getContext()));

        recyclerView.setAdapter(adapter);
    }

}
