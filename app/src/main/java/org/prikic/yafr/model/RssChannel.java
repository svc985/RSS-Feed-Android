package org.prikic.yafr.model;

import java.io.Serializable;

public class RssChannel implements Serializable{

    private long id;
    private String name;
    private String url;

    public RssChannel() {
    }

    public RssChannel(Long id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public RssChannel(String name, String url) {
        this.name = name;
        this.url = url;
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
}
