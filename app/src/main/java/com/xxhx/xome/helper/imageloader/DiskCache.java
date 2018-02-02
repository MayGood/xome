package com.xxhx.xome.helper.imageloader;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by xxhx on 2017/2/17.
 */

public class DiskCache {
    public static final int DEFAULT_BUFFER_SIZE = 32 * 1024; // 32 Kb
    public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.PNG;
    public static final int DEFAULT_COMPRESS_QUALITY = 100;

    private final File mRootCacheDirecoty;

    public DiskCache(@NonNull File cacheDirecoty) {
        if(cacheDirecoty == null || !cacheDirecoty.canWrite()) {
            throw new RuntimeException("DiskCache init failed!");
        }
        mRootCacheDirecoty = cacheDirecoty;
    }

    public File get(String key) {
        if(key != null && key.length() > 0 && !key.endsWith("/")) {
            File cacheDirectory = getCacheDirectory(key);
            if(cacheDirectory != null && cacheDirectory.exists() && cacheDirectory.isDirectory()) {
                File cacheFile = new File(cacheDirectory, getCacheKey(key));
                if(cacheFile.exists()) {
                    return cacheFile;
                }
            }
        }
        return null;
    }

    public void put(String key, Bitmap bitmap) {
        if(key != null && key.length() > 0 && !key.endsWith("/")) {
            File cacheDirectory = getCacheDirectory(key);
            if(!cacheDirectory.exists() || !cacheDirectory.isDirectory()) {
                cacheDirectory.mkdir();
            }
            File cacheFile = new File(cacheDirectory, getCacheKey(key));
            File tmpFile = new File(cacheFile.getAbsolutePath() + ".tmp");

            boolean result = false;
            try {
                BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(tmpFile), DEFAULT_BUFFER_SIZE);
                result = bitmap.compress(DEFAULT_COMPRESS_FORMAT, DEFAULT_COMPRESS_QUALITY, os);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if(result && !tmpFile.renameTo(cacheFile)) {
                    result = false;
                }
                if(!result) {
                    tmpFile.delete();
                }
            }
        }
        bitmap.recycle();
    }

    public boolean delete(String key) {
        File cacheFile = get(key);
        if(cacheFile != null && cacheFile.exists() && cacheFile.canWrite()) {
            return cacheFile.delete();
        }
        return false;
    }

    private File getCacheDirectory(String key) {
        int index = key.lastIndexOf("/");
        if(index > 0) {
            return new File(mRootCacheDirecoty, key.substring(0, index));
        }
        return mRootCacheDirecoty;
    }

    private String getCacheKey(String key) {
        int index = key.lastIndexOf("/");
        return key.substring(index + 1);
    }
}
