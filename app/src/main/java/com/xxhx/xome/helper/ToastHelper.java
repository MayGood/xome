package com.xxhx.xome.helper;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Created by xxhx on 2017/2/26.
 */

public class ToastHelper {

    private static Handler mHandler;

    public static void toast(final String content) {
        if(mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ContextHelper.getContext(), content, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
