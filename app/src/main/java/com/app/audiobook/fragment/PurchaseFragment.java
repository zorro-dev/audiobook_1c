package com.app.audiobook.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import com.app.audiobook.BaseFragment;
import com.app.audiobook.R;
import com.app.audiobook.audio.book.AudioBook;
import com.app.audiobook.audio.catalog.UserCatalog;
import com.app.audiobook.component.JSONManager;
import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.joooonho.SelectableRoundedImageView;

import static com.app.audiobook.audio.book.BookPrice.TYPE_DISCOUNT_PRICE;
import static com.app.audiobook.audio.book.BookPrice.TYPE_FREE;
import static com.app.audiobook.audio.book.BookPrice.TYPE_USUAL_PRICE;

public class PurchaseFragment extends BaseFragment {

    View v;
    AudioBook audioBook;
    UserCatalog userCatalog;

    public PurchaseFragment(AudioBook audioBook, UserCatalog userCatalog) {
        this.audioBook = audioBook;
        this.userCatalog = userCatalog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_buy_book, container, false);

        initInterface();

        initBuyButton();

        return v;
    }

    private void initInterface(){
        TextView title = v.findViewById(R.id.title);
        TextView author = v.findViewById(R.id.author);
        TextView count_parts = v.findViewById(R.id.count_parts);
        SelectableRoundedImageView cover = v.findViewById(R.id.book_cover);
        TextView price = v.findViewById(R.id.price);
        ImageView layout_color = v.findViewById(R.id.layout_color);
        ConstraintLayout discount_layout = v.findViewById(R.id.discount_layout);
        TextView discount= v.findViewById(R.id.discount);
        TextView description = v.findViewById(R.id.description);

        ImageView colorButton = v.findViewById(R.id.color_button);
        TextView textButton = v.findViewById(R.id.text_button);

        title.setText(audioBook.getTitle());
        author.setText(audioBook.getAuthor().getName());
        count_parts.setText(String.valueOf("Частей: " + audioBook.getChapterSize()));
        Glide.with(getContext())
                .load(audioBook.getCoverUrl())
                .placeholder(R.drawable.ic_black_square)
                .into(cover);

        if (userCatalog.contains(audioBook)) {
            colorButton.setColorFilter(getResources().getColor(R.color.colorGray_1));
            textButton.setText("Уже в аудиотеке");
        } else if (audioBook.getBookPrice().getType().equals(TYPE_FREE)) {
            price.setText("Бесплатно");
            colorButton.setColorFilter(getResources().getColor(R.color.colorFreePrice));
            textButton.setText("Забрать книгу");
        } else {
            price.setText(audioBook.getBookPrice().getPrice() + "$");
        }

        if (audioBook.getBookPrice().getDiscount() == 0) {
            discount_layout.setVisibility(View.GONE);
        } else {
            discount_layout.setVisibility(View.VISIBLE);
            price.setText(String.valueOf(Integer.parseInt(audioBook.getBookPrice().getPrice()) * audioBook.getBookPrice().getDiscount()) + "$");
            price.setTextColor(getResources().getColor(R.color.colorGreen_5));
            discount.setVisibility(View.VISIBLE);
            discount.setText(audioBook.getBookPrice().getPrice() + "$");
        }

        if (audioBook.getBookPrice().getType().equals(TYPE_FREE)) {
            layout_color.setColorFilter(getResources().getColor(R.color.colorFreePrice));
        } else if (audioBook.getBookPrice().getType().equals(TYPE_USUAL_PRICE)) {
            layout_color.setColorFilter(getResources().getColor(R.color.colorUsualPrice));
        } else if (audioBook.getBookPrice().getType().equals(TYPE_DISCOUNT_PRICE)) {
            layout_color.setColorFilter(getResources().getColor(R.color.colorDiscountPrice));
        }

        description.setText(audioBook.getDescription());

        ConstraintLayout back = v.findViewById(R.id.back);
        back.setOnClickListener(v1 -> {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();

            fragmentTransaction.addToBackStack("PurchaseFragment");
            fragmentTransaction.remove(this).commit();
        });
    }

    private void initBuyButton() {
        ConstraintLayout buy = v.findViewById(R.id.button);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userCatalog.contains(audioBook)) {
                    Toast.makeText(getContext(), "Уже добавлено", Toast.LENGTH_SHORT).show();
                } else if (audioBook.getBookPrice().getType().equals(TYPE_FREE)) {
                    addToUserCatalog();
                    initInterface();
                } else {
                    //TODO показать фрагмент покупки
                }
            }
        });
    }

    private void addToUserCatalog() {
        userCatalog.getCatalogList().add(audioBook);
        userCatalog.updateList();
        FirebaseDatabase.getInstance().getReference("BookCatalog")
                .child("ByUsers").child(getAuthManager().getUser().getId())
                .child(audioBook.getId()).setValue(JSONManager.exportToJSON(audioBook));
    }

}
