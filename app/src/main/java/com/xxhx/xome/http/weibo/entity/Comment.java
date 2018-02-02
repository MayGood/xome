package com.xxhx.xome.http.weibo.entity;

/**
 * Created by xxhx on 2016/9/29.
 */
public class Comment {
    private String created_at;
    private long id;
    private String text;
    private String source;
    private User user;
    private String mid;
    private String idstr;
    private Status status;
    private Comment reply_comment;

    public String getCreatedAt() {
        return created_at;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getSource() {
        return source;
    }

    public User getUser() {
        return user;
    }

    public String getMid() {
        return mid;
    }

    public String getIdstr() {
        return idstr;
    }

    public Status getStatus() {
        return status;
    }

    public Comment getReplyComment() {
        return reply_comment;
    }
}
