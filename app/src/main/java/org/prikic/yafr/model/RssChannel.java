package org.prikic.yafr.model;

import java.io.Serializable;

public class RssChannel implements Serializable{

    private long id;
    private String name;
    private String url;
    private boolean isChannelActive;

    public RssChannel() {
    }

    public RssChannel(Long id, String name, String url, boolean isChannelActive) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.isChannelActive = isChannelActive;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isChannelActive() {
        return isChannelActive;
    }

    public void setChannelActive(boolean channelActive) {
        isChannelActive = channelActive;
    }

    @Override
    public String toString() {
        return "RssChannel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", isChannelActive=" + isChannelActive +
                '}';
    }
}
