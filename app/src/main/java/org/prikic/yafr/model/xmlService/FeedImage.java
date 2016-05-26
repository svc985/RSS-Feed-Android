package org.prikic.yafr.model.xmlService;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "image", strict = false)
public class FeedImage implements Serializable {

    @Element(name = "url", data = true)
    private String url;

    public FeedImage() {

    }

    public FeedImage(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
