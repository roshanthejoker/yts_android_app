package com.thejoker.yts;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by thejoker on 2/9/2016.
 */
public class VolleySingleton {
    private  static VolleySingleton sInstance = null;
    private  static  RequestQueue mRequestQueue;
    private VolleySingleton(){
        mRequestQueue =  Volley.newRequestQueue(MyApplication.GetAppContext());
    }
public static  VolleySingleton getsInstance(){
    if (sInstance == null)
        sInstance = new VolleySingleton();
    return sInstance;

}
    public static RequestQueue getmRequestQueue(){
        return mRequestQueue;
    }
}
