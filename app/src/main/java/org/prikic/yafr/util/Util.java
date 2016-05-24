package org.prikic.yafr.util;

import org.prikic.yafr.model.xmlService.FeedItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class Util {

    public static List<Object> getDummyList() {

        List<Object> list = new LinkedList<>();
        list.add("object 1");
        list.add("object 2");
        list.add("object 3");

        return list;
    }

    public static List<FeedItem> getFeedItems() {
        List<FeedItem> feedItems = new ArrayList<>();
        feedItems.add(new FeedItem("desc", "link", "title", "pubDate"));
        feedItems.add(new FeedItem("desc 2", "link 2", "title 2", "pubDate 2"));

        return feedItems;
    }

    public static String parseDate(String pubDate) {

        List<SimpleDateFormat> knownPatterns = new ArrayList<>();
        knownPatterns.add(new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z", Locale.US));
        knownPatterns.add(new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US));

        for (SimpleDateFormat pattern : knownPatterns) {
            try {
                // Take a try
                Date date = pattern.parse(pubDate);
                SimpleDateFormat dt1 = new SimpleDateFormat("dd MM yyyy - HH:mm", Locale.US);
                return dt1.format(date);

            } catch (ParseException pe) {
                // Loop on
            }
        }
        return "";

    }
}
