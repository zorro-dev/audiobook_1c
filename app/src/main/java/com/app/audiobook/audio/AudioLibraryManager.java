package com.app.audiobook.audio;

import android.content.Context;

import com.app.audiobook.R;

import java.util.ArrayList;

import static com.app.audiobook.audio.Chapter.STATE_NOT_READ;

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
        audioBook1.setTitle("Похищенная картина");
        audioBook1.setDescription("Краткое описание");
        audioBook1.setCoverUrl("https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Похищенная%20картина%2Fic_audio_book_2.jpg?alt=media&token=e031e56e-b5f6-4ae5-aff8-fce944330b9b");

        PriceBook priceBook1 = new PriceBook();
        priceBook1.setType(PriceBook.TYPE_FREE);

        audioBook1.setPriceBook(priceBook1);

        Author authorAudioBook1 = new Author();
        authorAudioBook1.setId("1");
        authorAudioBook1.setName("Эдгар Уоллес");
        audioBook1.setAuthor(authorAudioBook1);

        Chapter chapter1 = new Chapter();
        chapter1.setId("1");
        chapter1.setName("Похищенная картина 1");
        chapter1.setState(STATE_NOT_READ);
        chapter1.setUrl("https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Похищенная%20картина%2FПохищенная%20картина%201.mp3?alt=media&token=c2e9e3f1-4dda-43ea-be48-8cebc080d23f");
        chapter1.setCached(false);
        chapter1.setDurationInSeconds(366);

        Chapter chapter2 = new Chapter();
        chapter2.setId("2");
        chapter2.setName("Похищенная картина 2");
        chapter2.setState(STATE_NOT_READ);
        chapter2.setUrl("https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Похищенная%20картина%2FПохищенная%20картина%202.mp3?alt=media&token=a3e2e237-9a80-4742-9835-9a02c64c8841");
        chapter2.setCached(false);
        chapter2.setDurationInSeconds(603);

        Chapter chapter3 = new Chapter();
        chapter3.setId("3");
        chapter3.setName("Похищенная картина 3");
        chapter3.setState(STATE_NOT_READ);
        chapter3.setUrl("https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Похищенная%20картина%2FПохищенная%20картина%203.mp3?alt=media&token=4e818d09-bcee-4199-8f18-2917e6d61825");
        chapter3.setCached(false);
        chapter3.setDurationInSeconds(537);

        ArrayList<Chapter> characters = new ArrayList<>();
        characters.add(chapter1);
        characters.add(chapter2);
        characters.add(chapter3);

        audioBook1.setChapters(characters);

        return audioBook1;
    }

    private static AudioBook getAudioBook2(){
        AudioBook audioBook2 = new AudioBook();
        audioBook2.setId("2");
        audioBook2.setTitle("Сказки барда Бидля");
        audioBook2.setDescription("Краткое описание");
        audioBook2.setCoverUrl("https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Роулинг%20Джоан%20Кэтлин%20-%20Сказки%20барда%20Бидля%2Fic_audio_book_1.jpg?alt=media&token=a19a472b-2e17-44f7-bb05-964e3d5c785f");

        PriceBook priceBook2 = new PriceBook();
        priceBook2.setType(PriceBook.TYPE_USUAL_PRICE);
        priceBook2.setPrice("5");
        priceBook2.setDiscount(0);

        audioBook2.setPriceBook(priceBook2);

        Author authorAudioBook2 = new Author();
        authorAudioBook2.setId("2");
        authorAudioBook2.setName("Роулинг Джоан Кэтлин");
        audioBook2.setAuthor(authorAudioBook2);

        Chapter chapter1 = new Chapter();
        chapter1.setId("1");
        chapter1.setName("Предисловие");
        chapter1.setState(STATE_NOT_READ);
        chapter1.setUrl("https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Роулинг%20Джоан%20Кэтлин%20-%20Сказки%20барда%20Бидля%2FПредисловие.mp3?alt=media&token=e9778a39-a0d7-4e3d-820e-24744444c1f7");
        chapter1.setCached(false);
        chapter1.setDurationInSeconds(359);

        Chapter chapter2 = new Chapter();
        chapter2.setId("2");
        chapter2.setName("Колдун и прыгливый горшок");
        chapter2.setState(STATE_NOT_READ);
        chapter2.setUrl("https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Роулинг%20Джоан%20Кэтлин%20-%20Сказки%20барда%20Бидля%2FКолдун%20и%20прыгливый%20горшок.mp3?alt=media&token=75fbcbb4-8d63-4c5f-b2ff-d86d9121c284");
        chapter2.setCached(false);
        chapter2.setDurationInSeconds(891);

        Chapter chapter3 = new Chapter();
        chapter3.setId("3");
        chapter3.setName("Фонтан феи Фортуны");
        chapter3.setState(STATE_NOT_READ);
        chapter3.setUrl("https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Роулинг%20Джоан%20Кэтлин%20-%20Сказки%20барда%20Бидля%2FФонтан%20феи%20Фортуны.mp3?alt=media&token=b49d275e-d1cf-45a9-bfd6-e6f456ffb01e");
        chapter3.setCached(false);
        chapter3.setDurationInSeconds(964);

        ArrayList<Chapter> characters = new ArrayList<>();
        characters.add(chapter1);
        characters.add(chapter2);
        characters.add(chapter3);

        audioBook2.setChapters(characters);

        return audioBook2;
    }

    private static AudioBook getAudioBook3(){
        AudioBook audioBook3 = new AudioBook();
        audioBook3.setId("3");
        audioBook3.setTitle("Рядовой Велиев");
        audioBook3.setDescription("Краткое описание");
        audioBook3.setCoverUrl("https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Рядовой%20Велиев%20-%20Виталий%20Сергеев%2Fic_audio_book_3.jpg?alt=media&token=c23ee603-7589-4a1d-9c0e-97eb844de782");

        PriceBook priceBook3 = new PriceBook();
        priceBook3.setType(PriceBook.TYPE_DISCOUNT_PRICE);
        priceBook3.setPrice("10");
        priceBook3.setDiscount(0.3f);

        audioBook3.setPriceBook(priceBook3);

        Author authorAudioBook3 = new Author();
        authorAudioBook3.setId("3");
        authorAudioBook3.setName("Виталий Сергеев");
        audioBook3.setAuthor(authorAudioBook3);

        Chapter chapter1 = new Chapter();
        chapter1.setId("1");
        chapter1.setName("Часть 1");
        chapter1.setState(STATE_NOT_READ);
        chapter1.setUrl("https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Рядовой%20Велиев%20-%20Виталий%20Сергеев%2Fчасть%201.mp3?alt=media&token=f4a0a3a9-86ef-46fb-a457-cc630e87e119");
        chapter1.setCached(false);
        chapter1.setDurationInSeconds(352);

        Chapter chapter2 = new Chapter();
        chapter2.setId("2");
        chapter2.setName("Часть 2");
        chapter2.setState(STATE_NOT_READ);
        chapter2.setUrl("https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Рядовой%20Велиев%20-%20Виталий%20Сергеев%2Fчасть%202.mp3?alt=media&token=24f0bd4f-a3ef-4bb4-96e3-f836959936aa");
        chapter2.setCached(false);
        chapter2.setDurationInSeconds(309);

        Chapter chapter3 = new Chapter();
        chapter3.setId("3");
        chapter3.setName("Часть 3");
        chapter3.setState(STATE_NOT_READ);
        chapter3.setUrl("https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Рядовой%20Велиев%20-%20Виталий%20Сергеев%2Fчасть%203.mp3?alt=media&token=3004aeb7-4def-4933-aee0-80f77e9eee83");
        chapter3.setCached(false);
        chapter3.setDurationInSeconds(350);

        ArrayList<Chapter> characters = new ArrayList<>();
        characters.add(chapter1);
        characters.add(chapter2);
        characters.add(chapter3);

        audioBook3.setChapters(characters);

        return audioBook3;
    }

    private static AudioBook getAudioBook4(){
        AudioBook audioBook4 = new AudioBook();
        audioBook4.setId("4");
        audioBook4.setTitle("На тебя вся надежда");
        audioBook4.setDescription("Краткое описание");
        audioBook4.setCoverUrl("https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Сотник%20Юрий%20-%20На%20тебя%20вся%20надежда%2Fic_audio_book_4.jpg?alt=media&token=7fbcbc95-64d7-4367-8bb2-ae0b5dd044f9");

        PriceBook priceBook4 = new PriceBook();
        priceBook4.setType(PriceBook.TYPE_FREE);

        audioBook4.setPriceBook(priceBook4);

        Author authorAudioBook4 = new Author();
        authorAudioBook4.setId("4");
        authorAudioBook4.setName("Юрий Сотник");
        audioBook4.setAuthor(authorAudioBook4);

        Chapter chapter1 = new Chapter();
        chapter1.setId("1");
        chapter1.setName("Часть 1");
        chapter1.setState(STATE_NOT_READ);
        chapter1.setUrl("https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Сотник%20Юрий%20-%20На%20тебя%20вся%20надежда%2F1.1.mp3?alt=media&token=325b7c45-95f0-4a59-8c2e-daf6da28b126");
        chapter1.setCached(false);
        chapter1.setDurationInSeconds(140);

        Chapter chapter2 = new Chapter();
        chapter2.setId("2");
        chapter2.setName("Часть 2");
        chapter2.setState(STATE_NOT_READ);
        chapter2.setUrl("https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Сотник%20Юрий%20-%20На%20тебя%20вся%20надежда%2F1.2.mp3?alt=media&token=97cd09a7-795b-4047-a638-935ddf72d105");
        chapter2.setCached(false);
        chapter2.setDurationInSeconds(140);

        Chapter chapter3 = new Chapter();
        chapter3.setId("3");
        chapter3.setName("Часть 3");
        chapter3.setState(STATE_NOT_READ);
        chapter3.setUrl("https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Сотник%20Юрий%20-%20На%20тебя%20вся%20надежда%2F1.3.mp3?alt=media&token=aebd0e7b-3cd0-4979-9473-caec71ec0bbb");
        chapter3.setCached(false);
        chapter3.setDurationInSeconds(140);

        Chapter chapter4 = new Chapter();
        chapter3.setId("4");
        chapter3.setName("Часть 4");
        chapter3.setState(STATE_NOT_READ);
        chapter3.setUrl("https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Сотник%20Юрий%20-%20На%20тебя%20вся%20надежда%2F1.4.mp3?alt=media&token=315741a8-5920-4a22-a5ba-b806a9aa6d13");
        chapter3.setCached(false);
        chapter3.setDurationInSeconds(146);

        ArrayList<Chapter> characters = new ArrayList<>();
        characters.add(chapter1);
        characters.add(chapter2);
        characters.add(chapter3);
        characters.add(chapter4);

        audioBook4.setChapters(characters);

        return audioBook4;
    }
}
