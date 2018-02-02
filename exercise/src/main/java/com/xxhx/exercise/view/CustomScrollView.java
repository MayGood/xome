package com.xxhx.exercise.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import com.xxhx.exercise.R;

/**
 * Created by xxhx on 2017/7/24.
 */

public class CustomScrollView extends ScrollView {
    private View mPlaceHolderView;

    private int mChildAlignBottom;

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        initPlaceHolderView();
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
        initPlaceHolderView();
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        initPlaceHolderView();
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        initPlaceHolderView();
    }

    private void initPlaceHolderView() {
        mPlaceHolderView = findViewById(R.id.place_holder);
    }

    public void setChildAlignBottom(int alignBottom) {
        mChildAlignBottom = alignBottom;
        requestLayout();
    }

    public int getPlaceHolderViewHeight() {
        if(mPlaceHolderView != null) return mPlaceHolderView.getHeight();
        return getHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mPlaceHolderView != null && mChildAlignBottom > 0) {
            int placeHolderHeight = getMeasuredHeight() - mChildAlignBottom;
            if(placeHolderHeight < 0) placeHolderHeight = 0;
            mPlaceHolderView.getLayoutParams().height = placeHolderHeight;
            mPlaceHolderView.invalidate();
            mPlaceHolderView.requestLayout();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(mPlaceHolderView != null) {
            int[] locationOnScreen = new int[2];
            mPlaceHolderView.getLocationOnScreen(locationOnScreen);
            if(ev.getRawY() < (locationOnScreen[1] + mPlaceHolderView.getMeasuredHeight())) {
                return false;
            }
        }

        return super.onTouchEvent(ev);
    }
}
