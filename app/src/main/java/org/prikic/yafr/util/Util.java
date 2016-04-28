package org.prikic.yafr.util;

import org.prikic.yafr.model.RssChannel;

import java.util.LinkedList;
import java.util.List;

public class Util {

    public static List<RssChannel> getDummyDataset() {

        List<RssChannel> list = new LinkedList<>();
        list.add(new RssChannel("channel 1", "url 1"));
        list.add(new RssChannel("channel 2", "url 2"));
        list.add(new RssChannel("channel 3", "url 3"));

        return list;
    }
}
