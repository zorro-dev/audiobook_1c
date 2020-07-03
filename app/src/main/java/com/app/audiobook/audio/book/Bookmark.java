package com.app.audiobook.audio.book;

public class Bookmark {

    private String id;
    private int durationInSeconds;
    private String timeStamp;
    private Chapter chapter;

    public Bookmark(String id, int durationInSeconds, String timeStamp, Chapter chapter) {
        this.id = id;
        this.durationInSeconds = durationInSeconds;
        this.timeStamp = timeStamp;
        this.chapter = chapter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }
}
