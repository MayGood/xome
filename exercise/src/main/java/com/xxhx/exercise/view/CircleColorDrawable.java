package com.xxhx.exercise.view;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;

/**
 * Created by xxhx on 2017/7/23.
 */

public class CircleColorDrawable extends Drawable {
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int mBaseColor;
    private int mUseColor;

    public CircleColorDrawable(@ColorInt int color) {
        setColor(color);
    }

    public void setColor(@ColorInt int color) {
        if (mBaseColor != color || mUseColor != color) {
            mBaseColor = mUseColor = color;
            invalidateSelf();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if ((mUseColor >>> 24) != 0) {
            mPaint.setColor(mUseColor);
            Rect bounds = getBounds();
            int width = bounds.right - bounds.left;
            int height = bounds.bottom - bounds.top;
            float radius = 10;//(float) Math.sqrt(width * width + height * height) / 2;//Math.min(width, height) / 2;
            canvas.drawCircle((bounds.left + bounds.right) / 2, (bounds.top + bounds.height()) / 2, 1, mPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        alpha += alpha >> 7;   // make it 0..256
        final int baseAlpha = mBaseColor >>> 24;
        final int useAlpha = baseAlpha * alpha >> 8;
        final int useColor = (mBaseColor << 8 >>> 8) | (useAlpha << 24);
        if (mUseColor != useColor) {
            mUseColor = useColor;
            invalidateSelf();
        }
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        if (mPaint.getColorFilter() != null) {
            return PixelFormat.TRANSLUCENT;
        }

        switch (mUseColor >>> 24) {
            case 255:
                return PixelFormat.OPAQUE;
            case 0:
                return PixelFormat.TRANSPARENT;
        }
        return PixelFormat.TRANSLUCENT;
    }
}
