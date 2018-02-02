package com.xxhx.xome.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xxhx.xome.config.glide.CacheModule;
import com.xxhx.xome.config.glide.cache.LruResourceCache;
import com.xxhx.xome.config.glide.cache.SafeKeyGenerator;
import java.io.File;
import java.io.InputStream;

/**
 * Created by xxhx on 2016/11/3.
 */

public class ImageHelper {
    public static final String DiskCacheName = "glide";

    private static final int SOURCE_LOCAL_THUMB = 1;
    private static final int SOURCE_LOCAL_LARGE = 2;
    private static final int SOURCE_SERVER = 3;

    private static File cacheDirectory;
    private static LruResourceCache statusImageCache;

    public static void cacheToDisk(String key, InputStream is) {
    }

    private static Bitmap decodeFromDiskCache(String key) {
        if(cacheDirectory == null || (!cacheDirectory.exists())) {
            File externalCacheDir = ContextHelper.getContext().getExternalCacheDir();
            cacheDirectory = new File(externalCacheDir, DiskCacheName);
            cacheDirectory.mkdir();
        }
        File diskImageFile = new File(cacheDirectory, key);
        if(diskImageFile != null && diskImageFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(diskImageFile.getAbsolutePath());
            if(statusImageCache == null) {
                statusImageCache = new LruResourceCache(300 * 1024 * 1024);
            }
            statusImageCache.put(key, bitmap);
            return bitmap;
        }
        return null;
    }

    private static Bitmap loadFromCache(String key) {
        if (statusImageCache != null && statusImageCache.contains(key)) {
            return statusImageCache.get(key);
        }
        return null;
    }

    private static File sCacheDirectory;

    public static void displayStatusImage(final Context context, String key, final ImageView imageView, boolean preferLarge) {
        //int source = 0;
        //if(CacheModule.getDiskCacheFactory() != null) {
        //    DiskCache diskCache = CacheModule.getDiskCacheFactory().build();
        //}
        //
        //if(sCacheDirectory == null) {
        //    sCacheDirectory = new File(context.getExternalCacheDir(), DiskCacheName);
        //}
        //String thumbKey = SafeKeyGenerator.getSafeKey(StatusHelper.getThumbPicUrl(key))+".0";
        //String largeKey = SafeKeyGenerator.getSafeKey(StatusHelper.getLargePicUrl(key))+".0";
        //File thumbFile = new File(sCacheDirectory, thumbKey);
        //File largeFile = new File(sCacheDirectory, largeKey);
        ////if(largeFile != null && largeFile.exists()) {
        ////    if(preferLarge) {
        ////        Glide.with(context).load(largeFile).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
        ////    }
        ////    else {
        ////        Glide.with(context).load(largeFile).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
        ////    }
        ////    source = SOURCE_LOCAL_LARGE;
        ////}
        ////else
        //if(thumbFile != null && thumbFile.exists()) {
        //    Glide.with(context).load(thumbFile).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
        //    source = SOURCE_LOCAL_THUMB;
        //}
        //else {
        //    source = SOURCE_SERVER;
        //}
        //SimpleTarget<File> target = new SimpleTarget<File>() {
        //    @Override
        //    public void onResourceReady(File resource,
        //            GlideAnimation<? super File> glideAnimation) {
        //        Glide.with(context).load(resource).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
        //    }
        //};
        //if(preferLarge && source != SOURCE_LOCAL_LARGE) {
        //    Glide.with(context).load(StatusHelper.getLargePicUrl(key)).downloadOnly(target);
        //    //Glide.with(context).load(StatusHelper.getLargePicUrl(key)).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(target);
        //}
        //else if(source == SOURCE_SERVER) {
        //    Glide.with(context).load(StatusHelper.getThumbPicUrl(key)).downloadOnly(target);
        //    //Glide.with(context).load(StatusHelper.getThumbPicUrl(key)).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(target);
        //}
        //Toast.makeText(context, "source " + source, Toast.LENGTH_SHORT).show();
    }
}
