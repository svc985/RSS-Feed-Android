package org.prikic.yafr.model;

import java.io.Serializable;

public class FeedItemExtended implements Serializable {

    private String pubDate;
    private String title;
    private String link;
    private String description;
    private String sourceImageUrl;

    public FeedItemExtended() {
    }

    public FeedItemExtended(String description, String link, String title, String pubDate, String source_image_url) {
        this.description = description;
        this.link = link;
        this.title = title;
        this.pubDate = pubDate;
        this.sourceImageUrl = source_image_url;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSourceImageUrl() {
        return sourceImageUrl;
    }

    public void setSourceImageUrl(String sourceImageUrl) {
        this.sourceImageUrl = sourceImageUrl;
    }
}
