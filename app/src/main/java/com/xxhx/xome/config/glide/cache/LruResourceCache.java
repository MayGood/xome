package com.xxhx.xome.config.glide.cache;

/**
 * Created by xxhx on 2016/11/11.
 */

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

/**
 * An LRU in memory cache for {@link com.bumptech.glide.load.engine.Resource}s.
 */
public class LruResourceCache extends LruCache<String, Bitmap> {
    //private ResourceRemovedListener listener;

    /**
     * Constructor for LruResourceCache.
     *
     * @param size The maximum size in bytes the in memory cache can use.
     */
    public LruResourceCache(int size) {
        super(size);
    }

    //public void setResourceRemovedListener(ResourceRemovedListener listener) {
    //    this.listener = listener;
    //}
    //
    //protected void onItemEvicted(String key,Bitmap item) {
    //    if (listener != null) {
    //        listener.onResourceRemoved(item);
    //    }
    //}

    @SuppressLint("InlinedApi")
    public void trimMemory(int level) {
        if (level >= android.content.ComponentCallbacks2.TRIM_MEMORY_MODERATE) {
            // Nearing middle of list of cached background apps
            // Evict our entire bitmap cache
            clearMemory();
        } else if (level >= android.content.ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) {
            // Entering list of cached background apps
            // Evict oldest half of our bitmap cache
            trimToSize(getCurrentSize() / 2);
        }
    }
}
