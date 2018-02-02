package com.xxhx.exercise.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by xxhx on 2017/7/21.
 */

public class KeepTextView extends TextView {
    private static Typeface mTypeface;

    public KeepTextView(Context context) {
        super(context);
        init(context);
    }

    public KeepTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KeepTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if(mTypeface == null) {
            mTypeface = Typeface.createFromAsset(getResources().getAssets(), "fonts/Keep.ttf");
        }
        setTypeface(mTypeface);
    }
}
