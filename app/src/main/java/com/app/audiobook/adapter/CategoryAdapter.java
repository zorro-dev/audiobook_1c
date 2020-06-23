package com.app.audiobook.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.audiobook.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter {

    private ArrayList<String> categoryList = new ArrayList<>();

    private int[] colors = new int[] {
        R.color.colorCategory1,
        R.color.colorCategory2,
        R.color.colorCategory3,
        R.color.colorCategory4,
        R.color.colorCategory5,
        R.color.colorCategory6,
        R.color.colorCategory7,
        R.color.colorCategory8,
    };

    public ArrayList<String> getCategory() {
        return categoryList;
    }

    public void setCategory(ArrayList<String> categoryList) {
        this.categoryList = categoryList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView background;

        public ViewHolder(@NonNull View v) {
            super(v);

            title = v.findViewById(R.id.title);
            background = v.findViewById(R.id.background);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_category, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder h = (ViewHolder) holder;

        Resources res = h.itemView.getResources();

        String item = categoryList.get(position);

        h.title.setText(item);
        h.background.setColorFilter(res.getColor(colors[position -  (position / colors.length) * colors.length ]));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
