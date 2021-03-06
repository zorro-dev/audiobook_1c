package com.app.audiobook.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.app.audiobook.BaseFragment;
import com.app.audiobook.R;
import com.app.audiobook.adapter.AudioLibraryAdapter;
import com.app.audiobook.adapter.FilterAdapter;
import com.app.audiobook.audio.book.AudioBook;
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

        adapter.setAudioBooks(audioBooks);

        adapter.setClickListener((v1, pos) -> {
            ((MainActivity) getActivity()).initPurchaseFragment(audioBooks.get(pos));
            //Intent intent = new Intent(getContext(), BookActivity.class);
            //
            //String gson = JSONManager.exportToJSON(adapter.getAudioBooks().get(pos));
            //
            //intent.putExtra("audioBook", gson);
            //intent.putExtra("path", "AudioLibraryFragment");
            //intent.putExtra("checkUserBook", false);
            //getParent().startActivityForResult(intent, BOOK_ACTIVITY_REQUEST);
        });

        recyclerView.setAdapter(adapter);
    }

    private void initSearchView() {
        EditText searchEdit = v.findViewById(R.id.edit_text);

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = searchEdit.getText().toString();

                getCatalog().updateListByQuery(query);
            }
        });

    }

    private void initRecyclerViewFilterParameters(){
        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewFilter);
        FilterAdapter adapter = new FilterAdapter();

        FilterParameter filterParameter1 = new FilterParameter("", "Мои аудиокниги");
        FilterParameter filterParameter2 = new FilterParameter("", "Купленные книги");
        FilterParameter filterParameter3 = new FilterParameter("", "Подаренные книги");

        adapter.getFilterParameters().add(filterParameter1);
        adapter.getFilterParameters().add(filterParameter2);
        adapter.getFilterParameters().add(filterParameter3);

        recyclerView.setAdapter(adapter);

    }

    private UserCatalog getCatalog() {
        return getParent().userCatalog;
    }

    private MainActivity getParent() {
        return (MainActivity) getActivity();
    }

}
