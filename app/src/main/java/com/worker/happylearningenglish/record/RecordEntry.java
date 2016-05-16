package com.worker.happylearningenglish.record;


import com.worker.happylearningenglish.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hataketsu on 4/15/16.
 */
public class RecordEntry {
    private CharSequence url;
    private CharSequence title;
    private CharSequence desc;
    private CharSequence date;

    public RecordEntry(CharSequence title, CharSequence desc, CharSequence date, CharSequence url) {
        this.title = title;
        this.desc = desc;
        this.url = url;
        this.date = date;
    }

    public static RecordEntry fromFile(File i) {
        CharSequence title = null;
        CharSequence desc = null;
        CharSequence date = null;
        try {
            title = FileUtils.readFromFile(i.getPath() + "/title.txt");
            if (new File(i.getPath() + "/date.txt").isFile())
                date = FileUtils.readFromFile(i.getPath() + "/date.txt");
            else date = "No date";
            desc = FileUtils.readFromFile(i.getPath() + "/desc.txt");
            return new RecordEntry(title, desc, date, i.getPath() + "/record.3gp");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CharSequence getUrl() {
        return url;
    }

    public CharSequence getTitle() {
        return title;
    }

    public CharSequence getDescription() {
        return desc;
    }

    public void delete() {
        FileUtils.deleteFolder(new File(new File((String) getUrl()).getParent()));
    }

    public boolean save() {
        File folder = new File(FileUtils.getPath() + "record/" + System.currentTimeMillis());
        folder.mkdirs();
        new File(FileUtils.getPath() + "tmp/tmp.3gp").renameTo(new File(folder.getPath() + "/record.3gp"));
        try {
            FileUtils.writeToFile(folder.getPath() + "/title.txt", title.toString());
            FileUtils.writeToFile(folder.getPath() + "/desc.txt", desc.toString());
            FileUtils.writeToFile(folder.getPath() + "/date.txt", FileUtils.getTime());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public CharSequence getDate() {
        return date;
    }
}
