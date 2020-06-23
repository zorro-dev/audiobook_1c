package com.app.audiobook.audio;

import com.app.audiobook.component.PriceBook;

public class AudioBook {

    private String id;
    private String title;
    private String description;
    private Author author;
    private String parts;
    private PriceBook priceBook;
    private int cover;
    //private String coverUrl;
    //private ArrayList<Chapter> chapters = new ArrayList<>();

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

    public String getParts() {
        return parts;
    }

    public void setParts(String parts) {
        this.parts = parts;
    }

    /*
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

    */

    public int getCover() {
        return cover;
    }

    public void setCover(int cover) {
        this.cover = cover;
    }

    public PriceBook getPriceBook() {
        return priceBook;
    }

    public void setPriceBook(PriceBook priceBook) {
        this.priceBook = priceBook;
    }
}
