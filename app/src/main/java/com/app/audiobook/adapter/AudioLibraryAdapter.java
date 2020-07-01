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
import com.app.audiobook.audio.service.book.AudioBook;
import com.app.audiobook.interfaces.ClickListener;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AudioLibraryAdapter extends RecyclerView.Adapter {

    private ArrayList<AudioBook> audioBooks = new ArrayList<>();
    private ClickListener clickListener;

    public ArrayList<AudioBook> getAudioBooks() {
        return audioBooks;
    }

    public void setAudioBooks(ArrayList<AudioBook> audioBooks) {
        this.audioBooks = audioBooks;
    }

    public ClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView author;
        TextView count_parts;
        ImageView cover;

        public ViewHolder(@NonNull View v) {
            super(v);

            title = v.findViewById(R.id.title);
            author = v.findViewById(R.id.author);
            count_parts = v.findViewById(R.id.count_parts);
            cover = v.findViewById(R.id.book_cover);

            itemView.setOnClickListener(v1 -> {
                if (clickListener != null)
                    clickListener.onClick(v1, getAdapterPosition());
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_book_audio_library, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder h = (ViewHolder) holder;

        Resources res = h.itemView.getResources();

        AudioBook item = audioBooks.get(position);

        h.title.setText(item.getTitle());
        h.author.setText(item.getAuthor().getName());
        h.count_parts.setText(String.valueOf(item.getChapterSize()));
        //h.cover.setImageResource(item.getCover());
        Glide.with(h.itemView)
                .load(item.getCoverUrl())
                .placeholder(R.drawable.ic_black_square)
                .into(h.cover);
    }

    @Override
    public int getItemCount() {
        return audioBooks.size();
    }
}
