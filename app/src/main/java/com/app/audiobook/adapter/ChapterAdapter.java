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
import com.app.audiobook.interfaces.ClickListener;

import java.util.ArrayList;

import static com.app.audiobook.audio.book.Chapter.STATE_NOT_READ;
import static com.app.audiobook.audio.book.Chapter.STATE_READ;

public class ChapterAdapter extends RecyclerView.Adapter {

    ArrayList<Chapter> chapters = new ArrayList<>();

    ClickListener clickListener;

    public ArrayList<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(ArrayList<Chapter> chapters) {
        this.chapters = chapters;
    }

    public ClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onClick(v, getAdapterPosition());
                    }
                }
            });
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

        h.title.setText(item.getName());
        h.time.setText(getFormattedTime(item.getDurationInSeconds()));

        if(item.isCached()){
            h.icon_download.setColorFilter(res.getColor(R.color.newColorOrange1));
        } else {
            h.icon_download.setColorFilter(res.getColor(R.color.newColorBackgroundWhite));
        }


        if(item.getState().equals(STATE_READ)){
            h.icon_state.setColorFilter(res.getColor(R.color.newColorOrange1));
        } else if(item.getState().equals(STATE_NOT_READ)){
            h.icon_state.setColorFilter(res.getColor(R.color.newColorBackgroundWhite));
        }

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
        return chapters.size();
    }
}
