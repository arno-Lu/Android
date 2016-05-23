package com.ckt.io.wifidirect.utils;

import android.util.Log;

/**
 * Created by admin on 2016/3/14.
 */
public class LogUtils {
    private static final boolean DBG_I = true;
    private static final boolean DBG_D = true;

    public static void i(String tag, String msg) {
        if (DBG_I) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (DBG_D) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }
}
