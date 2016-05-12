package com.worker.happylearningenglish.rss;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by hataketsu on 4/14/16.
 */
public class RSSFeedParser {
    public static RSSFeed feedRSS(InputStream inputStream) throws XmlPullParserException, IOException, ParserConfigurationException, SAXException {
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document xmlDoc = docBuilder.parse(inputStream);
        return feedRSS(xmlDoc);
    }

    private static RSSFeed feedRSS(Document xmlDoc) throws IOException, XmlPullParserException {
        NodeList list = xmlDoc.getElementsByTagName("rss"); //RSS Document
        if (list.getLength() > 0)
            return readRSSFeed((Element) list.item(0));

        list = xmlDoc.getElementsByTagName("feed"); //Atom Document
        if (list.getLength() > 0)
            return readAtomFeed((Element) list.item(0));
        return null;
    }

    private static RSSFeed readAtomFeed(Element feedTag) {
        RSSFeed atomFeed = new RSSFeed();
        atomFeed.setLink(getValue(feedTag, "title"));
        atomFeed.setDescription(getValue(feedTag, "subtitle"));
        atomFeed.setLink(getValue(feedTag, "link"));
        NodeList entryList = feedTag.getElementsByTagName("entry");
        for (int i = 0; i < entryList.getLength(); i++) {
            atomFeed.addEntry(readAtomEntry((Element) entryList.item(i)));
        }
        return atomFeed;
    }

    private static RSSEntry readAtomEntry(Element entryTag) {
        RSSEntry atomEntry = new RSSEntry();
        atomEntry.setTitle(getValue(entryTag, "title"));
        atomEntry.setDescription("");
        atomEntry.setDate(getValue(entryTag, "updated"));
        String link = null;
        NodeList atts = entryTag.getElementsByTagName("link");
        for (int i = 0; i < atts.getLength(); i++) {
            Node item = atts.item(i);
            if (item.getAttributes().getNamedItem("rel").getTextContent().equals("alternate")) {
                link = item.getAttributes().getNamedItem("href").getTextContent();
            }
        }
        atomEntry.setUrl(link);
        return atomEntry;
    }

    private static RSSFeed readRSSFeed(Element rssTag) throws IOException, XmlPullParserException {
        RSSFeed rssFeed = new RSSFeed();
        Element channelTag = (Element) rssTag.getElementsByTagName("channel").item(0);
        rssFeed.setLink(getValue(channelTag, "title"));
        rssFeed.setDescription(getValue(channelTag, "description"));
        rssFeed.setLink(getValue(channelTag, "link"));
        NodeList entryList = channelTag.getElementsByTagName("item");
        for (int i = 0; i < entryList.getLength(); i++) {
            rssFeed.addEntry(readRSSEntry((Element) entryList.item(i)));
        }
        return rssFeed;
    }

    private static CharSequence getValue(Element itemTag, String tag) {
        return itemTag.getElementsByTagName(tag).item(0).getTextContent();
    }

    private static RSSEntry readRSSEntry(Element itemTag) throws IOException, XmlPullParserException {
        RSSEntry rssEntry = new RSSEntry();
        rssEntry.setTitle(getValue(itemTag, "title"));
        rssEntry.setDescription(getValue(itemTag, "description"));
        rssEntry.setUrl(getValue(itemTag, "link"));
        rssEntry.setDate(getValue(itemTag, "pubDate"));
        if (itemTag.getElementsByTagName("media:content").getLength() > 0)
            rssEntry.setMp3url(getMP3Value(itemTag));
        return rssEntry;
    }

    private static CharSequence getMP3Value(Element itemTag) {
        return itemTag.getElementsByTagName("media:content").item(0).getAttributes().getNamedItem("url").getTextContent();
    }
}
