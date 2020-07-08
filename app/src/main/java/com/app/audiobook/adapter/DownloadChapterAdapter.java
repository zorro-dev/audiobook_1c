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
import com.app.audiobook.audio.book.Chapter;

import java.util.ArrayList;

public class DownloadChapterAdapter extends RecyclerView.Adapter {

    private ArrayList<Chapter> chapters = new ArrayList<>();

    private ArrayList<Integer> selection = new ArrayList<>();

    public ArrayList<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(ArrayList<Chapter> chapters) {
        this.chapters = chapters;

        for (int i = 0; i < chapters.size(); i ++) {
            selection.add(i);
        }
    }

    public ArrayList<Integer> getSelection() {
        return selection;
    }

    public void setSelection(ArrayList<Integer> selection) {
        this.selection = selection;
    }

    public ArrayList<Chapter> getSelectedChapter() {
        ArrayList<Chapter> selectedChapters = new ArrayList<>();

        for (int i = 0; i < selection.size(); i ++) {
            selectedChapters.add(chapters.get(i));
        }

        return selectedChapters;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView backgroundIcon;

        public ViewHolder(@NonNull View v) {
            super(v);

            title = v.findViewById(R.id.title);
            backgroundIcon = v.findViewById(R.id.icon_background);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selection.contains(getAdapterPosition())) {
                        selection.remove((Integer) getAdapterPosition());
                    } else {
                        selection.add(getAdapterPosition());
                    }

                    notifyItemChanged(getAdapterPosition());
                }
            });

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_chapter_download, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder h = (ViewHolder) holder;

        Chapter item = chapters.get(position);

        Resources res = h.itemView.getResources();

        h.title.setText(item.getName());

        if (selection.contains(position)) {
            h.backgroundIcon.setColorFilter(res.getColor(R.color.newColorOrange2));
        } else {
            h.backgroundIcon.setColorFilter(res.getColor(R.color.newColorBackgroundGray3));
        }
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }
}
