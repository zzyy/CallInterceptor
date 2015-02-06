package com.zy.callinterceptor.util;

import android.util.Log;

import com.zy.callinterceptor.pojo.Constants;

/**
 * Created by Simon on 2015/1/23.
 */
public class LogUtil {
    private final static boolean DEBUG = Constants.IS_ALLOW_LOG;
    private final static String TAG = Constants.LOG_TAG;

    public static void d(String msg){
        d(TAG, msg);
    }

    public static void d(String TAG, String msg){
        if (DEBUG){
            Log.d(TAG, msg);
        }
    }

    public static void i(String msg){
        i(TAG, msg);
    }

    public static void i(String TAG, String msg){
        if (DEBUG){
            Log.i(TAG, msg);
        }
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void e(String TAG, String msg) {
        if (DEBUG){
            Log.e(TAG, msg);
        }
    }
}
