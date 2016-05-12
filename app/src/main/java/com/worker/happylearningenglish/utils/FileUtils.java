package com.worker.happylearningenglish.utils;

import android.os.Environment;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by hataketsu on 4/14/16.
 */
public class FileUtils {

    public static String getPath() {
        return Environment.getExternalStorageDirectory() + "/happyenglish/";
    }

    public static void writeToFile(String path, String text) throws IOException {
        File file = new File(path);
        if (!file.exists())
            file.createNewFile();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
        outputStreamWriter.write(text);
        outputStreamWriter.close();
    }

    public static String readFromFile(String path) throws IOException {
        StringBuffer buffer = new StringBuffer();
        String result = "";
        File file = new File(path);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
        while ((result = bufferedReader.readLine()) != null)
            buffer.append(result + "\n");
        if (buffer.length() > 1)
            buffer.deleteCharAt(buffer.length() - 1);
        bufferedReader.close();
        return buffer.toString();
    }

    public static void saveList(ArrayAdapter<String> arrayAdapter) throws IOException {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < arrayAdapter.getCount(); i++) {
            buffer.append(arrayAdapter.getItem(i));
            buffer.append("\n");
        }
        writeToFile(getPath() + "listen/sitelist.txt", buffer.toString());
    }

    public static void deleteFolder(File file) {
        if (file.isDirectory()) {
            for (File i : file.listFiles())
                deleteFolder(i);
        }
        file.delete();
    }
}
