package com.xxhx.xome.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;

/**
 * Created by xxhx on 2016/9/30.
 */
public class CacheHelper {
    private static CacheHelper Instance;

    private File mCacheDir;

    private CacheHelper(Context context) {
        mCacheDir = context.getCacheDir();
    }

    public static CacheHelper getInstance() {
        if(Instance == null) {
            Instance = new CacheHelper(ContextHelper.getContext());
        }
        return Instance;
    }

    public void cacheInDisc(@NonNull String key, Object src) {
        try {
            File cacheFile = new File(mCacheDir, key);
            if(src == null && cacheFile.exists()) {
                cacheFile.delete();
                return;
            }
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(cacheFile));
            Gson gson = new Gson();
            out.writeObject(gson.toJson(src));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> T readFromDisc(@NonNull String key, Type typeOfT) {
        try {
            File cacheFile = new File(mCacheDir, key);
            if(cacheFile.exists()) {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(cacheFile));
                String json = in.readObject().toString();
                Gson gson = new Gson();
                return gson.fromJson(json, typeOfT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T readFromDisc(@NonNull String key, Class<T> classOfT) {
        try {
            File cacheFile = new File(mCacheDir, key);
            if(cacheFile.exists()) {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(cacheFile));
                String json = in.readObject().toString();
                Gson gson = new Gson();
                return gson.fromJson(json, classOfT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
