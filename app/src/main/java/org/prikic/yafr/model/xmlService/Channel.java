package org.prikic.yafr.model.xmlService;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;

@Root(name = "channel", strict = false)
public class Channel implements Serializable {
    @ElementList(inline = true, name="item")
    private ArrayList<FeedItem> feedItems;

    @Element(required=false, name="image")
    private FeedImage feedImage;

    public ArrayList<FeedItem> getFeedItems() {
        return feedItems;
    }

    public FeedImage getFeedImage() {
        return feedImage;
    }

    public Channel() {
    }

    public Channel(ArrayList<FeedItem> feedItems, FeedImage feedImage) {
        this.feedItems = feedItems;
        this.feedImage = feedImage;
    }
}
