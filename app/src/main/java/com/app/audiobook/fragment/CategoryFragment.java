package com.app.audiobook.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.app.audiobook.BaseFragment;
import com.app.audiobook.R;
import com.app.audiobook.adapter.CategoryAdapter;
import com.app.audiobook.audio.book.Category;
import com.app.audiobook.interfaces.ClickListener;
import com.app.audiobook.ux.CategoryListActivity;
import com.app.audiobook.ux.MainActivity;

public class CategoryFragment extends BaseFragment {

    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_category, container, false);

        initInterface();

        return v;
    }

    private void initInterface(){
        initRecyclerView();


    }

    private void initRecyclerView(){
        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewCategory);
        CategoryAdapter adapter = new CategoryAdapter();

        adapter.getCategory().add(new Category("new_book","Новые\n книги"));
        adapter.getCategory().add(new Category("comlpete","Комлпекты"));
        adapter.getCategory().add(new Category("learn_foreign_languages","Изучаем\n иностранные\n языки"));
        adapter.getCategory().add(new Category("russian_classics","Русская\n классика"));
        adapter.getCategory().add(new Category("children","Детям"));

        adapter.getCategory().add(new Category("new_book_2","Новые\n книги"));
        adapter.getCategory().add(new Category("comlpete_2","Комлпекты"));
        adapter.getCategory().add(new Category("learn_foreign_languages_2","Изучаем\n иностранные\n языки"));
        adapter.getCategory().add(new Category("russian_classics_2","Русская\n классика"));
        adapter.getCategory().add(new Category("children_2","Детям"));

        adapter.setClickListener(new ClickListener() {
            @Override
            public void onClick(View v, int pos) {
                String categoryId = adapter.getCategory().get(pos).getId();
                //Intent intent = new Intent(getContext(), CategoryListActivity.class);
                //intent.putExtra("categoryId", categoryId);
                //startActivity(intent);
                ((MainActivity) getActivity()).initCategoryListFragment(categoryId);
            }
        });

        recyclerView.setAdapter(adapter);
    }
}
