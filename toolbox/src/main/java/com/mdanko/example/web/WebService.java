package com.mdanko.example.web;

import android.content.Context;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

// static singleton using VOLLEY
// but could an interface for other implementations too (ie. retro or jsquire)

public class WebService {
    public static RequestQueue volleyQ; // global access to volley HTTP request queue
    public static ImageLoader imageLoader; // global access to volley image loader
    public static BitmapCache bitmapCache; // global access to bitmapCache

    // REQUIRED to be called with application context BEFORE any usage
    // recommended early in Application.onCreate() before Web access is needed
    public static void init(Context context) {
        Cache httpCache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024 * 10);
        Network network = new BasicNetwork(new HurlStack());
        volleyQ = new RequestQueue(httpCache, network);
        bitmapCache = new BitmapCache(context); // in-memory
        imageLoader = new ImageLoader(volleyQ, bitmapCache);
        volleyQ.start();
    }

    // asynchronously execute HTTP request and run callback on main thread with response pogo
    public static void call(WebRequest<?> webRequest) {
        Log.i("volley", ">> " + webRequest.url + " => " + webRequest.responseType);
        volleyQ.add(new VolleyGsonRequest(webRequest));
    }

    // asynchronously execute oAuth HTTP request and run callback on main thread with response pogo
    public static void callOAuth(WebRequest<?> webRequest) {
        Log.i("volley", ">> " + webRequest.url + " => " + webRequest.responseType);
        volleyQ.add(new VolleyOAuthRequest(webRequest));
    }

    public static void cancel(Object requestKey) {
        volleyQ.cancelAll(requestKey);
    }

    // on low memory shutdown everything immediately
    public static void lowMemory() {
        bitmapCache.clear();
    }

    // TODO can stop/start volleyQ for even more memory but requires some tracking
}