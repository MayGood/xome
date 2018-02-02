package com.xxhx.exercise.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by xxhx on 2017/7/22.
 */

public class PercentageLayout extends FrameLayout {
    private float mPercentage;

    public PercentageLayout(Context context) {
        super(context);
    }

    public PercentageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PercentageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPercentage(float percentage) {
        if(percentage > 1) mPercentage = 1;
        else if(percentage < 0) mPercentage = 0;
        else mPercentage = percentage;
        invalidate();
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int parentLeft = getPaddingLeft();
        final int parentRight = right - left - getPaddingRight();

        final int parentTop = getPaddingTop();
        final int parentBottom = bottom - top - getPaddingBottom();

        int childWidth = (int) ((right - left) * mPercentage);
        int count = getChildCount();
        for(int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int childLeft = parentLeft + lp.leftMargin;
            int childTop = parentTop + lp.topMargin;

            child.layout(childLeft, childTop, childLeft + childWidth, childTop + child.getMeasuredHeight());
        }
    }
}
