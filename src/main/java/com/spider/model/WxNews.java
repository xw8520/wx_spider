package com.spider.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wq on 2017/4/24.
 */
public class WxNews implements Serializable {
    private String title;
    private String post_date;
    private String post_user;
    private String post_value;
    private String content;
    private List<Discuss> discusses;

    public List<Discuss> getDiscusses() {
        return discusses;
    }

    public void setDiscusses(List<Discuss> discusses) {
        this.discusses = discusses;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getPost_value() {
        return post_value;
    }

    public void setPost_value(String post_value) {
        this.post_value = post_value;
    }

    public String getPost_user() {
        return post_user;
    }

    public void setPost_user(String post_user) {
        this.post_user = post_user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
