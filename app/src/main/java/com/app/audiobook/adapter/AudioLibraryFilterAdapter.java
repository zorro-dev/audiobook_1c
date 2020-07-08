package com.app.audiobook.adapter;

import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.audiobook.R;
import com.app.audiobook.component.FilterParameter;
import com.app.audiobook.interfaces.ClickListener;

import java.util.ArrayList;

public class AudioLibraryFilterAdapter extends RecyclerView.Adapter {

    private ArrayList<FilterParameter> filterParameters = new ArrayList<>();

    private ArrayList<Integer> selectedFilterParameters = new ArrayList<>();

    private ClickListener clickListener;

    public ArrayList<FilterParameter> getFilterParameters() {
        return filterParameters;
    }

    public void setFilterParameters(ArrayList<FilterParameter> filterParameters) {
        this.filterParameters = filterParameters;
    }

    public ArrayList<Integer> getSelectedFilterParameters() {
        return selectedFilterParameters;
    }

    public void setSelectedFilterParameters(ArrayList<Integer> selectedFilterParameters) {
        this.selectedFilterParameters = selectedFilterParameters;
    }

    public ClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void selectAll() {
        for (int i = 0; i < filterParameters.size(); i ++) {
            selectedFilterParameters.add(i);
        }
    }

    public ArrayList<FilterParameter> getSelectedParams() {
        ArrayList<FilterParameter> selectedParams = new ArrayList<>();

        for (int i = 0; i < filterParameters.size(); i ++) {
            if (selectedFilterParameters.contains(i)) {
                selectedParams.add(filterParameters.get(i));
            }
        }

        return selectedParams;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView colorSelected;

        public ViewHolder(@NonNull View v) {
            super(v);

            title = v.findViewById(R.id.title);
            colorSelected = v.findViewById(R.id.colorSelected);

            itemView.setOnClickListener(v1 -> {
                if(selectedFilterParameters.contains(getAdapterPosition())){
                    selectedFilterParameters.remove((Integer) getAdapterPosition());
                } else {
                    selectedFilterParameters.add(getAdapterPosition());
                }

                if (clickListener != null)
                    clickListener.onClick(v1, getAdapterPosition());

                notifyItemChanged(getAdapterPosition());
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_sort, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder h = (ViewHolder) holder;

        Resources res = h.itemView.getResources();

        FilterParameter item = filterParameters.get(position);

        h.title.setText(item.getTitle());

        if(selectedFilterParameters.contains(position)){
            h.colorSelected.setColorFilter(res.getColor(R.color.newColorOrange1));
        } else {
            h.colorSelected.setColorFilter(res.getColor(R.color.newColorBackgroundGray3));
        }
    }

    @Override
    public int getItemCount() {
        return filterParameters.size();
    }
}
