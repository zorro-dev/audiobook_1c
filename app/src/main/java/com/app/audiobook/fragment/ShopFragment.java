package com.app.audiobook.fragment;

import android.content.Intent;
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
import com.app.audiobook.adapter.AudioLibraryFilterAdapter;
import com.app.audiobook.adapter.ShopAdapter;
import com.app.audiobook.audio.book.AudioBook;
import com.app.audiobook.audio.book.BookPrice;
import com.app.audiobook.audio.catalog.ShopCatalog;
import com.app.audiobook.audio.catalog.UserCatalog;
import com.app.audiobook.component.FilterParameter;
import com.app.audiobook.component.JSONManager;
import com.app.audiobook.interfaces.ClickListener;
import com.app.audiobook.ux.BookActivity;
import com.app.audiobook.ux.MainActivity;

import java.util.ArrayList;

import static com.app.audiobook.ux.MainActivity.BOOK_ACTIVITY_REQUEST;

public class ShopFragment extends BaseFragment {

    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_shop, container, false);

        initInterface();

        getCatalog().getCatalogLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<AudioBook>>() {
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
        ShopAdapter adapter = new ShopAdapter(getParent().userCatalog);

        adapter.setAudioBooks(audioBooks);

        adapter.setClickListener((v1, pos) -> {
            ((MainActivity) getActivity()).initPurchaseFragment(audioBooks.get(pos));
        });

        recyclerView.setAdapter(adapter);
    }

    private void initRecyclerViewFilterParameters(){
        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewFilter);
        AudioLibraryFilterAdapter adapter = new AudioLibraryFilterAdapter();

        FilterParameter filterParameter1 = new FilterParameter(BookPrice.TYPE_FREE, "Бесплатные");
        FilterParameter filterParameter2 = new FilterParameter(BookPrice.TYPE_USUAL_PRICE, "Платные");
        FilterParameter filterParameter3 = new FilterParameter(BookPrice.TYPE_DISCOUNT_PRICE, "Выгодные покупки");

        adapter.getFilterParameters().add(filterParameter1);
        adapter.getFilterParameters().add(filterParameter2);
        adapter.getFilterParameters().add(filterParameter3);

        adapter.selectAll();

        adapter.setClickListener(new ClickListener() {
            @Override
            public void onClick(View v, int pos) {
                getCatalog().updateListByFilter(
                        adapter.getSelectedParams());
            }
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

        /*searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.v("lol", "IME_ACTION_SEARCH");
                    getCatalog().updateListByQuery(searchEdit.getText().toString());

                    return true;
                }
                return false;
            }
        });*/

    }

    private ShopCatalog getCatalog() {
        return getParent().shopCatalog;
    }

    private MainActivity getParent() {
        return (MainActivity) getActivity();
    }

}
