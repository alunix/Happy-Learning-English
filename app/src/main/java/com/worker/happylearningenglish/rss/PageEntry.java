package com.worker.happylearningenglish.rss;


import com.worker.happylearningenglish.R;

/**
 * Created by hataketsu on 4/16/16.
 */
public class PageEntry {

    private int imageID;
    private String title;
    private String url;

    public PageEntry(String title, String url, int imageID) {
        this.title = title;
        this.url = url;
        this.imageID = imageID;
        if (this.imageID == -1) this.imageID = R.drawable.rss;
    }

    public String getTitle() {
        return title;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }
}
