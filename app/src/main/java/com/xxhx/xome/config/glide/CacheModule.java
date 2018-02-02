package com.xxhx.xome.config.glide;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.xxhx.xome.config.glide.cache.CustomDiskCacheFactory;
import com.xxhx.xome.helper.ImageHelper;
import com.xxhx.xome.http.weibo.entity.Status;
import java.io.File;
import java.io.InputStream;

/**
 * Created by xxhx on 2016/10/17.
 */
public class CacheModule implements GlideModule {
    private static DiskCache.Factory sDiskCacheFactory;

    public static DiskCache.Factory getDiskCacheFactory() {
        return sDiskCacheFactory;
    }

    @Override
    public void applyOptions(final Context context, GlideBuilder builder) {
        if(sDiskCacheFactory == null) {
            sDiskCacheFactory = new CustomDiskCacheFactory(context, ImageHelper.DiskCacheName, DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE);
        }
        builder.setDiskCache(sDiskCacheFactory);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        //glide.register(Status.class, InputStream.class, new OkHttpUrlLoader.Factory());
        //(String.class, InputStream.class, new StreamStringLoader.Factory());
    }
}
