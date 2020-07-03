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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.app.audiobook.BaseFragment;
import com.app.audiobook.R;
import com.app.audiobook.adapter.ShopAdapter;
import com.app.audiobook.audio.book.AudioBook;
import com.app.audiobook.audio.catalog.CategoryListCatalog;
import com.app.audiobook.interfaces.ClickListener;
import com.app.audiobook.ux.MainActivity;

import java.util.ArrayList;

public class CategoryListFragment extends BaseFragment {

    View v;

    private CategoryListCatalog categoryListCatalog;
    private String categoryId;

    public CategoryListFragment(String categoryId) {
        this.categoryId = categoryId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_category_list, container, false);

        initInterface();

        categoryListCatalog = new CategoryListCatalog(categoryId);
        categoryListCatalog.load();
        categoryListCatalog.getCatalogLiveData().observe(this, new Observer<ArrayList<AudioBook>>() {
            @Override
            public void onChanged(ArrayList<AudioBook> audioBooks) {
                initRecyclerView(audioBooks);
            }
        });

        return v;
    }

    private void initInterface(){
        initSearchView();

        ConstraintLayout back = v.findViewById(R.id.back);
        back.setOnClickListener(v1 -> {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();

            fragmentTransaction.addToBackStack("CategoryListFragment");
            fragmentTransaction.remove(this).commit();
        });
    }

    private void initRecyclerView(ArrayList<AudioBook> audioBooks){
        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewCategoryBook);
        ShopAdapter adapter = new ShopAdapter();

        adapter.setAudioBooks(audioBooks);

        adapter.setClickListener(new ClickListener() {
            @Override
            public void onClick(View v, int pos) {
                //TODO нажатие на книгу
                ((MainActivity) getActivity()).initPurchaseFragment(audioBooks.get(pos));
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

                categoryListCatalog.updateListByQuery(query);
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
}
