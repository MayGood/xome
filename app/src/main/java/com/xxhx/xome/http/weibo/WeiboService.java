package com.xxhx.xome.http.weibo;

import com.xxhx.xome.http.weibo.entity.AccessToken;
import com.xxhx.xome.http.weibo.entity.CursorResult;
import com.xxhx.xome.http.weibo.entity.Status;
import com.xxhx.xome.http.weibo.entity.User;
import java.util.List;
import java.util.Map;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by xxhx on 2016/9/28.
 */
public interface WeiboService {

    //@FormUrlEncoded
    //@POST("access_token")
    //Call<AccessToken> accessToken(@Field("client_id") String clientId, @Field("client_secret") String clientSecret, @Field("grant_type") String grantType,
    //        @Field("redirect_uri") String redirectUri, @Field("code") String code);

    //START 粉丝服务
    //END 粉丝服务

    //START 微博
    /**
     *
     */
    @GET("2/statuses/home_timeline.json")
    Observable<CursorResult> homeTimeline(@Query("access_token") String accessToken, @QueryMap Map<String, String> options);

    /**
     * 获取指定微博的转发微博列表
     *
     * @param accessToken 采用OAuth授权方式为必填参数，OAuth授权后获得
     * @param id 需要查询的微博ID
     */
    @GET("2/statuses/repost_timeline.json")
    Observable<CursorResult> repostTimeline(@Query("access_token") String accessToken, @Query("id") long id);

    /**
     * 根据微博ID获取单条微博内容
     *
     * @param accessToken 采用OAuth授权方式为必填参数，OAuth授权后获得
     * @param id 需要获取的微博ID
     */
    @GET("2/statuses/show.json")
    Observable<Status> statusShow(@Query("access_token") String accessToken, @Query("id") long id);
    //END 微博

    //START 评论
    /**
     * 根据微博ID返回某条微博的评论列表
     *
     * @param accessToken 采用OAuth授权方式为必填参数，OAuth授权后获得
     * @param id 需要查询的微博ID
     */
    @GET("2/comments/show.json")
    Observable<CursorResult> commentsShow(@Query("access_token") String accessToken, @Query("id") long id);
    //END 评论

    //START 用户
    /**
     * 根据用户ID获取用户信息
     *
     * @param accessToken 采用OAuth授权方式为必填参数，OAuth授权后获得
     * @param uid 需要查询的用户ID
     */
    @GET("2/users/show.json")
    Observable<User> userShow(@Query("access_token") String accessToken, @Query("uid") long uid);
    /**
     * 根据用户ID获取用户信息
     *
     * @param accessToken 采用OAuth授权方式为必填参数，OAuth授权后获得
     * @param screenName 需要查询的用户昵称
     */
    @GET("2/users/show.json")
    Observable<User> userShow(@Query("access_token") String accessToken, @Query("screen_name") String screenName);
    //END 用户

    //START 关系
    @GET("2/friendships/friends.json")
    Observable<CursorResult> friends(@Query("access_token") String accessToken, @Query("uid") long uid, @Query("cursor") int cursor);
    //END 关系

    //START 账号
    //END 账号

    //START 收藏
    //END 收藏

    //START 搜索
    //END 搜索

    //START 提醒
    //END 提醒

    //START 短链
    //END 短链

    //START 公共服务
    //END 公共服务

    //START 位置服务
    //END 位置服务

    //START 地理信息
    //END 地理信息

    //START OAuth2
    /**
     * OAuth2的access_token接口
     *
     * @param clientId 申请应用时分配的AppKey
     * @param clientSecret 申请应用时分配的AppSecret
     * @param grantType 请求的类型，填写authorization_code
     * @param redirectUri 回调地址，需需与注册应用里的回调地址一致
     * @param code 调用authorize获得的code值
     */
    @FormUrlEncoded
    @POST("oauth2/access_token")
    Observable<AccessToken> accessToken(@Field("client_id") String clientId, @Field("client_secret") String clientSecret, @Field("grant_type") String grantType,
            @Field("redirect_uri") String redirectUri, @Field("code") String code);
    //END OAuth2
}
