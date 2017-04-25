package com.spider.model;

import java.io.Serializable;

/**
 * Created by wq on 2017/4/23.
 */
public class Discuss implements Serializable {
    private String discuss_message_content;
    private String discuss_extra_info;
    private String nickname;
    private String avatar;
    private String praise_num;
    private Discuss reply_result;

    public String getDiscuss_message_content() {
        return discuss_message_content;
    }

    public void setDiscuss_message_content(String discuss_message_content) {
        this.discuss_message_content = discuss_message_content;
    }

    public String getDiscuss_extra_info() {
        return discuss_extra_info;
    }

    public void setDiscuss_extra_info(String discuss_extra_info) {
        this.discuss_extra_info = discuss_extra_info;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPraise_num() {
        return praise_num;
    }

    public void setPraise_num(String praise_num) {
        this.praise_num = praise_num;
    }

    public Discuss getReply_result() {
        return reply_result;
    }

    public void setReply_result(Discuss reply_result) {
        this.reply_result = reply_result;
    }
}
