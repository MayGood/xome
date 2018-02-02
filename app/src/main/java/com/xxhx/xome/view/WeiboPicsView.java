package com.xxhx.xome.view;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xxhx.xome.R;
import com.xxhx.xome.config.glide.status.StatusUrl;
import com.xxhx.xome.helper.ImageHelper;
import com.xxhx.xome.helper.StatusHelper;
import com.xxhx.xome.http.weibo.entity.Status;
import com.xxhx.xome.ui.activity.image.WeiboPictureSlideActivity;
import java.util.List;

/**
 * Created by xxhx on 2016/10/11.
 */
public class WeiboPicsView extends LinearLayout {
    private ImageView single;
    private LinearLayout grid;
    private ImageView pic0;
    private ImageView pic1;
    private ImageView pic2;
    private LinearLayout row1;
    private ImageView pic3;
    private ImageView pic4;
    private ImageView pic5;
    private LinearLayout row2;
    private ImageView pic6;
    private ImageView pic7;
    private ImageView pic8;

    private Status mStatus;

    public WeiboPicsView(Context context) {
        super(context);
        init(context);
    }

    public WeiboPicsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WeiboPicsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_weibo_pics, this);
        //row0 = (LinearLayout) findViewById(R.id.row0);
        single = (ImageView) findViewById(R.id.single);
        grid = (LinearLayout) findViewById(R.id.grid);
        row1 = (LinearLayout) findViewById(R.id.row1);
        row2 = (LinearLayout) findViewById(R.id.row2);
        pic0 = (ImageView) findViewById(R.id.pic0);
        pic1 = (ImageView) findViewById(R.id.pic1);
        pic2 = (ImageView) findViewById(R.id.pic2);
        pic3 = (ImageView) findViewById(R.id.pic3);
        pic4 = (ImageView) findViewById(R.id.pic4);
        pic5 = (ImageView) findViewById(R.id.pic5);
        pic6 = (ImageView) findViewById(R.id.pic6);
        pic7 = (ImageView) findViewById(R.id.pic7);
        pic8 = (ImageView) findViewById(R.id.pic8);
    }

    public void setStatus(Status status) {
        mStatus = status;
        if(mStatus != null && mStatus.getPicCount() > 0) {
            setPics(mStatus.getPicKeys());
            setVisibility(VISIBLE);
        }
        else {
            setVisibility(GONE);
        }
    }

    private void startSlideActivity(Activity activity, View view, String key) {
        Intent intent = new Intent(activity, WeiboPictureSlideActivity.class);
        intent.putExtra("key", key);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            view.setTransitionName(key);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, view, key);
            activity.startActivity(intent, options.toBundle());
        }
        else {
            activity.startActivity(intent);
        }
    }

    private void setPics(final List<String> keys) {
        if(keys == null || keys.size() < 1 || keys.size() > 9) {
            setVisibility(GONE);
        }
        else if(keys.size() == 1) {
            //single.setImageResource(R.drawable.ic_weibo);
            Glide.with(getContext()).load(new StatusUrl(StatusHelper.getThumbPicUrl(keys.get(0)))).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(single);
            //ImageHelper.displayStatusImage(fragment.getContext(), keys.get(0), single, false);
            //SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {
            //    @Override
            //    public void onResourceReady(Bitmap resource,
            //            GlideAnimation<? super Bitmap> glideAnimation) {
            //        single.setImageBitmap(resource);
            //    }
            //};
            //Glide.with(fragment).load(StatusHelper.getLargePicUrl(keys.get(0))).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(target);
                    //.placeholder(R.drawable.picture_holder).error(R.drawable.ic_picture_failed).dontAnimate().override(200, 200).into(single);
            single.setVisibility(VISIBLE);
            grid.setVisibility(GONE);
            single.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //startSlideActivity(fragment.getActivity(), v, keys.get(0));
                    //Intent intent = new Intent(getContext(), WeiboPictureSlideActivity.class);
                    //intent.putExtra("key", keys.get(0));
                    //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    //    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(fragment.getActivity(), v, "picture");
                    //    getContext().startActivity(intent, options.toBundle());
                    //}
                    //else {
                    //    getContext().startActivity(intent);
                    //}
                }
            });
        }
        else {
            List<String> urls = StatusHelper.getThumbPicUrls(keys);
            switch (keys.size()) {
                case 2:
                    Glide.with(getContext()).load(urls.get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic0);
                    Glide.with(getContext()).load(urls.get(1)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic1);
                    pic0.setVisibility(VISIBLE);
                    pic1.setVisibility(VISIBLE);
                    pic2.setVisibility(GONE);
                    row1.setVisibility(GONE);
                    row2.setVisibility(GONE);
                    break;
                case 3:
                    Glide.with(getContext()).load(urls.get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic0);
                    Glide.with(getContext()).load(urls.get(1)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic1);
                    Glide.with(getContext()).load(urls.get(2)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic2);
                    pic0.setVisibility(VISIBLE);
                    pic1.setVisibility(VISIBLE);
                    pic2.setVisibility(VISIBLE);
                    row1.setVisibility(GONE);
                    row2.setVisibility(GONE);
                    break;
                case 4:
                    Glide.with(getContext()).load(urls.get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic0);
                    Glide.with(getContext()).load(urls.get(1)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic1);
                    Glide.with(getContext()).load(urls.get(2)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic3);
                    Glide.with(getContext()).load(urls.get(3)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic4);
                    pic0.setVisibility(VISIBLE);
                    pic1.setVisibility(VISIBLE);
                    pic2.setVisibility(GONE);
                    pic3.setVisibility(VISIBLE);
                    pic4.setVisibility(VISIBLE);
                    pic5.setVisibility(GONE);
                    row1.setVisibility(VISIBLE);
                    row2.setVisibility(GONE);
                    break;
                case 5:
                    Glide.with(getContext()).load(urls.get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic0);
                    Glide.with(getContext()).load(urls.get(1)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic1);
                    Glide.with(getContext()).load(urls.get(2)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic2);
                    Glide.with(getContext()).load(urls.get(3)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic3);
                    Glide.with(getContext()).load(urls.get(4)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic4);
                    pic0.setVisibility(VISIBLE);
                    pic1.setVisibility(VISIBLE);
                    pic2.setVisibility(VISIBLE);
                    pic3.setVisibility(VISIBLE);
                    pic4.setVisibility(VISIBLE);
                    pic5.setVisibility(GONE);
                    row1.setVisibility(VISIBLE);
                    row2.setVisibility(GONE);
                    break;
                case 6:
                    Glide.with(getContext()).load(urls.get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic0);
                    Glide.with(getContext()).load(urls.get(1)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic1);
                    Glide.with(getContext()).load(urls.get(2)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic2);
                    Glide.with(getContext()).load(urls.get(3)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic3);
                    Glide.with(getContext()).load(urls.get(4)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic4);
                    Glide.with(getContext()).load(urls.get(5)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic5);
                    pic0.setVisibility(VISIBLE);
                    pic1.setVisibility(VISIBLE);
                    pic2.setVisibility(VISIBLE);
                    pic3.setVisibility(VISIBLE);
                    pic4.setVisibility(VISIBLE);
                    pic5.setVisibility(VISIBLE);
                    row1.setVisibility(VISIBLE);
                    row2.setVisibility(GONE);
                    break;
                case 7:
                    Glide.with(getContext()).load(urls.get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic0);
                    Glide.with(getContext()).load(urls.get(1)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic1);
                    Glide.with(getContext()).load(urls.get(2)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic2);
                    Glide.with(getContext()).load(urls.get(3)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic3);
                    Glide.with(getContext()).load(urls.get(4)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic4);
                    Glide.with(getContext()).load(urls.get(5)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic5);
                    Glide.with(getContext()).load(urls.get(6)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic6);
                    pic0.setVisibility(VISIBLE);
                    pic1.setVisibility(VISIBLE);
                    pic2.setVisibility(VISIBLE);
                    pic3.setVisibility(VISIBLE);
                    pic4.setVisibility(VISIBLE);
                    pic5.setVisibility(VISIBLE);
                    pic6.setVisibility(VISIBLE);
                    pic7.setVisibility(GONE);
                    pic8.setVisibility(GONE);
                    row1.setVisibility(VISIBLE);
                    row2.setVisibility(VISIBLE);
                    break;
                case 8:
                    Glide.with(getContext()).load(urls.get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic0);
                    Glide.with(getContext()).load(urls.get(1)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic1);
                    Glide.with(getContext()).load(urls.get(2)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic2);
                    Glide.with(getContext()).load(urls.get(3)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic3);
                    Glide.with(getContext()).load(urls.get(4)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic4);
                    Glide.with(getContext()).load(urls.get(5)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic5);
                    Glide.with(getContext()).load(urls.get(6)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic6);
                    Glide.with(getContext()).load(urls.get(7)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic7);
                    pic0.setVisibility(VISIBLE);
                    pic1.setVisibility(VISIBLE);
                    pic2.setVisibility(VISIBLE);
                    pic3.setVisibility(VISIBLE);
                    pic4.setVisibility(VISIBLE);
                    pic5.setVisibility(VISIBLE);
                    pic6.setVisibility(VISIBLE);
                    pic7.setVisibility(VISIBLE);
                    pic8.setVisibility(GONE);
                    row1.setVisibility(VISIBLE);
                    row2.setVisibility(VISIBLE);
                    break;
                case 9:
                    Glide.with(getContext()).load(urls.get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic0);
                    Glide.with(getContext()).load(urls.get(1)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic1);
                    Glide.with(getContext()).load(urls.get(2)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic2);
                    Glide.with(getContext()).load(urls.get(3)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic3);
                    Glide.with(getContext()).load(urls.get(4)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic4);
                    Glide.with(getContext()).load(urls.get(5)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic5);
                    Glide.with(getContext()).load(urls.get(6)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic6);
                    Glide.with(getContext()).load(urls.get(7)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic7);
                    Glide.with(getContext()).load(urls.get(8)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic8);
                    //ImageHelper.displayStatusImage(fragment.getContext(), keys.get(0), pic0, false);
                    //ImageHelper.displayStatusImage(fragment.getContext(), keys.get(1), pic1, false);
                    //ImageHelper.displayStatusImage(fragment.getContext(), keys.get(2), pic2, false);
                    //ImageHelper.displayStatusImage(fragment.getContext(), keys.get(3), pic3, false);
                    //ImageHelper.displayStatusImage(fragment.getContext(), keys.get(4), pic4, false);
                    //ImageHelper.displayStatusImage(fragment.getContext(), keys.get(5), pic5, false);
                    //ImageHelper.displayStatusImage(fragment.getContext(), keys.get(6), pic6, false);
                    //ImageHelper.displayStatusImage(fragment.getContext(), keys.get(7), pic7, false);
                    //ImageHelper.displayStatusImage(fragment.getContext(), keys.get(8), pic8, false);
                    pic0.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //startSlideActivity(fragment.getActivity(), v, keys.get(0));
                            //Intent intent = new Intent(getContext(), WeiboPictureSlideActivity.class);
                            //intent.putExtra("key", keys.get(0));
                            //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            //    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(fragment.getActivity(), v, "picture");
                            //    getContext().startActivity(intent, options.toBundle());
                            //}
                            //else {
                            //    getContext().startActivity(intent);
                            //}
                        }
                    });
                    //Glide.with(fragment).load(urls.get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic0);
                    //Glide.with(fragment).load(urls.get(1)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic1);
                    //Glide.with(fragment).load(urls.get(2)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic2);
                    //Glide.with(fragment).load(urls.get(3)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic3);
                    //Glide.with(fragment).load(urls.get(4)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic4);
                    //Glide.with(fragment).load(urls.get(5)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic5);
                    //Glide.with(fragment).load(urls.get(6)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic6);
                    //Glide.with(fragment).load(urls.get(7)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic7);
                    //Glide.with(fragment).load(urls.get(8)).diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_picture_failed).into(pic8);
                    pic0.setVisibility(VISIBLE);
                    pic1.setVisibility(VISIBLE);
                    pic2.setVisibility(VISIBLE);
                    pic3.setVisibility(VISIBLE);
                    pic4.setVisibility(VISIBLE);
                    pic5.setVisibility(VISIBLE);
                    pic6.setVisibility(VISIBLE);
                    pic7.setVisibility(VISIBLE);
                    pic8.setVisibility(VISIBLE);
                    row1.setVisibility(VISIBLE);
                    row2.setVisibility(VISIBLE);
                    break;
            }
            //pic0.setOnClickListener(new OnClickListener() {
            //    @Override
            //    public void onClick(View v) {
            //        startSlideActivity(fragment.getActivity(), v, keys.get(0));
            //    }
            //});
            //pic1.setOnClickListener(new OnClickListener() {
            //    @Override
            //    public void onClick(View v) {
            //        startSlideActivity(fragment.getActivity(), v, keys.get(1));
            //    }
            //});
            //pic2.setOnClickListener(new OnClickListener() {
            //    @Override
            //    public void onClick(View v) {
            //        startSlideActivity(fragment.getActivity(), v, keys.get(2));
            //    }
            //});
            //pic3.setOnClickListener(new OnClickListener() {
            //    @Override
            //    public void onClick(View v) {
            //        startSlideActivity(fragment.getActivity(), v, keys.get(3));
            //    }
            //});
            //pic4.setOnClickListener(new OnClickListener() {
            //    @Override
            //    public void onClick(View v) {
            //        startSlideActivity(fragment.getActivity(), v, keys.get(4));
            //    }
            //});
            //pic5.setOnClickListener(new OnClickListener() {
            //    @Override
            //    public void onClick(View v) {
            //        startSlideActivity(fragment.getActivity(), v, keys.get(5));
            //    }
            //});
            //pic6.setOnClickListener(new OnClickListener() {
            //    @Override
            //    public void onClick(View v) {
            //        startSlideActivity(fragment.getActivity(), v, keys.get(6));
            //    }
            //});
            //pic7.setOnClickListener(new OnClickListener() {
            //    @Override
            //    public void onClick(View v) {
            //        startSlideActivity(fragment.getActivity(), v, keys.get(7));
            //    }
            //});
            //pic8.setOnClickListener(new OnClickListener() {
            //    @Override
            //    public void onClick(View v) {
            //        startSlideActivity(fragment.getActivity(), v, keys.get(8));
            //    }
            //});
            single.setVisibility(GONE);
            grid.setVisibility(VISIBLE);
        }
    }
}
