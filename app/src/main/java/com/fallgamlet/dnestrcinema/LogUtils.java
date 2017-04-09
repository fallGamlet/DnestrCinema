package com.fallgamlet.dnestrcinema;

import android.util.Log;

/**
 * Created by fallgamlet on 09.04.17.
 */

public class LogUtils {
    public static final boolean DEBUG = BuildConfig.DEBUG;

    public static void log(String tag, String msg) {
        log(tag, msg, null);
    }

    public static void log(String tag, String msg, Throwable throwable) {
        if (DEBUG) {
            Log.d(tag, msg, throwable);
        }
    }
}
