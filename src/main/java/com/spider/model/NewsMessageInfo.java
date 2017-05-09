package com.spider.model;

import java.io.Serializable;

/**
 * Created by wq on 2017/5/9.
 */
public class NewsMessageInfo implements Serializable {
    private String url;
    private Integer type;
    private String body;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
