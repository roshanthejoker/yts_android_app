package com.thejoker.yts;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by thejoker on 2/9/2016.
 */
public class VolleySingleton {
    private static VolleySingleton sInstance = null;
    private   static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;
    private VolleySingleton(){
        mRequestQueue =  Volley.newRequestQueue(MyApplication.GetAppContext());
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private LruCache<String,Bitmap>cache = new LruCache<>((int)(Runtime.getRuntime().maxMemory()/1024)/8);
            @Override
            public Bitmap getBitmap(String url) {
                return  cache.get(url);

            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url,bitmap);

            }
        });


    }
public static VolleySingleton getsInstance(){
    if (sInstance == null)
        sInstance = new VolleySingleton();
    return sInstance;

}
    public static  RequestQueue getmRequestQueue(){
        return mRequestQueue;
    }
    public  static ImageLoader getmImageLoader(){
        return mImageLoader;
    }
}
