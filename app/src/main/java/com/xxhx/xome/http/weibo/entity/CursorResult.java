package com.xxhx.xome.http.weibo.entity;

import java.util.List;

/**
 * Created by xxhx on 2016/9/29.
 */
public class CursorResult {
    private long previous_cursor;
    private long next_cursor;
    private int total_number;

    private List<Status> statuses;
    private List<User> users;
    private List<Status> reposts;
    private List<Comment> comments;

    public List<Status> getStatuses() {
        return statuses;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Status> getReposts() {
        return reposts;
    }

    public List<Comment> getComments() {
        return comments;
    }
}
