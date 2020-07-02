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
import com.app.audiobook.component.TimerLabel;
import com.app.audiobook.interfaces.ClickListener;

import java.util.ArrayList;

public class TimerAdapter extends RecyclerView.Adapter {

    private ArrayList<TimerLabel> timerLabels = new ArrayList<>();

    private int selectedTimerLabel = -1;
    private int lastSelectedTimerLabel = -1;

    ClickListener clickListener;

    public ArrayList<TimerLabel> getTimerLabels() {
        return timerLabels;
    }

    public void setTimerLabels(ArrayList<TimerLabel> timerLabels) {
        this.timerLabels = timerLabels;
    }

    public ClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public int getSelectedTimerLabel() {
        return selectedTimerLabel;
    }

    public void setSelectedTimerLabel(int selectedTimerLabel) {
        this.selectedTimerLabel = selectedTimerLabel;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView time;
        ImageView colorSelected;

        public ViewHolder(@NonNull View v) {
            super(v);

            time = v.findViewById(R.id.title);
            colorSelected = v.findViewById(R.id.colorSelected);

            itemView.setOnClickListener(v1 -> {

                if (selectedTimerLabel == -1) {
                    selectedTimerLabel = getAdapterPosition();
                } else if (selectedTimerLabel == getAdapterPosition()) {
                    lastSelectedTimerLabel = selectedTimerLabel;
                    selectedTimerLabel = -1;
                } else {
                    lastSelectedTimerLabel = selectedTimerLabel;
                    selectedTimerLabel = getAdapterPosition();
                }

                if (clickListener != null)
                    clickListener.onClick(v1, getAdapterPosition());

                notifyItemChanged(getAdapterPosition());
                notifyItemChanged(lastSelectedTimerLabel);
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_timer, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder h = (ViewHolder) holder;

        Resources res = h.itemView.getResources();

        TimerLabel item = timerLabels.get(position);

        h.time.setText((item.getTime() / 60) + " мин");

        if (selectedTimerLabel == position) {
            h.colorSelected.setColorFilter(res.getColor(R.color.colorGreen_4));
        }

        if (selectedTimerLabel != position) {
            h.colorSelected.setColorFilter(res.getColor(R.color.colorGray_1));
        }
    }

    @Override
    public int getItemCount() {
        return timerLabels.size();
    }
}
