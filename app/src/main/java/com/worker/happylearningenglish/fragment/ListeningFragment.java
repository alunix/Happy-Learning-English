package com.worker.happylearningenglish.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.worker.happylearningenglish.R;
import com.worker.happylearningenglish.activities.RecordPlayerActivity;
import com.worker.happylearningenglish.rss.PageAdapter;
import com.worker.happylearningenglish.rss.PageEntry;
import com.worker.happylearningenglish.rss.RSSAdapter;
import com.worker.happylearningenglish.rss.RSSEntry;
import com.worker.happylearningenglish.rss.RSSFeed;
import com.worker.happylearningenglish.rss.RSSFeedParser;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by hataketsu on 4/23/16.
 */
public class ListeningFragment extends android.support.v4.app.Fragment implements OnBackPressedListener {
    public static int MODE_PAGE = 1, MODE_ENTERED_PAGE = 2;
    View rootView;
    ListView listView;
    ArrayAdapter<PageEntry> pageListAdapter;
    int mode = MODE_PAGE;
    private AdapterView.OnItemClickListener pageListener;
    private AdapterView.OnItemClickListener webviewCallingListener;
    private RSSAdapter rssAdapter;
    private PageEntry pageEntry;
    private ProgressBar progressbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.reading_fragment, container, false);
            ((TextView) rootView.findViewById(R.id.textView)).setText(R.string.listen_podcast);
            ((TextView) rootView.findViewById(R.id.textView7)).setText(R.string.click_to_listen);
            listView = (ListView) rootView.findViewById(R.id.reading_lv);
            progressbar = (ProgressBar) rootView.findViewById(R.id.reading_progressbar);
            init();
        }
        mode = MODE_PAGE;
        processMode();
        return rootView;
    }

    private void processMode() {
        if (mode == MODE_PAGE) {
            listView.setAdapter(pageListAdapter);
            listView.setOnItemClickListener(pageListener);
            progressbar.setVisibility(View.INVISIBLE);
        } else if (mode == MODE_ENTERED_PAGE) {
            listView.setAdapter(rssAdapter);
            listView.setOnItemClickListener(webviewCallingListener);
        }
    }

    private void init() {
        pageListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pageEntry = (PageEntry) adapterView.getItemAtPosition(i);
                enterPage(pageEntry);
            }
        };

        webviewCallingListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RSSEntry rssEntry = (RSSEntry) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getContext(), RecordPlayerActivity.class);
                intent.putExtra("title", rssEntry.getTitle());
                intent.putExtra("desc", rssEntry.getDescription());
                intent.putExtra("url", rssEntry.getMp3url());
                intent.putExtra("stream", true);
                startActivity(intent);
            }
        };

        pageListAdapter = new PageAdapter(getActivity(), -1);
        pageListAdapter.add(new PageEntry("English as a Second Language (ESL) Podcast - Learn English Online", "http://feeds.feedburner.com/EnglishAsASecondLanguagePodcast", -1));
        pageListAdapter.add(new PageEntry("Conversations—A Mormon Channel Original", "http://feeds.lds.org/LDSConversations", -1));
        pageListAdapter.add(new PageEntry("Enduring It Well", "http://feeds.lds.org/EnduringItWell", -1));
        pageListAdapter.add(new PageEntry("Everything Creative—Exploring Creativity", "http://feeds.lds.org/EverythingCreative", -1));
        pageListAdapter.add(new PageEntry("The Friend | AAC | ENGLISH", "http://feeds.lds.org/LDSFriend", -1));
        pageListAdapter.add(new PageEntry("General Conference | MP3 | ENGLISH", "http://feeds.lds.org/lds-general-conference-mp3-eng", -1));
        pageListAdapter.add(new PageEntry("Youth Voices", "http://feeds.lds.org/LDSYouthVoices", -1));
        pageListAdapter.add(new PageEntry("Why I Believe", "http://feeds.lds.org/WhyIBelieve", -1));
        pageListAdapter.add(new PageEntry("Mormon Channel — Questions & Answers", "http://feeds.lds.org/LDSQuestionsAndAnswers", -1));
        pageListAdapter.add(new PageEntry("Scripture Stories—Stories and Inspiration for Children", "http://feeds.lds.org/ScriptureStories", -1));
        pageListAdapter.add(new PageEntry("Relief Society", "http://feeds.lds.org/LDSReliefSociety", -1));
        pageListAdapter.add(new PageEntry("Local [for developer only]", "http://192.168.43.207/podcast.xml", -1));
    }

    private void enterPage(PageEntry pageEntry) {
        mode = MODE_ENTERED_PAGE;
        progressbar.setVisibility(View.VISIBLE);
        new LoadRSSTask().execute(pageEntry.getUrl());
        processMode();
    }

    @Override
    public boolean onBackPressed() {
        if (mode == MODE_ENTERED_PAGE) {
            mode = MODE_PAGE;
            processMode();
            if (rssAdapter != null)
                rssAdapter.clear();
            return true;
        }
        return false;
    }

    private class LoadRSSTask extends AsyncTask<String, String, RSSFeed> {
        @Override
        protected RSSFeed doInBackground(String... strings) {
            RSSFeed feed = null;
            try {
                feed = RSSFeedParser.feedRSS(new URL(strings[0]).openStream());
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                publishProgress(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                publishProgress(e.getMessage());

            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                publishProgress(e.getMessage());

            } catch (SAXException e) {
                e.printStackTrace();
                publishProgress(e.getMessage());
            }
            return feed;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            for (String i : values) {
                Toast.makeText(getActivity(), i, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPostExecute(RSSFeed rssFeed) {
            progressbar.setVisibility(View.INVISIBLE);

            if (rssFeed == null) {
                Toast.makeText(getContext(), R.string.load_failed, Toast.LENGTH_LONG).show();

                return;
            }
            ArrayList<RSSEntry> rssEntries = rssFeed.getRssEntries();
            rssAdapter = new RSSAdapter(getActivity(), -1, pageEntry.getImageID());
            rssAdapter.addAll(rssEntries);
            listView.setAdapter(rssAdapter);
            processMode();
        }
    }

}
