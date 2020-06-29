package com.app.audiobook.adapter;

import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.app.audiobook.R;
import com.app.audiobook.audio.AudioBook;
import com.app.audiobook.interfaces.ClickListener;

import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter {

    ArrayList<AudioBook> audioBooks = new ArrayList<>();

    ClickListener clickListener;

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
        TextView price;
        TextView discount;

        ConstraintLayout discount_layout;
        ImageView layout_color;

        public ViewHolder(@NonNull View v) {
            super(v);

            title = v.findViewById(R.id.title);
            author = v.findViewById(R.id.author);
            count_parts = v.findViewById(R.id.count_parts);
            cover = v.findViewById(R.id.book_cover);
            price = v.findViewById(R.id.price);
            discount = v.findViewById(R.id.discount);

            discount_layout = v.findViewById(R.id.discount_layout);
            layout_color = v.findViewById(R.id.layout_color);

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
                .inflate(R.layout.cell_book_shop, parent, false);

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
        h.cover.setImageResource(item.getCover());

        if(item.getPriceBook().getType().equals("TYPE_FREE")){
            h.price.setText("Бесплатно");
        } else {
            h.price.setText(item.getPriceBook().getPrice() + "$");
        }

        if(item.getPriceBook().getDiscount() == 0){
            h.discount_layout.setVisibility(View.GONE);
        } else {
            h.discount_layout.setVisibility(View.VISIBLE);
            h.price.setText(String.valueOf(Integer.parseInt(item.getPriceBook().getPrice()) * item.getPriceBook().getDiscount()) + "$");
            h.price.setTextColor(res.getColor(R.color.colorGreen_5));
            h.discount.setText(item.getPriceBook().getPrice() + "$");
        }

        if(item.getPriceBook().getType().equals("TYPE_FREE")){
            h.layout_color.setColorFilter(res.getColor(R.color.colorFreePrice));
        } else if(item.getPriceBook().getType().equals("TYPE_USUAL_PRICE")){
            h.layout_color.setColorFilter(res.getColor(R.color.colorUsualPrice));
        } else if(item.getPriceBook().getType().equals("TYPE_DISCOUNT_PRICE")){
            h.layout_color.setColorFilter(res.getColor(R.color.colorDiscountPrice));
        }
    }

    @Override
    public int getItemCount() {
        return audioBooks.size();
    }
}
