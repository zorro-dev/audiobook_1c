package com.app.audiobook.adapter;

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
import com.app.audiobook.audio.book.AudioBook;
import com.app.audiobook.audio.catalog.UserCatalog;
import com.app.audiobook.interfaces.ClickListener;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter {

    ArrayList<AudioBook> audioBooks = new ArrayList<>();

    ClickListener clickListener;

    UserCatalog userCatalog;

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

    public void setUserCatalog(UserCatalog userCatalog) {
        this.userCatalog = userCatalog;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView author;
        TextView count_parts;
        ImageView cover;
        TextView price;
        TextView discount;
        ImageView price_img;

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
            price_img = v.findViewById(R.id.price_img);

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
        h.author.setText(String.valueOf("Автор: " + item.getAuthor().getName()));
        h.count_parts.setText(String.valueOf("Частей: " + item.getChapterSize()));
        Glide.with(h.itemView)
                .load(item.getCoverUrl())
                .placeholder(R.drawable.ic_black_square)
                .into(h.cover);



        if (userCatalog.contains(item)) {
            h.price_img.setImageResource(R.drawable.ic_check);
            h.price_img.setColorFilter(res.getColor(R.color.colorOrange));
            h.layout_color.setColorFilter(res.getColor(R.color.colorGray_3));
            h.price.setText("Уже в аудиотеке");
            h.discount_layout.setVisibility(View.GONE);
        } else if (item.getBookPrice() != null) {
            if (item.getBookPrice().getType().equals("TYPE_FREE")) {
                h.price.setText("Бесплатно");
            } else {
                h.price.setText(item.getBookPrice().getPrice() + "$");
            }

            if (item.getBookPrice().getDiscount() == 0) {
                h.discount_layout.setVisibility(View.GONE);
            } else {
                h.discount_layout.setVisibility(View.VISIBLE);
                h.price.setText(String.valueOf(Integer.parseInt(item.getBookPrice().getPrice()) * item.getBookPrice().getDiscount()) + "$");
                h.price.setTextColor(res.getColor(R.color.colorGreen_5));
                h.discount.setText(item.getBookPrice().getPrice() + "$");
            }

            if (item.getBookPrice().getType().equals("TYPE_FREE")) {
                h.layout_color.setColorFilter(res.getColor(R.color.colorFreePrice));
            } else if (item.getBookPrice().getType().equals("TYPE_USUAL_PRICE")) {
                h.layout_color.setColorFilter(res.getColor(R.color.colorUsualPrice));
            } else if (item.getBookPrice().getType().equals("TYPE_DISCOUNT_PRICE")) {
                h.layout_color.setColorFilter(res.getColor(R.color.colorDiscountPrice));
            }

            h.price_img.setImageResource(R.drawable.ic_shop_bag);
            h.price_img.setColorFilter(android.R.color.transparent);
        }
    }

    @Override
    public int getItemCount() {
        return audioBooks.size();
    }
}
