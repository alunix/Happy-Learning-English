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
import com.worker.happylearningenglish.rss.PageAdapter;
import com.worker.happylearningenglish.rss.PageEntry;
import com.worker.happylearningenglish.rss.RSSAdapter;
import com.worker.happylearningenglish.rss.RSSEntry;
import com.worker.happylearningenglish.rss.RSSFeed;
import com.worker.happylearningenglish.rss.RSSFeedParser;
import com.worker.happylearningenglish.activities.WebviewActivity;

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
public class ReadingFragment extends android.support.v4.app.Fragment implements OnBackPressedListener {
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
                Intent intent = new Intent(getContext(), WebviewActivity.class);
                intent.putExtra("url", rssEntry.getUrl());
                startActivity(intent);
            }
        };

        pageListAdapter = new PageAdapter(getActivity(), -1);

        pageListAdapter.add(new PageEntry("NYT > World", "http://rss.nytimes.com/services/xml/rss/nyt/World.xml", R.drawable.nyt));
        pageListAdapter.add(new PageEntry("NYT > Sports", "http://rss.nytimes.com/services/xml/rss/nyt/Sports.xml", R.drawable.nyt));
        pageListAdapter.add(new PageEntry("NYT > Health", "http://rss.nytimes.com/services/xml/rss/nyt/Health.xml", R.drawable.nyt));
        pageListAdapter.add(new PageEntry("NYT > Technology", "http://rss.nytimes.com/services/xml/rss/nyt/Technology.xml", R.drawable.nyt));

        pageListAdapter.add(new PageEntry("CNN.com - World", "http://rss.cnn.com/rss/edition_world.rss", R.drawable.cnn));
        pageListAdapter.add(new PageEntry("CNN.com - Technology", "http://rss.cnn.com/rss/edition_technology.rss", R.drawable.cnn));
        pageListAdapter.add(new PageEntry("CNN.com - Science and Space", "http://rss.cnn.com/rss/edition_space.rss", R.drawable.cnn));
        pageListAdapter.add(new PageEntry("CNN.com - Entertainment", "http://rss.cnn.com/rss/edition_entertainment.rss", R.drawable.cnn));
        pageListAdapter.add(new PageEntry("BBC News Asia", "http://feeds.bbci.co.uk/news/world/asia/rss.xml", R.drawable.bbc_news));
        pageListAdapter.add(new PageEntry("BBC News - Home", "http://feeds.bbci.co.uk/news/rss.xml", R.drawable.bbc_news));
        pageListAdapter.add(new PageEntry("BBC News - World", "http://feeds.bbci.co.uk/news/world/rss.xml", R.drawable.bbc_news));
        pageListAdapter.add(new PageEntry("BBC News - Education & Family", "http://feeds.bbci.co.uk/news/education/rss.xml", R.drawable.bbc_news));

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
            if (rssAdapter != null)
                rssAdapter.clear();
            processMode();
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
