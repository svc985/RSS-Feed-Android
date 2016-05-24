package org.prikic.yafr.util;

import org.prikic.yafr.model.xmlService.FeedItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
}
