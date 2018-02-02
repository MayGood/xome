package com.xxhx.xome.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

/**
 * Created by xxhx on 2016/9/19.
 */
public class ContextHelper {
    private static Context mContext;

    private ContextHelper() {}

    public static void initInstance(Context context) {
        mContext = context;
    }

    public static Context getContext() {
        return mContext;
    }

    public static Resources getResources() {
        return mContext.getResources();
    }

    public static String getString(int id) {
        return getResources().getString(id);
    }

    public static String getString(int id, Object... formatArgs) {
        return getResources().getString(id, formatArgs);
    }

    public static int getColor(int id) {
        return getResources().getColor(id);
    }

    public static SharedPreferences getSharedPreferences(String name, int mode) {
        return mContext.getSharedPreferences(name, mode);
    }
}
