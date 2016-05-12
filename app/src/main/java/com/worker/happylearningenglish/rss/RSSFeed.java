package com.worker.happylearningenglish.rss;

import java.util.ArrayList;

/**
 * Created by hataketsu on 4/15/16.
 */
public class RSSFeed {
    private ArrayList<RSSEntry> rssEntries = new ArrayList<>();
    private CharSequence title = "";
    private CharSequence description = "";
    private CharSequence link = "";

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

    public CharSequence getLink() {
        return link;
    }

    public void setLink(CharSequence link) {
        this.link = link;
    }

    public ArrayList<RSSEntry> getRssEntries() {
        return rssEntries;
    }

    public void addEntry(RSSEntry rssEntry) {
        rssEntries.add(rssEntry);
    }
}
