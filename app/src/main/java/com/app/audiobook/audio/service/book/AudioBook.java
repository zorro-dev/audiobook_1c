package com.app.audiobook.audio.service.book;

import java.util.ArrayList;

public class AudioBook {

    private String id;
    private String title;
    private String description;
    private Author author;
    private BookPrice bookPrice;
    private String coverUrl;
    private ArrayList<Chapter> chapters = new ArrayList<>();

    public AudioBook() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getChapterSize() {
        return chapters.size();
    }

    public ArrayList<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(ArrayList<Chapter> chapters) {
        this.chapters = chapters;
    }

    public int getDurationInSeconds() {
        int duration = 0;

        for (int i = 0; i < chapters.size(); i ++) {
            duration += chapters.get(i).getDurationInSeconds();
        }

        return duration;
    }


    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public BookPrice getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(BookPrice bookPrice) {
        this.bookPrice = bookPrice;
    }
}
