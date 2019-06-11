package com.fallgamlet.dnestrcinema.utils

import android.util.Log

import com.fallgamlet.dnestrcinema.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder


object LogUtils {
    val isDebug = BuildConfig.DEBUG

    private val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.s'Z'")
            .create()

    fun log(tag: String, obj: Any?) {
        if(!isDebug) return

        log(tag, gson.toJson(obj))
    }

    fun log(msg: String) {
        log("", msg)
    }

    fun log(tag: String, msg: String?) {
        if(!isDebug) return

        Log.d(tag, msg?: "(null)")
    }

    fun log(tag: String, msg: String?, throwable: Throwable) {
        if(!isDebug) return

        Log.d(tag, msg?: "(null)", throwable)
    }
}
