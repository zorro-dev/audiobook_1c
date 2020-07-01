package com.app.audiobook.audio.loader;

import androidx.annotation.NonNull;

import com.app.audiobook.audio.book.AudioBook;
import com.app.audiobook.component.JSONManager;
import com.app.audiobook.database.Loader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookCatalogLoader extends Loader<ArrayList<AudioBook>> {


    @Override
    public void load() {
        FirebaseDatabase.getInstance().getReference("BookCatalog")
                .child("Book")
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ArrayList<AudioBook> list = new ArrayList<>();

                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    String json = data.getValue(String.class);

                                    AudioBook audioBook = JSONManager.importFromJSON(json, AudioBook.class);

                                    list.add(audioBook);
                                }

                                deliverResult(list);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        }
                );
    }
}
