package com.worker.happylearningenglish.rss;

/**
 * Created by hataketsu on 4/15/16.
 */
public class RSSEntry {
    private CharSequence title = "";
    private CharSequence description = "";
    private CharSequence mp3url;
    private CharSequence url = "";
    private CharSequence date;

    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public CharSequence getDescription() {
        return description;
    }

    public void setDescription(CharSequence description) {
        this.description = description;
    }

    public CharSequence getUrl() {
        return url;
    }

    public void setUrl(CharSequence url) {
        this.url = url;
    }

    public CharSequence getDate() {
        return date;
    }

    public void setDate(CharSequence date) {
        this.date = date;
    }

    public CharSequence getMp3url() {
        return mp3url;
    }

    public void setMp3url(CharSequence mp3url) {
        this.mp3url = mp3url;
    }
}
