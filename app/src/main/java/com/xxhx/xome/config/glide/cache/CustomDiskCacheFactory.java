package com.xxhx.xome.config.glide.cache;

import android.content.Context;
import com.bumptech.glide.load.engine.cache.DiskCache;
import java.io.File;

/**
 * Creates an {@link com.bumptech.glide.disklrucache.DiskLruCache} based disk cache in the specified disk cache
 * directory.
 * <p/>
 * If you need to make I/O access before returning the cache directory use
 * the {@link CustomDiskCacheFactory#CustomDiskCacheFactory(CacheDirectoryGetter, int)} constructor variant.
 */
public class CustomDiskCacheFactory implements DiskCache.Factory {

    private final int diskCacheSize;
    private final CacheDirectoryGetter cacheDirectoryGetter;

    /**
     * Interface called out of UI thread to get the cache folder.
     */
    public interface CacheDirectoryGetter {
        File getCacheDirectory();
    }

    public CustomDiskCacheFactory(final String diskCacheFolder, int diskCacheSize) {
        this(new CacheDirectoryGetter() {
            @Override
            public File getCacheDirectory() {
                return new File(diskCacheFolder);
            }
        }, diskCacheSize);
    }

    public CustomDiskCacheFactory(final Context context, final String diskCacheName, int diskCacheSize) {
        this(new CacheDirectoryGetter() {
            @Override
            public File getCacheDirectory() {
                File cacheDirectory = context.getExternalCacheDir();
                if (cacheDirectory == null) {
                    return null;
                }
                if (diskCacheName != null) {
                    return new File(cacheDirectory, diskCacheName);
                }
                return cacheDirectory;
            }
        }, diskCacheSize);
    }

    public CustomDiskCacheFactory(final String diskCacheFolder, final String diskCacheName, int diskCacheSize) {
        this(new CacheDirectoryGetter() {
            @Override
            public File getCacheDirectory() {
                return new File(diskCacheFolder, diskCacheName);
            }
        }, diskCacheSize);
    }

    /**
     * When using this constructor {@link CacheDirectoryGetter#getCacheDirectory()} will be called out of UI thread,
     * allowing to do I/O access without performance impacts.
     *
     * @param cacheDirectoryGetter Interface called out of UI thread to get the cache folder.
     * @param diskCacheSize        Desired max bytes size for the LRU disk cache.
     */
    public CustomDiskCacheFactory(CacheDirectoryGetter cacheDirectoryGetter, int diskCacheSize) {
        this.diskCacheSize = diskCacheSize;
        this.cacheDirectoryGetter = cacheDirectoryGetter;
    }

    @Override
    public DiskCache build() {
        File cacheDir = cacheDirectoryGetter.getCacheDirectory();

        if (cacheDir == null) {
            return null;
        }

        if (!cacheDir.mkdirs() && (!cacheDir.exists() || !cacheDir.isDirectory())) {
            return null;
        }

        return DiskLruCacheWrapper.get(cacheDir, diskCacheSize);
    }
}
