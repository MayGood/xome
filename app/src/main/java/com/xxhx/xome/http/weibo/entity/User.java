package com.xxhx.xome.http.weibo.entity;

/**
 * Created by xxhx on 2016/9/29.
 */
public class User {
    private long id;
    private String idstr;
    private String screen_name;
    private String name;
    private int province;
    private int city;
    private String location;
    private String description;
    private String url;
    private String profile_image_url;
    private String profile_url;
    private String domain;
    private String weihao;
    private String gender;
    private int followers_count;
    private int friends_count;
    private int statuses_count;
    private int favourites_count;
    private String created_at;
    private boolean following;
    private boolean allow_all_act_msg;
    private boolean geo_enabled;
    private boolean verified;
    private int verified_type;
    private String remark;
    private Status status;
    private boolean allow_all_comment;
    private String avatar_large;
    private String avatar_hd;
    private String verified_reason;
    private boolean follow_me;
    private int online_status;
    private int bi_followers_count;
    private String lang;

    public long getId() {
        return id;
    }

    public String getIdstr() {
        return idstr;
    }

    public String getScreenName() {
        return screen_name;
    }

    public String getName() {
        return name;
    }

    public int getProvince() {
        return province;
    }

    public int getCity() {
        return city;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getProfileImageUrl() {
        return profile_image_url;
    }

    public String getProfileUrl() {
        return profile_url;
    }

    public String getDomain() {
        return domain;
    }

    public String getWeihao() {
        return weihao;
    }

    public String getGender() {
        return gender;
    }

    public int getFollowersCount() {
        return followers_count;
    }

    public int getFriendsCount() {
        return friends_count;
    }

    public int getStatusesCount() {
        return statuses_count;
    }

    public int getFavouritesCount() {
        return favourites_count;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public boolean isFollowing() {
        return following;
    }

    public boolean isAllowAllActMsg() {
        return allow_all_act_msg;
    }

    public boolean isGeoEnabled() {
        return geo_enabled;
    }

    public boolean isVerified() {
        return verified;
    }

    public int getVerifiedType() {
        return verified_type;
    }

    public String getRemark() {
        return remark;
    }

    public Status getStatus() {
        return status;
    }

    public boolean isAllowAllComment() {
        return allow_all_comment;
    }

    public String getAvatarLarge() {
        return avatar_large;
    }

    public String getAvatarHd() {
        return avatar_hd;
    }

    public String getVerifiedReason() {
        return verified_reason;
    }

    public boolean isFollowMe() {
        return follow_me;
    }

    public int getOnlineStatus() {
        return online_status;
    }

    public int getBiFollowersCount() {
        return bi_followers_count;
    }

    public String getLang() {
        return lang;
    }
}
