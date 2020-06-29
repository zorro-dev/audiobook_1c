package com.app.audiobook.audio.loader;

import androidx.annotation.NonNull;

import com.app.audiobook.audio.AudioBook;
import com.app.audiobook.component.JSONManager;
import com.app.audiobook.database.Loader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserCatalogLoader extends Loader<ArrayList<AudioBook>> {

    private String userId;

    public UserCatalogLoader(String userId) {
        this.userId = userId;
    }

    @Override
    public void load() {
        FirebaseDatabase.getInstance().getReference("BookCatalog")
                .child("UserBooks").child(userId)
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
