package com.xxhx.xome.ui.prof.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ImageView;

/**
 * Created by xxhx on 2017/7/6.
 */

public class FloatButton extends ImageView {

    private int mTouchSlopSquare;

    private boolean mInDrag;
    private float mTranslationX;
    private float mTranslationY;
    private float mDownX;
    private float mDownY;
    private MotionEvent mCurrentDownEvent;

    public FloatButton(Context context) {
        super(context);
        init(context);
    }

    public FloatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FloatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        int touchSlop = configuration.getScaledTouchSlop();
        mTouchSlopSquare = touchSlop * touchSlop;
    }

    private void onSingleTapConfirmed(MotionEvent e) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getRawX();
        final float y = event.getRawY();
        final int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_UP:
                if(mInDrag) {
                    mInDrag = false;
                }
                else {
                    onSingleTapConfirmed(event);
                }
                break;

            case MotionEvent.ACTION_DOWN:
                mDownX = x;
                mDownY = y;
                mTranslationX = getTranslationX();
                mTranslationY = getTranslationY();
                if(mCurrentDownEvent != null) {
                    mCurrentDownEvent.recycle();
                }
                mCurrentDownEvent = MotionEvent.obtain(event);
                break;

            case MotionEvent.ACTION_CANCEL:
                mInDrag = false;
                break;

            case MotionEvent.ACTION_MOVE:
                final int deltaX = (int) (x - mDownX);
                final int deltaY = (int) (y - mDownY);
                int distance = (deltaX * deltaX) + (deltaY * deltaY);
                if (distance > mTouchSlopSquare) {
                    mInDrag = true;
                }
                if(mInDrag) {
                    setTranslationX(mTranslationX + (x - mDownX));
                    setTranslationY(mTranslationY + (y - mDownY));
                    invalidate();
                }
                break;
        }

        return true;
    }
}
