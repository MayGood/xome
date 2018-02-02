package com.xxhx.xome.http.weibo.entity;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by xxhx on 2016/9/29.
 */
public class Status {
    private static final SimpleDateFormat sTimeFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);

    private String created_at;
    private long id;
    private long mid;
    private String idstr;
    private String text;
    private int textLength;
    private String source;
    private boolean favorited;
    private boolean truncated;
    private String in_reply_to_status_id;
    private String in_reply_to_user_id;
    private String in_reply_to_screen_name;
    private List<PicUrls> pic_urls;
    private String thumbnail_pic;
    private String bmiddle_pic;
    private String original_pic;
    private Geo geo;
    private User user;
    private Status retweeted_status;
    private int reposts_count;
    private int comments_count;
    private int attitudes_count;
    private int mlevel;
    private Object visible;

    private int singlePicWidth;
    private int singlePicHeight;

    public long getId() {
        return id;
    }

    public Date getCreatedAt() {
        ParsePosition pp = new ParsePosition(0);
        return sTimeFormat.parse(created_at, pp);
    }

    public String getText() {
        return text;
    }

    public User getUser() {
        return user;
    }

    public Status getRetweetedStatus() {
        return retweeted_status;
    }

    public int getPicCount() {
        return (pic_urls == null ? 0 : pic_urls.size());
    }

    private String parseImageKey(String url) {
        String[] splits = url.split("/");
        return splits[splits.length-1];
    }

    //public List<String> getThumbPicUrls() {
    //    List<String> urls = new ArrayList<String>();
    //    for(PicUrls picUrls : pic_urls) {
    //        String imgKey = parseImageKey(picUrls.thumbnail_pic);
    //        urls.add(String.format(URL_PATTERN_THUMB, imgKey));
    //    }
    //    return urls;
    //}

    public List<String> getPicKeys() {
        List<String> keys = new ArrayList<String>();
        for(PicUrls picUrls : pic_urls) {
            keys.add(parseImageKey(picUrls.thumbnail_pic));
        }
        return keys;
    }

    public void setSinglePicSize(int singlePicWidth, int singlePicHeight) {
        this.singlePicWidth = singlePicWidth;
        this.singlePicHeight = singlePicHeight;
    }

    public int getSinglePicHeight() {
        return singlePicHeight;
    }

    public int getSinglePicWidth() {
        return singlePicWidth;
    }

    public int getRepostsCount() {
        return reposts_count;
    }

    public int getCommentsCount() {
        return comments_count;
    }

    public int getAttitudesCount() {
        return attitudes_count;
    }

    @Override
    public String toString() {
        return "Status{" +
                "text='" + text + '\'' +
                '}';
    }

    class PicUrls {
        private String thumbnail_pic;
    }
}
