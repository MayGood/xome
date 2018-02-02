package com.xxhx.xome.ui.activity.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.xxhx.xome.R;
import com.xxhx.xome.config.glide.status.StatusUrl;
import com.xxhx.xome.helper.ImageHelper;
import com.xxhx.xome.helper.StatusHelper;
import com.xxhx.xome.helper.ToastHelper;
import com.xxhx.xome.ui.BaseActivity;
import com.xxhx.xome.view.XImageView;
import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class WeiboPictureSlideActivity extends BaseActivity {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private ImageView mPicture;
    private XImageView mImage;

    private int mBitmapWidth;
    private int mBitmapHeight;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_weibo_picture_slide);

        mPicture = (ImageView) findViewById(R.id.picture);
        mImage = (XImageView) findViewById(R.id.image);

        final String key = getIntent().getStringExtra("key");

        //Glide.with(this).load(new StatusUrl(StatusHelper.getLargePicUrl(key))).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(mPicture);
        final FutureTarget<File> futureTarget = Glide.with(this).load(StatusHelper.getLargePicUrl(key)).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final File file = futureTarget.get();
                    if(file != null) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                        mBitmapWidth = options.outWidth;
                        mBitmapHeight = options.outHeight;
                        if(mBitmapHeight / mBitmapWidth >= 2) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mImage.setImageFile(file);
                                    mPicture.setVisibility(View.GONE);
                                    mImage.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Glide.with(WeiboPictureSlideActivity.this).load(file).into(mPicture);
                                    mPicture.setVisibility(View.VISIBLE);
                                    mImage.setVisibility(View.GONE);
                                }
                            });
                        }
                        //ToastHelper.toast(mBitmapWidth + "/" + mBitmapHeight);
                        //ToastHelper.toast(mPicture.getWidth() + "/" + mPicture.getHeight());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
