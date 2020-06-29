package com.app.audiobook.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.app.audiobook.BaseFragment;
import com.app.audiobook.BookActivity;
import com.app.audiobook.R;
import com.app.audiobook.adapter.AudioLibraryAdapter;
import com.app.audiobook.adapter.AudioLibraryFilterAdapter;
import com.app.audiobook.audio.AudioBook;
import com.app.audiobook.audio.AudioLibraryManager;
import com.app.audiobook.audio.catalog.UserCatalog;
import com.app.audiobook.component.FilterParameter;
import com.app.audiobook.ux.MainActivity;

import java.util.ArrayList;

public class AudioLibraryFragment extends BaseFragment {

    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_audio_library, container, false);

        initInterface();

        getParent().userCatalog.getCatalogLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<AudioBook>>() {
            @Override
            public void onChanged(ArrayList<AudioBook> audioBooks) {
                initRecyclerViewBooks(audioBooks);
            }
        });

        return v;
    }

    private void initInterface(){
        //initRecyclerViewBooks();
        initRecyclerViewFilterParameters();
        initSearchView();
    }

    private void initRecyclerViewBooks(ArrayList<AudioBook> audioBooks){
        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewBook);
        AudioLibraryAdapter adapter = new AudioLibraryAdapter();

        adapter.setAudioBooks(AudioLibraryManager.getBookList(getContext()));

        adapter.setClickListener((v1, pos) -> {
            Intent intent = new Intent(getContext(), BookActivity.class);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }

    private void initSearchView() {
        EditText searchEdit = v.findViewById(R.id.edit_text);

        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getParent().userCatalog.updateListByQuery(searchEdit.getText().toString());

                    return true;
                }
                return false;
            }
        });

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

    private MainActivity getParent() {
        return (MainActivity) getActivity();
    }

}
