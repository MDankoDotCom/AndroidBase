package com.mdanko.example.web;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

// implementation from volley group at google
class BitmapCache implements ImageLoader.ImageCache {
    private LruCache<String, Bitmap> mMemoryCache;

    public BitmapCache(Context context) {
        int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        // Use 1/16th of the available memory for this memory cache.
        int cacheSize = 1024 * 1024 * memClass / 16;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mMemoryCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mMemoryCache.put(url, bitmap);
    }

    public void remove(String url) {
        mMemoryCache.remove(url);
    }

    public void clear() {
        mMemoryCache.evictAll();
    }
}
