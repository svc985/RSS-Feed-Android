package org.prikic.yafr.model.xmlService;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;

@Root(name = "channel", strict = false)
public class Channel implements Serializable {
    @ElementList(inline = true, name="item")
    private ArrayList<FeedItem> feedItems;

    public ArrayList<FeedItem> getFeedItems() {
        return feedItems;
    }

    public Channel() {
    }

    public Channel(ArrayList<FeedItem> feedItems) {
        this.feedItems = feedItems;
    }
}
