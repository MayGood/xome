package com.xxhx.xome.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by xxhx on 2017/5/15.
 */

public class XImageView extends View {
    private static final String TAG = "XImageView";

    private static final int MESSAGE_DECODE_CACHE_BITMAP = 1;
    private static final int MESSAGE_DECODE_FITXY_BITMAP = 2;

    private void decodeCachedBitmap() {
        if(mDisplayRect.isEmpty()) {
            int w = (int) (getWidth() / mScale);
            int h = (int) (getHeight() / mScale);
            mDisplayRect.set(0, 0, w > mImageWidth ? mImageWidth : w, h > mImageHeight ? mImageHeight : h);
        }
        final int width = mDisplayRect.width();
        final int height = mDisplayRect.height();
        int leftEdge = mDisplayRect.left > width ? (mDisplayRect.left - width) : 0;
        int rightEdge = (mDisplayRect.right + width) > mImageWidth ? mImageWidth : (mDisplayRect.right + width);
        int topEdge = mDisplayRect.top > height ? (mDisplayRect.top - height) : 0;
        int bottomEdge = (mDisplayRect.bottom + height) > mImageHeight ? mImageHeight : (mDisplayRect.bottom + height);
        Rect rect = new Rect(leftEdge, topEdge, rightEdge, bottomEdge);
        Bitmap bitmap = mBitmapRegionDecoder.decodeRegion(rect, mDecodeOptions);
        if(mCachedBitmap != null) {
            mCachedBitmap.recycle();
        }
        mCachedBitmap = bitmap;
        mDecodeRect.set(rect);



        //mMatrix.reset();
        //mMatrix.setScale(mScale, mScale);
        //mMatrix.postTranslate((mDisplayRect.left - mDecodeRect.left) * mScale, (mDisplayRect.top - mDecodeRect.top) * mScale);
        //mMatrix.postTranslate((width - (mCachedRightEdge - mCachedLeftEdge) * mScale) / 2, mCachedTopEdge);
    }

    private Handler mDecodeHandler;
    private Handler.Callback mDecodeCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == MESSAGE_DECODE_CACHE_BITMAP) {
                decodeCachedBitmap();

                postInvalidate();
                //invalidate();
                //Log.i(TAG, "decode bitmap [" + mCachedLeftEdge + "," + mCachedTopEdge + "," + mCachedRightEdge + "," + mCachedBottomEdge + "]");
                return true;
            }
            else if(msg.what == MESSAGE_DECODE_FITXY_BITMAP) {
                int expW = (int) Math.floor(Math.log((float) mImageWidth / (float) getWidth()) / Math.log(2));
                int expH = (int) Math.floor(Math.log((float) mImageHeight / (float) getHeight()) / Math.log(2));
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = (int) Math.pow(2, Math.max(expW, expH));
                mFitXYBitmap = mBitmapRegionDecoder.decodeRegion(new Rect(0, 0, mImageWidth, mImageHeight), options);
                mFitXYMatrix.setScale((float) (mImageWidth * mScale) / (float) mFitXYBitmap.getWidth(), (float) (mImageHeight * mScale) / (float) mFitXYBitmap.getHeight());
                postInvalidate();
            }
            return false;
        }
    };

    private final Rect mDecodeRect = new Rect();
    private final Rect mDisplayRect = new Rect();

    private float mScale = 1;
    private boolean mCacheValid;
    private Bitmap mCachedBitmap;
    private BitmapFactory.Options mDecodeOptions;
    private final Matrix mMatrix = new Matrix();
    private Bitmap mFitXYBitmap;
    private final Matrix mFitXYMatrix = new Matrix();
    //private int mCachedLeftEdge;
    //private int mCachedTopEdge;
    //private int mCachedRightEdge;
    //private int mCachedBottomEdge;
    private Paint mPaint;
    private BitmapRegionDecoder mBitmapRegionDecoder;
    private int mImageWidth;
    private int mImageHeight;

    /**
     * 该视图横向可滚动的像素数（基本宽度-可见宽度）
     */
    private int mScrollRangeX;

    /**
     * 该视图纵向可滚动的像素数（基本高度-可见高度）
     */
    private int mScrollRangeY;

    /**
     * 为克服顺滑滚动难题所需的
     */
    private Scroller mScroller;

    private int mTouchSlopSquare;
    private int mDoubleTapTouchSlopSquare;
    private int mDoubleTapSlopSquare;
    private int mMinimumFlingVelocity;
    private int mMaximumFlingVelocity;

    private static final int DECODE_TIMEOUT = 200;
    private static final int LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
    private static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
    private static final int DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
    private static final int DOUBLE_TAP_MIN_TIME = 40;

    //private final Handler mHandler;
    //private final OnGestureListener mListener;
    //private OnDoubleTapListener mDoubleTapListener;
    private OnContextClickListener mContextClickListener;

    private boolean mStillDown;
    private boolean mDeferConfirmSingleTap;
    private boolean mInLongPress;
    private boolean mInContextClick;
    private boolean mAlwaysInTapRegion;
    private boolean mAlwaysInBiggerTapRegion;
    private boolean mIgnoreNextUpEvent;

    private MotionEvent mCurrentDownEvent;
    private MotionEvent mPreviousUpEvent;

    /**
     * True when the user is still touching for the second tap (down, move, and
     * up events). Can only be true if there is a double tap listener attached.
     */
    private boolean mIsDoubleTapping;

    private float mLastFocusX;
    private float mLastFocusY;
    private float mDownFocusX;
    private float mDownFocusY;

    private boolean mIsLongpressEnabled = true;

    /**
     * Determines speed during touch scrolling
     */
    private VelocityTracker mVelocityTracker;

    // constants for Message.what used by GestureHandler below
    private static final int SHOW_PRESS = 1;
    private static final int LONG_PRESS = 2;
    private static final int TAP = 3;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_PRESS:
                    onShowPress(mCurrentDownEvent);
                    break;

                case LONG_PRESS:
                    dispatchLongPress();
                    break;

                case TAP:
                    // If the user's finger is still down, do not count it as a tap
                    if (!mStillDown) {
                        onSingleTapConfirmed(mCurrentDownEvent);
                    } else {
                        mDeferConfirmSingleTap = true;
                    }
                    break;

                default:
                    throw new RuntimeException("Unknown message " + msg); //never
            }
        }
    };

    private void dispatchLongPress() {
        mHandler.removeMessages(TAP);
        mDeferConfirmSingleTap = false;
        mInLongPress = true;
        onLongPress(mCurrentDownEvent);
    }

    public XImageView(Context context) {
        super(context);
        init(context);
    }

    public XImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public XImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // 绘制相关
        mPaint = new Paint();
        HandlerThread thread = new HandlerThread("decode");
        thread.start();
        mDecodeHandler = new Handler(thread.getLooper(), mDecodeCallback);

        // 缓存 ViewConfiguration 的值
        int touchSlop, doubleTapSlop, doubleTapTouchSlop;
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        touchSlop = configuration.getScaledTouchSlop();
        doubleTapSlop = configuration.getScaledDoubleTapSlop();
        doubleTapTouchSlop = touchSlop;
        mTouchSlopSquare = touchSlop * touchSlop;
        mDoubleTapTouchSlopSquare = doubleTapTouchSlop * doubleTapTouchSlop;
        mDoubleTapSlopSquare = doubleTapSlop * doubleTapSlop;
        mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();

        // 修改这个将被渲染的视图
        setWillNotDraw(false);

        // 其他设置
        mScroller = new Scroller(context);
    }

    public void setImageFile(File file) {
        if(file != null && file.exists()) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                mImageWidth = options.outWidth;
                mImageHeight = options.outHeight;

                if(mBitmapRegionDecoder != null && !mBitmapRegionDecoder.isRecycled()) {
                    mBitmapRegionDecoder.recycle();
                    mBitmapRegionDecoder = null;
                }
                mBitmapRegionDecoder = BitmapRegionDecoder.newInstance(new FileInputStream(file), false);
                mDecodeOptions = new BitmapFactory.Options();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        requestLayout();
        invalidate();
    }

    public void setScale(float scale, float ex, float ey) {
        if(scale == mScale) return;
        float relativeX = mDisplayRect.left + ex / mScale;
        float relativeY = mDisplayRect.top + ey / mScale;
        float dScale = scale / mScale;
        int newLeft = (int) (relativeX - (relativeX - mDisplayRect.left) / dScale);
        int newRight = (int) ((mDisplayRect.right - relativeX) / dScale + relativeX);
        int newTop = (int) (relativeY - (relativeY - mDisplayRect.top) / dScale);
        int newBottom = (int) ((mDisplayRect.bottom - relativeY) / dScale + relativeY);
        mScale = scale;
        mDisplayRect.set(newLeft, newTop, newRight, newBottom);
        Log.i(TAG, "relativeX:" + relativeX + " relativeY:" + relativeY + " dScale:" + dScale + " display:" + mDisplayRect.toShortString());
        invalidate();
    }

    public void smoothScaleTo(final float scale, final float ex, final float ey) {
        if(scale == mScale) return;
        if(mImageWidth * scale > getWidth()) {
            mScrollRangeX = (int) (mImageWidth * scale - getWidth());
        }
        else {
            mScrollRangeX = 0;
        }
        if(mImageHeight * scale > getHeight()) {
            mScrollRangeY = (int) (mImageHeight * scale - getHeight());
        }
        else {
            mScrollRangeY = 0;
        }
        final float relativeX = mDisplayRect.left + ex / mScale;
        final float relativeY = mDisplayRect.top + ey / mScale;
        final ValueAnimator animator = ValueAnimator.ofFloat(mScale, scale);
        animator.setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //mScale = scale;
                //mFitXYMatrix.setScale((float) (mImageWidth * mScale) / (float) mFitXYBitmap.getWidth(), (float) (mImageHeight * mScale) / (float) mFitXYBitmap.getHeight());
                //requestLayout();
                //invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = (float) animation.getAnimatedValue();
                float dScale = scale / mScale;
                int newLeft = (int) (relativeX - (relativeX - mDisplayRect.left) / dScale);
                int newRight = (int) ((mDisplayRect.right - relativeX) / dScale + relativeX);
                int newTop = (int) (relativeY - (relativeY - mDisplayRect.top) / dScale);
                int newBottom = (int) ((mDisplayRect.bottom - relativeY) / dScale + relativeY);
                mScale = scale;
                mDisplayRect.set(newLeft, newTop, newRight, newBottom);
                Log.i(TAG, "relativeX:" + relativeX + " relativeY:" + relativeY + " dScale:" + dScale + " display:" + mDisplayRect.toShortString());
                invalidate();
            }
        });
        animator.start();
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        final int maxWidth = (int) (mImageWidth * mScale + getPaddingLeft() + getPaddingRight());

        int result = 0;
        if(specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        else {
            if(specMode == MeasureSpec.AT_MOST) {
                result = Math.min(maxWidth, specSize);
            }
            else {
                result = maxWidth;
            }
        }

        if(maxWidth > result) {
            mScrollRangeX = maxWidth - result;
        }
        else {
            mScrollRangeX = 0;
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        final int maxHeight = (int) (mImageHeight * mScale + getPaddingTop() + getPaddingBottom());

        int result = 0;
        if(specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        else {
            if(specMode == MeasureSpec.AT_MOST) {
                result = Math.min(maxHeight, specSize);
            }
            else {
                result = maxHeight;
            }
        }

        if(maxHeight > result) {
            mScrollRangeY = maxHeight - result;
        }
        else {
            mScrollRangeY = 0;
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mBitmapRegionDecoder == null) {
            return;
        }
        if(mFitXYBitmap == null) {
            mDecodeHandler.sendEmptyMessage(MESSAGE_DECODE_FITXY_BITMAP);
        }
        if (mCachedBitmap == null) {
            decodeCachedBitmap();
        }
        if(mDecodeRect.contains(mDisplayRect) && !mCachedBitmap.isRecycled()) {
            Log.i(TAG, "onDraw contains " + mDisplayRect.toShortString() + " / " + mDecodeRect.toShortString());
            mCacheValid = true;
            mMatrix.reset();
            mMatrix.setScale(mScale, mScale);
            mMatrix.postTranslate((mDecodeRect.left - mDisplayRect.left) * mScale, (mDecodeRect.top - mDisplayRect.top) * mScale);
            canvas.drawBitmap(mCachedBitmap, mMatrix, mPaint);
        }
        else {
            Log.i(TAG, "onDraw !contains" + mDisplayRect.toShortString() + " / " + mDecodeRect.toShortString());
            mCacheValid = false;
            if(mFitXYBitmap != null) {
                mFitXYMatrix.setScale((float) (mImageWidth * mScale) / (float) mFitXYBitmap.getWidth(), (float) (mImageHeight * mScale) / (float) mFitXYBitmap.getHeight());
                mFitXYMatrix.postTranslate(-mDisplayRect.left * mScale, -mDisplayRect.top * mScale);
                canvas.drawBitmap(mFitXYBitmap, mFitXYMatrix, mPaint);
            }
        }

    }

    /**
     * 以特定速率沿水平/垂直方向猛滑动视图
     * @param velocityX velocity int pixels per second along X axis
     * @param velocityY velocity int pixels per second along Y axis
     */
    private void fling(int velocityX, int velocityY) {
        if(mScrollRangeX == 0 && mScrollRangeY == 0) {
            return;
        }
        mScroller.fling(getScrollX(), getScrollY(), velocityX, velocityY, 0, mScrollRangeX, 0, mScrollRangeY);
        invalidate();
        //Log.i(TAG, "fling " + velocityX + "," + velocityY);
    }

    /**
     * Notified when a tap occurs with the down {@link MotionEvent}
     * that triggered it. This will be triggered immediately for
     * every down event. All other events should be preceded by this.
     *
     * @param e The down motion event.
     */
    private boolean onDown(MotionEvent e) {
        //Log.i(TAG, "onDown");
        mDecodeHandler.removeMessages(MESSAGE_DECODE_CACHE_BITMAP);
        if(!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
        return true;
    }

    /**
     * The user has performed a down {@link MotionEvent} and not performed
     * a move or up yet. This event is commonly used to provide visual
     * feedback to the user to let them know that their action has been
     * recognized i.e. highlight an element.
     *
     * @param e The down motion event
     */
    private void onShowPress(MotionEvent e) {
        //Log.i(TAG, "onShowPress");
    }

    /**
     * Notified when a tap occurs with the up {@link MotionEvent}
     * that triggered it.
     *
     * @param e The up motion event that completed the first tap
     * @return true if the event is consumed, else false
     */
    private boolean onSingleTapUp(MotionEvent e) {
        //Log.i(TAG, "onSingleTapUp");
        return true;
    }

    /**
     * Notified when a single-tap occurs.
     * <p>
     * Unlike {@link XImageView#onSingleTapUp(MotionEvent)}, this
     * will only be called after the detector is confident that the user's
     * first tap is not followed by a second tap leading to a double-tap
     * gesture.
     *
     * @param e The down motion event of the single-tap.
     * @return true if the event is consumed, else false
     */
    private boolean onSingleTapConfirmed(MotionEvent e) {
        //Log.i(TAG, "onSingleTapConfirmed");
        return true;
    }

    private boolean isConsideredDoubleTap(MotionEvent firstDown, MotionEvent firstUp,
            MotionEvent secondDown) {
        if (!mAlwaysInBiggerTapRegion) {
            return false;
        }

        final long deltaTime = secondDown.getEventTime() - firstUp.getEventTime();
        if (deltaTime > DOUBLE_TAP_TIMEOUT || deltaTime < DOUBLE_TAP_MIN_TIME) {
            return false;
        }

        int deltaX = (int) firstDown.getX() - (int) secondDown.getX();
        int deltaY = (int) firstDown.getY() - (int) secondDown.getY();
        return (deltaX * deltaX + deltaY * deltaY < mDoubleTapSlopSquare);
    }

    /**
     * Notified when a double-tap occurs.
     *
     * @param e The down motion event of the first tap of the double-tap.
     * @return true if the event is consumed, else false
     */
    private boolean onDoubleTap(MotionEvent e) {
        //Log.i(TAG, "onDoubleTap");
        smoothScaleTo(mScale + 1, e.getX(), e.getY());
        return true;
    }

    /**
     * Notified when an event within a double-tap gesture occurs, including
     * the down, move, and up events.
     *
     * @param e The motion event that occurred during the double-tap gesture.
     * @return true if the event is consumed, else false
     */
    private boolean onDoubleTapEvent(MotionEvent e) {
        //Log.i(TAG, "onDoubleTapEvent");
        return true;
    }

    /**
     * Notified when a scroll occurs with the initial on down {@link MotionEvent} and the
     * current move {@link MotionEvent}. The distance in x and y is also supplied for
     * convenience.
     *
     * @param e1 The first down motion event that started the scrolling.
     * @param e2 The move motion event that triggered the current onScroll.
     * @param distanceX The distance along the X axis that has been scrolled since the last
     *              call to onScroll. This is NOT the distance between {@code e1}
     *              and {@code e2}.
     * @param distanceY The distance along the Y axis that has been scrolled since the last
     *              call to onScroll. This is NOT the distance between {@code e1}
     *              and {@code e2}.
     * @return true if the event is consumed, else false
     */
    private boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //Log.i(TAG, "onScroll " + distanceX + ", " + distanceY);
        if(!mDisplayRect.isEmpty()) {
            int offsetX = 0;
            int offsetY = 0;
            mDisplayRect.offset((int) (distanceX / mScale), (int) (distanceY / mScale));
            if(mDisplayRect.left < 0) {
                offsetX = -mDisplayRect.left;
            }
            else if(mDisplayRect.right > mImageWidth) {
                offsetX = mImageWidth - mDisplayRect.right;
            }
            if(mDisplayRect.top < 0) {
                offsetY = -mDisplayRect.top;
            }
            else if(mDisplayRect.bottom > mImageHeight) {
                offsetY = mImageHeight - mDisplayRect.bottom;
            }
            if(offsetX != 0 || offsetY != 0) {
                mDisplayRect.offset(offsetX, offsetY);
            }
        }
        invalidate();
        mDecodeHandler.removeMessages(MESSAGE_DECODE_CACHE_BITMAP);
        mDecodeHandler.sendEmptyMessageAtTime(MESSAGE_DECODE_CACHE_BITMAP, e2.getEventTime() + DECODE_TIMEOUT);
        return true;
    }

    /**
     * Notified when a long press occurs with the initial on down {@link MotionEvent}
     * that trigged it.
     *
     * @param e The initial on down motion event that started the longpress.
     */
    private void onLongPress(MotionEvent e) {
        //Log.i(TAG, "onLongPress");
    }

    private int lastX;
    private int lastY;
    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset() && !mDisplayRect.isEmpty()) {

            int currX = mScroller.getCurrX();
            int currY = mScroller.getCurrY();
            int offsetX = 0;
            int offsetY = 0;
            mDisplayRect.offset((int) ((currX - lastX) / mScale), (int) ((currY - lastY) / mScale));
            lastX = currX;
            lastY = currY;
            if(mDisplayRect.left < 0) {
                offsetX = -mDisplayRect.left;
            }
            else if(mDisplayRect.right > mImageWidth) {
                offsetX = mImageWidth - mDisplayRect.right;
            }
            if(mDisplayRect.top < 0) {
                offsetY = -mDisplayRect.top;
            }
            else if(mDisplayRect.bottom > mImageHeight) {
                offsetY = mImageHeight - mDisplayRect.bottom;
            }
            if(offsetX != 0 || offsetY != 0) {
                mDisplayRect.offset(offsetX, offsetY);
            }

            postInvalidate();
            //Log.i(TAG, "computeScroll [" + mScroller.getStartX() + "/" + mScroller.getStartY() + ", " + mScroller.getCurrX() + "/" + mScroller.getCurrY() + ", " + mScroller.getFinalX() + "/" + mScroller.getFinalY() + "]");
        }
    }

    /**
     * Notified of a fling event when it occurs with the initial on down {@link MotionEvent}
     * and the matching up {@link MotionEvent}. The calculated velocity is supplied along
     * the x and y axis in pixels per second.
     *
     * @param e1 The first down motion event that started the fling.
     * @param e2 The move motion event that triggered the current onFling.
     * @param velocityX The velocity of this fling measured in pixels per second
     *              along the x axis.
     * @param velocityY The velocity of this fling measured in pixels per second
     *              along the y axis.
     * @return true if the event is consumed, else false
     */
    private boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //Log.i(TAG, "onFling");
        if(mScrollRangeX == 0 && mScrollRangeY == 0) {
            return false;
        }
        lastX = lastY = 0;
        mScroller.fling(0, 0, (int) -velocityX, (int) -velocityY, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
        mDecodeHandler.removeMessages(MESSAGE_DECODE_CACHE_BITMAP);
        mDecodeHandler.sendEmptyMessageAtTime(MESSAGE_DECODE_CACHE_BITMAP, e2.getEventTime() + DECODE_TIMEOUT);
        invalidate();
        return true;
        //return false;
    }

    private void cancel() {
        mHandler.removeMessages(SHOW_PRESS);
        mHandler.removeMessages(LONG_PRESS);
        mHandler.removeMessages(TAP);
        mVelocityTracker.recycle();
        mVelocityTracker = null;
        mIsDoubleTapping = false;
        mStillDown = false;
        mAlwaysInTapRegion = false;
        mAlwaysInBiggerTapRegion = false;
        mDeferConfirmSingleTap = false;
        mInLongPress = false;
        mInContextClick = false;
        mIgnoreNextUpEvent = false;
    }

    private boolean scaleMode;
    private float lastGap;

    private float getMaxGap(MotionEvent ev) {
        int pc = ev.getPointerCount();
        float maxGap = 0;
        float sx = ev.getX(0);
        float sy = ev.getY(0);
        for(int i = 1; i < pc; i++) {
            float cx = ev.getX(i);
            float cy = ev.getY(i);
            float gap =
                    (float) Math.sqrt(((cx - sx) * (cx - sx) + (cy - sy) * (cy - sy)));
            if(gap > maxGap) maxGap = gap;
        }
        return maxGap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);

        final boolean pointerUp =
                (action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_UP;
        final int skipIndex = pointerUp ? ev.getActionIndex() : -1;

        // Determine focal point
        float sumX = 0, sumY = 0;
        final int count = ev.getPointerCount();
        for (int i = 0; i < count; i++) {
            if (skipIndex == i) continue;
            sumX += ev.getX(i);
            sumY += ev.getY(i);
        }
        final int div = pointerUp ? count - 1 : count;
        final float focusX = sumX / div;
        final float focusY = sumY / div;

        boolean handled = false;

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:
                scaleMode = true;
                lastGap = getMaxGap(ev);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if(ev.getPointerCount() == 2) {
                    scaleMode = false;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                boolean hadTapMessage = mHandler.hasMessages(TAP);
                if (hadTapMessage) mHandler.removeMessages(TAP);
                if ((mCurrentDownEvent != null) && (mPreviousUpEvent != null) && hadTapMessage &&
                        isConsideredDoubleTap(mCurrentDownEvent, mPreviousUpEvent, ev)) {
                    // This is a second tap
                    mIsDoubleTapping = true;
                    // Give a callback with the first tap of the double-tap
                    handled |= onDoubleTap(mCurrentDownEvent);
                    // Give a callback with down event of the double-tap
                    handled |= onDoubleTapEvent(ev);
                } else {
                    // This is a first tap
                    mHandler.sendEmptyMessageDelayed(TAP, DOUBLE_TAP_TIMEOUT);
                }

                mDownFocusX = mLastFocusX = focusX;
                mDownFocusY = mLastFocusY = focusY;
                if (mCurrentDownEvent != null) {
                    mCurrentDownEvent.recycle();
                }
                mCurrentDownEvent = MotionEvent.obtain(ev);
                mAlwaysInTapRegion = true;
                mAlwaysInBiggerTapRegion = true;
                mStillDown = true;
                mInLongPress = false;
                mDeferConfirmSingleTap = false;

                if (mIsLongpressEnabled) {
                    mHandler.removeMessages(LONG_PRESS);
                    mHandler.sendEmptyMessageAtTime(LONG_PRESS, mCurrentDownEvent.getDownTime()
                            + TAP_TIMEOUT + LONGPRESS_TIMEOUT);
                }
                mHandler.sendEmptyMessageAtTime(SHOW_PRESS, mCurrentDownEvent.getDownTime() + TAP_TIMEOUT);
                handled |= onDown(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                if(scaleMode) {
                    float maxGap = getMaxGap(ev);
                    setScale(maxGap / lastGap, focusX, focusY);
                    //lastGap = maxGap;
                    break;
                }
                if (mInLongPress || mInContextClick) {
                    break;
                }
                final float scrollX = mLastFocusX - focusX;
                final float scrollY = mLastFocusY - focusY;
                if (mIsDoubleTapping) {
                    // Give the move events of the double-tap
                    handled |= onDoubleTapEvent(ev);
                } else if (mAlwaysInTapRegion) {
                    final int deltaX = (int) (focusX - mDownFocusX);
                    final int deltaY = (int) (focusY - mDownFocusY);
                    int distance = (deltaX * deltaX) + (deltaY * deltaY);
                    if (distance > mTouchSlopSquare) {
                        handled = onScroll(mCurrentDownEvent, ev, scrollX, scrollY);
                        mLastFocusX = focusX;
                        mLastFocusY = focusY;
                        mAlwaysInTapRegion = false;
                        mHandler.removeMessages(TAP);
                        mHandler.removeMessages(SHOW_PRESS);
                        mHandler.removeMessages(LONG_PRESS);
                    }
                    if (distance > mDoubleTapTouchSlopSquare) {
                        mAlwaysInBiggerTapRegion = false;
                    }
                } else if ((Math.abs(scrollX) >= 1) || (Math.abs(scrollY) >= 1)) {
                    handled = onScroll(mCurrentDownEvent, ev, scrollX, scrollY);
                    mLastFocusX = focusX;
                    mLastFocusY = focusY;
                }
                break;
            case MotionEvent.ACTION_UP:
                mStillDown = false;
                MotionEvent currentUpEvent = MotionEvent.obtain(ev);
                if (mIsDoubleTapping) {
                    // Finally, give the up event of the double-tap
                    handled |= onDoubleTapEvent(ev);
                } else if (mInLongPress) {
                    mHandler.removeMessages(TAP);
                    mInLongPress = false;
                } else if (mAlwaysInTapRegion && !mIgnoreNextUpEvent) {
                    handled = onSingleTapUp(ev);
                    if (mDeferConfirmSingleTap) {
                        onSingleTapConfirmed(ev);
                    }
                } else if (!mIgnoreNextUpEvent) {

                    // A fling must travel the minimum tap distance
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    final int pointerId = ev.getPointerId(0);
                    velocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
                    final float velocityY = velocityTracker.getYVelocity(pointerId);
                    final float velocityX = velocityTracker.getXVelocity(pointerId);

                    if ((Math.abs(velocityY) > mMinimumFlingVelocity)
                            || (Math.abs(velocityX) > mMinimumFlingVelocity)){
                        handled = onFling(mCurrentDownEvent, ev, velocityX, velocityY);
                    }
                }
                if (mPreviousUpEvent != null) {
                    mPreviousUpEvent.recycle();
                }
                // Hold the event we obtained above - listeners may have changed the original.
                mPreviousUpEvent = currentUpEvent;
                if (mVelocityTracker != null) {
                    // This may have been cleared when we called out to the
                    // application above.
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                mIsDoubleTapping = false;
                mDeferConfirmSingleTap = false;
                mIgnoreNextUpEvent = false;
                mHandler.removeMessages(SHOW_PRESS);
                mHandler.removeMessages(LONG_PRESS);
                break;
            case MotionEvent.ACTION_CANCEL:
                cancel();
                break;
        }
        return handled;
    }
}
