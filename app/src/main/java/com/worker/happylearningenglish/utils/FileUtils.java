package com.worker.happylearningenglish.utils;

import android.os.Environment;
import android.widget.Toast;

import com.worker.happylearningenglish.R;
import com.worker.happylearningenglish.activities.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(text);
        fileWriter.close();
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


    public static void deleteFolder(File file) {
        if (file.isDirectory()) {
            for (File i : file.listFiles())
                deleteFolder(i);
        }
        file.delete();
    }

    public static void error(Exception e) {
        StringBuffer buf = new StringBuffer();
        buf.append(e.getMessage()).append("\n");
        buf.append(e.toString()).append("\n");
        for (StackTraceElement i : e.getStackTrace()) {
            buf.append(i.toString()).append("\n");
        }
        try {
            String path = FileUtils.getPath() + "errorlog.txt";

            FileUtils.writeToFile(path, readFromFile(path) + "\n\n\n" + getTime() + "\n\n" + buf.toString());
        } catch (IOException e1) {
            e1.printStackTrace();
            Toast.makeText(MainActivity.getContext(), R.string.cannot_error, Toast.LENGTH_LONG).show();
        }
    }

    public static String getTime() {
        return new SimpleDateFormat("MM'/'dd'/'y hh:mm:ss").format(new Date());
    }
}
