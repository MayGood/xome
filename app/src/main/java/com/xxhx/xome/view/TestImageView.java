package com.xxhx.xome.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xxhx on 2017/5/8.
 */

public class TestImageView extends View {
    private Paint mPaint;
    private BitmapRegionDecoder mBitmapRegionDecoder;
    private int mImageWidth;
    private int mImageHeight;

    public TestImageView(Context context) {
        super(context);
    }

    public TestImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mImageWidth, mImageHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mBitmapRegionDecoder != null) {
            if(mPaint == null) {
                mPaint = new Paint();
            }
            Bitmap bitmap = mBitmapRegionDecoder.decodeRegion(new Rect(0,0,mImageWidth,mImageHeight), new BitmapFactory.Options());
            canvas.drawBitmap(bitmap, 0, 0, mPaint);
        }
    }
}
