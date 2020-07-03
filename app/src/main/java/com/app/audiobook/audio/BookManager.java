package com.app.audiobook.audio;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.audiobook.audio.book.AudioBook;
import com.app.audiobook.component.JSONManager;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class BookManager {

    public static ArrayList<AudioBook> getBooksList(Context context){

        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

        ArrayList<AudioBook> bookmarks = JSONManager.importFromJSON(
                pref.getString("books", ""),
                new TypeToken<ArrayList<AudioBook>>(){}.getType());

        if (bookmarks == null){
            bookmarks = new ArrayList<>();
        }

        return bookmarks;
    }

    public static void addBook(Context context, AudioBook book){
        ArrayList<AudioBook> books = getBooksList(context);
        books.add(book);
        setBooksList(context, books);
    }

    public static void removeBook(Context context, String bookId){
        ArrayList<AudioBook> books = getBooksList(context);

        for(int i = 0; i < books.size(); i++){
            if(books.get(i).getId().equals(bookId)){
                books.remove(i);
                i--;
            }
        }

        setBooksList(context, books);
    }

    public static void setBooksList(Context context, ArrayList<AudioBook> bookmarks){
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String json = JSONManager.exportToJSON(bookmarks);

        editor.putString("books", json);
        editor.apply();
    }

}
