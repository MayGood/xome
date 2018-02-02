package com.xxhx.xome.ui.disc.wealth.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.xxhx.xome.R;

/**
 * Created by xxhx on 2017/7/11.
 */

public class WealthBarView extends FrameLayout {

    private TextView mPositiveTextView;
    private TextView mNegativeTextView;

    private double mPositiveValue;
    private double mNegativeValue;

    public WealthBarView(Context context) {
        super(context);
        init(context);
    }

    public WealthBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WealthBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPositiveTextView = new TextView(context);
        mNegativeTextView = new TextView(context);
        mPositiveTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        mPositiveTextView.setGravity(Gravity.CENTER);
        mPositiveTextView.setTextColor(Color.WHITE);
        mPositiveTextView.setBackgroundColor(getResources().getColor(R.color.positiveWealth));
        mNegativeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        mNegativeTextView.setGravity(Gravity.CENTER);
        mNegativeTextView.setTextColor(Color.WHITE);
        mNegativeTextView.setBackgroundColor(getResources().getColor(R.color.negativeWealth));

        removeAllViews();
        super.addView(mPositiveTextView, 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        super.addView(mNegativeTextView, 1, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setValues(60, 40);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
    }

    //@Override
    //public void addView(View child, int index, LayoutParams params) {
    //}

    public void setValues(double positive, double negative) {
        mPositiveValue = positive;
        mNegativeValue = negative;
        mPositiveTextView.setText(String.format("%.2f", mPositiveValue));
        mNegativeTextView.setText(String.format("%.2f", mNegativeValue));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int divider = (int) ((mNegativeValue) / (mPositiveValue + mNegativeValue) * l
                + (mPositiveValue) / (mPositiveValue + mNegativeValue) * r);
        mPositiveTextView.getLayoutParams().width = divider - l;
        mNegativeTextView.getLayoutParams().width = r - divider;
        mPositiveTextView.invalidate();
        mNegativeTextView.invalidate();
        mPositiveTextView.layout(l, t, divider, b);
        mNegativeTextView.layout(divider, t, r, b);
    }
}
