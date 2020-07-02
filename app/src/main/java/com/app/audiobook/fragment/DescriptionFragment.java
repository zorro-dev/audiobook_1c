package com.app.audiobook.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import com.app.audiobook.BaseFragment;
import com.app.audiobook.R;
import com.app.audiobook.audio.book.AudioBook;
import com.bumptech.glide.Glide;
import com.joooonho.SelectableRoundedImageView;

import static com.app.audiobook.audio.book.BookPrice.TYPE_DISCOUNT_PRICE;
import static com.app.audiobook.audio.book.BookPrice.TYPE_FREE;
import static com.app.audiobook.audio.book.BookPrice.TYPE_USUAL_PRICE;

public class DescriptionFragment extends BaseFragment {

    View v;
    AudioBook audioBook;

    public DescriptionFragment(AudioBook audioBook) {
        this.audioBook = audioBook;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_description, container, false);

        initInterface();

        return v;
    }

    private void initInterface(){
        TextView description = v.findViewById(R.id.description);

        description.setText(audioBook.getDescription());

        ConstraintLayout back = v.findViewById(R.id.back);
        back.setOnClickListener(v1 -> {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();

            fragmentTransaction.addToBackStack("DescriptionFragment");
            fragmentTransaction.remove(this).commit();
        });
    }


}
