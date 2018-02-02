package com.xxhx.xome.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import com.xxhx.xome.R;

/**
 * Created by xxhx on 2016/9/21.
 */
public class BadgeView extends TextView {
    private final int FIXED_SIZE = 16; // in sp

    private DisplayMetrics mMetrics;

    public BadgeView(Context context) {
        super(context);
        init();
    }

    public BadgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mMetrics = getResources().getDisplayMetrics();
        setWidth((int) (FIXED_SIZE * mMetrics.density + 0.5));
        setHeight((int) (FIXED_SIZE * mMetrics.density + 0.5));
        setBackgroundResource(R.drawable.bg_badge);
        setGravity(Gravity.CENTER);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
        setTextColor(Color.WHITE);
        setIncludeFontPadding(false);
    }

    public void showBadgeCount(int count) {
        if(count <= 0) {
            setVisibility(View.GONE);
            return;
        }
        if(count > 99) {
            setText(R.string.badge_more);
        }
        else {
            setText(String.valueOf(count));
        }
        setVisibility(View.VISIBLE);
    }
}
