package com.worker.happylearningenglish.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by hataketsu on 5/4/16.
 */
public class GoogleTranslator {
    public static String translate(String i) {
        try {
            String request = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=vi&dt=t&q=" + URLEncoder.encode(i, "UTF-8");
            InputStream inputStream = new URL(request).openStream();
            //
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
