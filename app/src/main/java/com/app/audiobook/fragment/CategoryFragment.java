package com.app.audiobook.fragment;

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

        adapter.getCategory().add("Новые\n книги");
        adapter.getCategory().add("Комлпекты");
        adapter.getCategory().add("Изучаем\n иностранные\n языки");
        adapter.getCategory().add("Русская\n классика");
        adapter.getCategory().add("Детям");

        adapter.getCategory().add("Новые\n книги");
        adapter.getCategory().add("Комлпекты");
        adapter.getCategory().add("Изучаем\n иностранные\n языки");
        adapter.getCategory().add("Русская\n классика");
        adapter.getCategory().add("Детям");

        recyclerView.setAdapter(adapter);
    }
}
