package com.fallgamlet.dnestrcinema.utils;

import android.util.Log;

import com.fallgamlet.dnestrcinema.BuildConfig;

/**
 * Created by fallgamlet on 09.04.17.
 */

public class LogUtils {
    public static final boolean DEBUG = BuildConfig.DEBUG;

    public static void log(String msg) {
        log("", msg);
    }

    public static void log(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void log(String tag, String msg, Throwable throwable) {
        if (DEBUG) {
            Log.d(tag, msg, throwable);
        }
    }
}
