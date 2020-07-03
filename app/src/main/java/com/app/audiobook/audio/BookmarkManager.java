package com.app.audiobook.audio;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.audiobook.audio.book.Bookmark;
import com.app.audiobook.component.JSONManager;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class BookmarkManager {

    public static ArrayList<Bookmark> getBookmarksList(Context context, String bookId){

        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

        ArrayList<Bookmark> bookmarks = JSONManager.importFromJSON(
                pref.getString("bookmarks_" + bookId , ""),
                new TypeToken<ArrayList<Bookmark>>(){}.getType());

        if (bookmarks == null){
            bookmarks = new ArrayList<>();
        }

        return bookmarks;
    }

    public static void addBookmark(Context context, String bookId, Bookmark bookmark){
        ArrayList<Bookmark> bookmarks = getBookmarksList(context, bookId);
        bookmarks.add(bookmark);
        setBookmarksList(context, bookmarks, bookId);
    }

    public static void removeBookmark(Context context, String bookId, String bookmarkId){
        ArrayList<Bookmark> bookmarks = getBookmarksList(context, bookId);

        for(int i = 0; i < bookmarks.size(); i++){
            if(bookmarks.get(i).getId().equals(bookmarkId)){
                bookmarks.remove(i);
                i--;
            }
        }

        setBookmarksList(context, bookmarks, bookId);
    }

    public static void setBookmarksList(Context context, ArrayList<Bookmark> bookmarks, String bookId){
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String json = JSONManager.exportToJSON(bookmarks);

        editor.putString("bookmarks_" + bookId, json);
        editor.apply();
    }

}
