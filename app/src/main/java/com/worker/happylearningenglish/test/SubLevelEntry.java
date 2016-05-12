package com.worker.happylearningenglish.test;

/**
 * Created by hataketsu on 5/8/16.
 */
public class SubLevelEntry {
    private int level;
    private int subLevel;
    private String description;

    public SubLevelEntry(int level, int subLevel, String description) {
        this.level = level;
        this.subLevel = subLevel;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getLevel() {
        return level;
    }

    public int getSubLevel() {
        return subLevel;
    }
}
