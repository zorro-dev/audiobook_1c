package com.app.audiobook.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.audiobook.R;
import com.app.audiobook.audio.book.Bookmark;
import com.app.audiobook.interfaces.ClickListener;

import java.util.ArrayList;

public class BookmarkAdapter extends RecyclerView.Adapter {

    private ArrayList<Bookmark> bookmarks = new ArrayList<>();

    private ClickListener clickListener;
    private ClickListener longClickListener;

    public ArrayList<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(ArrayList<Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public ClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public ClickListener getLongClickListener() {
        return longClickListener;
    }

    public void setLongClickListener(ClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView time;

        public ViewHolder(@NonNull View v) {
            super(v);

            title = v.findViewById(R.id.title);
            time = v.findViewById(R.id.time);

            itemView.setOnClickListener(v1 -> {
                if (clickListener != null)
                    clickListener.onClick(v1, getAdapterPosition());
            });

            itemView.setOnLongClickListener(v1 -> {

                if (longClickListener != null) {
                    longClickListener.onClick(v1, getAdapterPosition());
                }

                return false;
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_bookmark, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder h = (ViewHolder) holder;

        Bookmark item = bookmarks.get(position);

        h.title.setText(item.getTimeStamp());
        h.time.setText(getFormattedTime(item.getDurationInSeconds()));
    }

    public static String getFormattedTime(int time) {

        int minutes = time / 60;

        int seconds = time - (60 * minutes);

        String minutesStr = String.valueOf(minutes);
        String secondsStr;

        if (seconds < 10) {
            secondsStr = "0" + String.valueOf(seconds);
        } else {
            secondsStr = String.valueOf(seconds);
        }

        return minutesStr + ":" + secondsStr;
    }

    @Override
    public int getItemCount() {
        return bookmarks.size();
    }
}
