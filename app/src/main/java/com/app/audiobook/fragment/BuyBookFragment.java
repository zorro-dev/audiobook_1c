package com.app.audiobook.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import com.app.audiobook.BaseFragment;
import com.app.audiobook.R;

public class BuyBookFragment extends BaseFragment {

    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_buy_book, container, false);

        initInterface();

        return v;
    }

    private void initInterface(){
        ConstraintLayout back = v.findViewById(R.id.back);

        back.setOnClickListener(v1 -> {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();

            fragmentTransaction.addToBackStack("BuyBookFragment");
            fragmentTransaction.remove(this).commit();
        });
    }


}
