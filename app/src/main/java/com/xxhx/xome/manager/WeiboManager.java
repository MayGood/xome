package com.xxhx.xome.manager;

import com.xxhx.xome.config.Constants;
import com.xxhx.xome.helper.PreferHelper;
import com.xxhx.xome.http.weibo.WeiboService;
import com.xxhx.xome.http.weibo.entity.AccessToken;
import com.xxhx.xome.http.weibo.entity.CursorResult;
import com.xxhx.xome.http.weibo.entity.Status;
import com.xxhx.xome.http.weibo.entity.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xxhx on 2016/9/29.
 */
public class WeiboManager {

    private static final String BASE_URL = "https://api.weibo.com/";

    private WeiboService mWeiboService;

    private AccessToken mAccessToken;

    private WeiboManager() {
        AccessToken token = PreferHelper.getWeiboAccessToken();
        if(token.isValid()) {
            mAccessToken = token;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        mWeiboService = retrofit.create(WeiboService.class);
    }

    private static class InstanceHolder {
        private static final WeiboManager sInstance = new WeiboManager();
    }

    public static WeiboManager getInstance() {
        return InstanceHolder.sInstance;
    }

    public void setAccessToken(AccessToken accessToken) {
        mAccessToken = accessToken;
    }

    public AccessToken getAccessToken() {
        return mAccessToken;
    }

    public void authAccessToken(Subscriber<AccessToken> subscriber, String code) {
        mWeiboService.accessToken(Constants.sWeiboClientId, Constants.sWeiboClientSecret, Constants.sWeiboGrantType,
                Constants.sWeiboRedirectUri, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    public void loadHomeTimeline(Subscriber<CursorResult> subscriber, String accessToken, Map<String, String> options) {
        mWeiboService.homeTimeline(accessToken, options)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void loadHomeTimeline(Subscriber<CursorResult> subscriber, String accessToken) {
        loadHomeTimeline(subscriber, accessToken, new HashMap<String, String>());
    }

    public void loadStatusInfo(Subscriber<Status> subscriber, String accessToken, long id) {
        mWeiboService.statusShow(accessToken, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void loadStatusComments(Subscriber<CursorResult> subscriber, String accessToken, long id) {
        mWeiboService.commentsShow(accessToken, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void loadStatusReposts(Subscriber<CursorResult> subscriber, String accessToken, long id) {
        mWeiboService.repostTimeline(accessToken, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void loadUserInfo(Subscriber<User> subscriber, String accessToken, long uid) {
        mWeiboService.userShow(accessToken, uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void loadUserInfo(Subscriber<User> subscriber, String accessToken, String screenName) {
        mWeiboService.userShow(accessToken, screenName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void loadFriends(Subscriber<CursorResult> subscriber, String accessToken, long uid) {
        mWeiboService.friends(accessToken, uid, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

}
