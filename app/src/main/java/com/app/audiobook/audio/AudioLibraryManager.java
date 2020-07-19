package com.app.audiobook.audio;

import android.content.Context;

import com.app.audiobook.audio.book.AudioBook;
import com.app.audiobook.audio.book.Author;
import com.app.audiobook.audio.book.BookPrice;
import com.app.audiobook.audio.book.Chapter;

import java.util.ArrayList;

import static com.app.audiobook.audio.book.Chapter.STATE_NOT_READ;

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
        audioBook1.setDescription("«Главный следователь Скотленд-Ярда Питер Дауэс был для своей должности сравнительно молод. Еще ни разу не потерял он найденный след, хотя сам Питер не любил говорить о своих подвигах.\n" +
                "\n" +
                "Если его что-то и увлекало, так это преступления с какой-нибудь запутанной загадкой. Все необычное, странное и таинственное вызывало в нем живейший интерес, и больше всего на свете он сожалел, что ему не пришлось заниматься ни одним из множества дел Джейн Четыре Квадрата…»");
        audioBook1.setCoverUrl("https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Похищенная%20картина%2Fic_audio_book_2.jpg?alt=media&token=e031e56e-b5f6-4ae5-aff8-fce944330b9b");

        audioBook1.setIntroductoryFragment("https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Сотник%20Юрий%20-%20На%20тебя%20вся%20надежда%2F1.4.mp3?alt=media&token=315741a8-5920-4a22-a5ba-b806a9aa6d13");

        BookPrice bookPrice1 = new BookPrice();
        bookPrice1.setType(BookPrice.TYPE_FREE);

        audioBook1.setBookPrice(bookPrice1);

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
        audioBook2.setDescription("«Сказки барда Бидля» — вымышленная книга из сеттинга «Гарри Поттера». Наряду с «Фантастическими животными» и «Квиддичем скозь века», она фигурирует в текстах романов, а в 2007 году была реально написана Джоан Роулинг.\n" +
                "\n" +
                "\n" +
                "«Бидль…» издан в качестве рекламно-благотворительной акции вскоре после выхода последнего романа серии, «Даров смерти». Отпечатанная всего в количестве семи экземпляров книга не поступала в продажу. Шесть авторских экземпляров Роулинг подарила своим друзьям. Седьмая книга была выставлена на аукционе «Сотбис», где максимальная цена одного экземпляра достигла 4 миллионов долларов. Все деньги поступили на благотворительные нужды в организацию помощи детям-сиротам.\n" +
                "\n" +
                "В 2008 году книга была издана в бумажном виде, с благотворительными целями. В нашей стране она вышла в конце 2008 года.");
        audioBook2.setCoverUrl("https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Роулинг%20Джоан%20Кэтлин%20-%20Сказки%20барда%20Бидля%2Fic_audio_book_1.jpg?alt=media&token=a19a472b-2e17-44f7-bb05-964e3d5c785f");

        audioBook2.setIntroductoryFragment("https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Роулинг%20Джоан%20Кэтлин%20-%20Сказки%20барда%20Бидля%2FПредисловие.mp3?alt=media&token=e9778a39-a0d7-4e3d-820e-24744444c1f7");

        BookPrice bookPrice2 = new BookPrice();
        bookPrice2.setType(BookPrice.TYPE_USUAL_PRICE);
        bookPrice2.setPrice("5");
        bookPrice2.setDiscount(0);

        audioBook2.setBookPrice(bookPrice2);

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
        audioBook3.setDescription("Великолепный психологический экшн в застойных реалиях СА. Дитя Апшерона и рабоче-рыбацкой семьи беспечно ищет обрыв телефонного провода в снежных сумерках Забайкалья. Солдата бодрят лёгкий 30-градусный мороз и дежурный по узлу связи, читающий ему в трубку письмо из дома.");
        audioBook3.setCoverUrl("https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Рядовой%20Велиев%20-%20Виталий%20Сергеев%2Fic_audio_book_3.jpg?alt=media&token=c23ee603-7589-4a1d-9c0e-97eb844de782");

        BookPrice bookPrice3 = new BookPrice();
        bookPrice3.setType(BookPrice.TYPE_DISCOUNT_PRICE);
        bookPrice3.setPrice("10");
        bookPrice3.setDiscount(0.3f);

        audioBook3.setBookPrice(bookPrice3);

        audioBook3.setIntroductoryFragment("https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Рядовой%20Велиев%20-%20Виталий%20Сергеев%2Fчасть%201.mp3?alt=media&token=f4a0a3a9-86ef-46fb-a457-cc630e87e119");

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

    public static AudioBook getAudioBook4(){
        AudioBook audioBook4 = new AudioBook();
        audioBook4.setId("4");
        audioBook4.setTitle("На тебя вся надежда");
        audioBook4.setDescription("Предлагаем к чтению аннотацию, описание, краткое содержание или предисловие (зависит от того, что написал сам автор книги ««На тебя вся надежда…»»). Если вы не нашли необходимую информацию о книге — напишите в комментариях, мы постараемся отыскать её.");
        audioBook4.setCoverUrl("https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Сотник%20Юрий%20-%20На%20тебя%20вся%20надежда%2Fic_audio_book_4.jpg?alt=media&token=7fbcbc95-64d7-4367-8bb2-ae0b5dd044f9");

        BookPrice bookPrice4 = new BookPrice();
        bookPrice4.setType(BookPrice.TYPE_FREE);

        audioBook4.setBookPrice(bookPrice4);

        audioBook4.setIntroductoryFragment("https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Сотник%20Юрий%20-%20На%20тебя%20вся%20надежда%2F1.1.mp3?alt=media&token=325b7c45-95f0-4a59-8c2e-daf6da28b126");

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
        chapter4.setId("4");
        chapter4.setName("Часть 4");
        chapter4.setState(STATE_NOT_READ);
        chapter4.setUrl("https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Сотник%20Юрий%20-%20На%20тебя%20вся%20надежда%2F1.4.mp3?alt=media&token=315741a8-5920-4a22-a5ba-b806a9aa6d13");
        chapter4.setCached(false);
        chapter4.setDurationInSeconds(146);

        ArrayList<Chapter> characters = new ArrayList<>();
        characters.add(chapter1);
        characters.add(chapter2);
        characters.add(chapter3);
        characters.add(chapter4);

        audioBook4.setChapters(characters);

        return audioBook4;
    }
}
