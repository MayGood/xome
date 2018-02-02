package com.xxhx.xome.test;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Created by xxhx on 2017/10/11.
 */

public class CenterCropDrawable extends Drawable {

    private Bitmap mBitmap;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF mRectF;

    public CenterCropDrawable(Bitmap bitmap) {
        setBitmap(bitmap);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(mRectF, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        if(alpha != mPaint.getAlpha()) {
            mPaint.setAlpha(alpha);
            invalidateSelf();
        }
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mRectF = new RectF(bounds);
    }

    @Override
    public int getIntrinsicWidth() {
        if(mBitmap != null) {
            return mBitmap.getWidth();
        }
        return 0;
    }

    @Override
    public int getIntrinsicHeight() {
        if(mBitmap != null) {
            return mBitmap.getHeight();
        }
        return 0;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        final Shader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(shader);
        invalidateSelf();
    }
}
