package com.app.audiobook.audio;

import android.content.Context;

import com.app.audiobook.R;

import java.util.ArrayList;

public class AudioLibraryManager {

    public static ArrayList<AudioBook> getBookList(Context context){

        ArrayList<AudioBook> audioBooks = new ArrayList<>();

        audioBooks.add(getAudioBook1());
        audioBooks.add(getAudioBook2());
        audioBooks.add(getAudioBook3());
        audioBooks.add(getAudioBook4());

        return audioBooks;
    }

    private static AudioBook getAudioBook1(){
        AudioBook audioBook1 = new AudioBook();
        audioBook1.setId("1");
        audioBook1.setTitle("Сказки барда Бидля");
        audioBook1.setDescription("Краткое описание");
        audioBook1.setCover(R.drawable.ic_audio_book);
        audioBook1.setParts("3 части");

        Author authorAudioBook1 = new Author();
        authorAudioBook1.setId("1");
        authorAudioBook1.setName("Роулинг Джоан Кэтлин");
        audioBook1.setAuthor(authorAudioBook1);

        return audioBook1;
    }

    private static AudioBook getAudioBook2(){
        AudioBook audioBook1 = new AudioBook();
        audioBook1.setId("2");
        audioBook1.setTitle("Harry Potter and the Goblet of Fire");
        audioBook1.setDescription("Краткое описание");
        audioBook1.setCover(R.drawable.ic_audio_book_2);
        audioBook1.setParts("5 частей");

        Author authorAudioBook1 = new Author();
        authorAudioBook1.setId("2");
        authorAudioBook1.setName("J. K. Rowling");
        audioBook1.setAuthor(authorAudioBook1);

        return audioBook1;
    }

    private static AudioBook getAudioBook3(){
        AudioBook audioBook1 = new AudioBook();
        audioBook1.setId("3");
        audioBook1.setTitle("Гарри Поттер 'Рождение легенды'");
        audioBook1.setDescription("Краткое описание");
        audioBook1.setCover(R.drawable.ic_audio_book_3);
        audioBook1.setParts("1 часть");

        Author authorAudioBook1 = new Author();
        authorAudioBook1.setId("3");
        authorAudioBook1.setName("Джоан Роулинг");
        audioBook1.setAuthor(authorAudioBook1);

        return audioBook1;
    }

    private static AudioBook getAudioBook4() {
        AudioBook audioBook1 = new AudioBook();
        audioBook1.setId("4");
        audioBook1.setTitle("Глаз бога");
        audioBook1.setDescription("Краткое описание");
        audioBook1.setCover(R.drawable.ic_audio_book_4);
        audioBook1.setParts("7 частей");

        Author authorAudioBook1 = new Author();
        authorAudioBook1.setId("4");
        authorAudioBook1.setName("Джеймс Роллинс");
        audioBook1.setAuthor(authorAudioBook1);

        return audioBook1;
    }
}
