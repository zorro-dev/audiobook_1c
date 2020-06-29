package com.app.audiobook.audio.loader;

import androidx.annotation.NonNull;

import com.app.audiobook.audio.AudioBook;
import com.app.audiobook.component.JSONManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookCatalogLoader {

    private ArrayList<AudioBook> audioBooks = new ArrayList<>();

    public ArrayList<AudioBook> getAudioBooks() {
        return audioBooks;
    }

    public interface OnLoadListener {
        void onCatalogLoaded(ArrayList<AudioBook> audioBooks);
    }

    private OnLoadListener loadListener;

    public OnLoadListener getLoadListener() {
        return loadListener;
    }

    public void setLoadListener(OnLoadListener loadListener) {
        this.loadListener = loadListener;
    }

    public void load() {
        FirebaseDatabase.getInstance().getReference("BookCatalog")
                .child("Book").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                audioBooks = new ArrayList<>();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String json = data.getValue(String.class);

                    AudioBook audioBook = JSONManager.importFromJSON(json, AudioBook.class);

                    audioBooks.add(audioBook);
                }

                if (loadListener != null)
                    loadListener.onCatalogLoaded(audioBooks);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
