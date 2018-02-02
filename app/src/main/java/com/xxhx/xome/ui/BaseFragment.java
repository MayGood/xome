package com.xxhx.xome.ui;

import android.support.v4.app.Fragment;

/**
 * Created by xxhx on 2016/9/19.
 */
public abstract class BaseFragment extends Fragment {
    private String title;
    private int themeColor = 0;
    private int themeDarkColor = 0;

    public abstract String retrieveTitle();
    public abstract int retrieveThemeColor();
    public abstract int retrieveThemeDarkColor();

    public final String getTitle() {
        if (title == null) {
            title = retrieveTitle();
        }
        return title;
    }

    public final int getThemeColor() {
        if(themeColor == 0) {
            themeColor = retrieveThemeColor();
        }
        return themeColor;
    }

    public final int getThemeDarkColor() {
        if(themeDarkColor == 0) {
            themeDarkColor = retrieveThemeDarkColor();
        }
        return themeDarkColor;
    }
}
