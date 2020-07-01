package com.app.audiobook.audio.book;

public class Chapter {

    private String id;
    private String name;
    private String url;

    public static String STATE_READ = "STATE_READ";
    public static String STATE_NOT_READ = "STATE_NOT_READ";
    public static String STATE_CLOSED = "STATE_CLOSED";
    public static String STATE_CURRENT = "STATE_CURRENT";

    private String state;
    private boolean isCached = false;
    private int durationInSeconds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isCached() {
        return isCached;
    }

    public void setCached(boolean cached) {
        isCached = cached;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }
}
