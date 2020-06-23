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
import com.app.audiobook.component.Chapter;

import java.util.ArrayList;

public class ChapterAdapter extends RecyclerView.Adapter {

    ArrayList<Chapter> chapters = new ArrayList<>();

    public ArrayList<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(ArrayList<Chapter> chapters) {
        this.chapters = chapters;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView time;
        ImageView icon_download;
        ImageView icon_state;

        public ViewHolder(@NonNull View v) {
            super(v);

            title = v.findViewById(R.id.title);
            time = v.findViewById(R.id.time);
            icon_download = v.findViewById(R.id.icon_download);
            icon_state = v.findViewById(R.id.icon_state);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_chapter, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder h = (ViewHolder) holder;

        Resources res = h.itemView.getResources();

        Chapter item = chapters.get(position);

        h.title.setText(item.getTitle());
        h.time.setText(item.getTime());

        if(item.isDownload()){
            h.icon_download.setColorFilter(res.getColor(R.color.colorGreen_2));
        } else {
            h.icon_download.setColorFilter(res.getColor(R.color.colorGray_5));
        }

        if(item.isListen()){
            h.icon_state.setColorFilter(res.getColor(R.color.colorGreen_2));
        } else {
            h.icon_state.setColorFilter(res.getColor(R.color.colorGray_5));
        }
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }
}
