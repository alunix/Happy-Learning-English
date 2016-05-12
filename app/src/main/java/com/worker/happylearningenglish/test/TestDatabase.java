package com.worker.happylearningenglish.test;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.widget.ListAdapter;

import com.worker.happylearningenglish.activities.MainActivity;
import com.worker.happylearningenglish.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by hataketsu on 5/8/16.
 */
public class TestDatabase {
    private static SQLiteDatabase instance;


    private static Cursor query(String s) {
        return getInstance().rawQuery(s, null);
    }

    private static SQLiteDatabase getInstance() {
        if (instance == null) {
            instance = SQLiteDatabase.openOrCreateDatabase(getPath(), null);
        }
        return instance;
    }

    @NonNull
    private static String getPath() {
        return FileUtils.getPath() + "engrammar10.db";
    }

    public static ListAdapter getSubLevelAdapter(int level) {
        Cursor cursor = query("select SubLevel, Subject from LevelSubject where Level==" + level);
        SubLevelAdapter adapter = new SubLevelAdapter(MainActivity.getContext(), -1);
        while (cursor.moveToNext()) {
            adapter.add(new SubLevelEntry(level, cursor.getInt(0), cursor.getString(1)));
        }
        return adapter;
    }

    public static ListAdapter getQuestions(SubLevelEntry entry) {
        int level = entry.getLevel();
        int subLevel = entry.getSubLevel();
        Cursor cursor = query("select QContent, AnswerA, AnswerB, AnswerC, AnswerD, CorrectAnswer from Test where Level==" + level + " and SubLevel==" + subLevel);
        QuestionAdapter questionAdapter = new QuestionAdapter(MainActivity.getContext(), -1);
        while (cursor.moveToNext()) {
            questionAdapter.add(new QuestionEntry(cursor));
        }
        return questionAdapter;
    }

    public static void storeDatabase() {
        if (new File(getPath()).isFile()) return;
        try {
            InputStream src = MainActivity.getContext().getAssets().open("engrammar10.db");
            OutputStream dest = new FileOutputStream(getPath());
            byte[] buffer = new byte[1024];
            int len;
            while ((len = src.read(buffer)) != -1) {
                dest.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
