package com.xxhx.exercise.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.BounceInterpolator;

/**
 * Created by xxhx on 2017/7/22.
 */

public class CountDownView extends KeepTextView {
    private int mCountStart;
    private int mCountEnd;
    private int mCount;

    private boolean mInCounting;

    private AnimatorSet mAnimatorSet;

    public interface OnCountDownListener {
        void onFinished();
    }

    private OnCountDownListener mOnCountDownListener;

    public CountDownView(Context context) {
        super(context);
        init();
    }

    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mInCounting = false;
        setGravity(Gravity.CENTER);

        AnimatorSet animIn = new AnimatorSet();
        AnimatorSet animOut = new AnimatorSet();

        animIn.play(ObjectAnimator.ofFloat(this, "scaleX", 0, 1)).with(ObjectAnimator.ofFloat(this, "scaleY", 0, 1));
        animIn.setDuration(600).setInterpolator(new BounceInterpolator());

        animOut.play(ObjectAnimator.ofFloat(this, "scaleX", 1, 0)).with(ObjectAnimator.ofFloat(this, "scaleY", 1, 0));
        animOut.setDuration(200);

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mCount--;
                if(mCount >= mCountEnd) {
                    countDown();
                }
                else {
                    mInCounting = false;
                    if(mOnCountDownListener != null) {
                        mOnCountDownListener.onFinished();
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimatorSet.play(animOut).after(200).after(animIn);
    }

    private void countDown() {
        if(mCount == mCountEnd) {
            setText("GO");
        }
        else {
            setText(String.valueOf(mCount));
        }
        mAnimatorSet.start();
    }

    public void startCountDown(int from, OnCountDownListener onCountDownListener) {
        startCountDown(from, 0, onCountDownListener);
    }

    public void startCountDown(int from, int to, OnCountDownListener onCountDownListener) {
        if(from <= to) {
            throw new IllegalArgumentException("\"from\" must be large than \"to\"");
        }

        if(mInCounting) {
            return;
        }

        mCountStart = from;
        mCountEnd = to;
        mCount = from;
        countDown();
        mInCounting = true;
        mOnCountDownListener = onCountDownListener;
    }
}
