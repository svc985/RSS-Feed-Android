package org.prikic.yafr.model.xmlService;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root(name = "channel", strict = false)
public class Channel implements Serializable {
    @ElementList(inline = true, name="item")
    private List<FeedItem> mFeedItems;

    public List<FeedItem> getFeedItems() {
        return mFeedItems;
    }

    public Channel() {
    }

    public Channel(List<FeedItem> mFeedItems) {
        this.mFeedItems = mFeedItems;
    }
}
