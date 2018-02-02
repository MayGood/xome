package com.xxhx.xome.helper;

import android.content.Context;
import android.content.SharedPreferences;
import com.xxhx.xome.http.weibo.entity.AccessToken;

/**
 * Created by xxhx on 2016/9/28.
 */
public class PreferHelper {
    private static final String sPreferWeiboAuthInfo = "weibo_auth_info";

    public static void setWeiboAccessToken(AccessToken token) {
        SharedPreferences preferWeiboAuthInfo = ContextHelper.getSharedPreferences(sPreferWeiboAuthInfo,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferWeiboAuthInfo.edit();
        editor.putString("access_token", token.getAccess_token());
        editor.putString("expires_in", token.getExpires_in());
        editor.putString("remind_in", token.getRemind_in());
        editor.putString("uid", token.getUid());
        editor.commit();
    }

    public static AccessToken getWeiboAccessToken() {
        AccessToken token = new AccessToken();
        SharedPreferences preferWeiboAuthInfo = ContextHelper.getSharedPreferences(sPreferWeiboAuthInfo,
                Context.MODE_PRIVATE);
        token.setAccess_token(preferWeiboAuthInfo.getString("access_token", null));
        token.setExpires_in(preferWeiboAuthInfo.getString("expires_in", null));
        token.setRemind_in(preferWeiboAuthInfo.getString("remind_in", null));
        token.setUid(preferWeiboAuthInfo.getString("uid", null));
        return token;
    }
}
