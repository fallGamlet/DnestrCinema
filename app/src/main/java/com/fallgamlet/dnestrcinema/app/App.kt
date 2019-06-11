package com.fallgamlet.dnestrcinema.app

import androidx.multidex.MultiDexApplication
import android.util.Log
import com.fallgamlet.dnestrcinema.data.KinoTirCinemaRepository
import com.fallgamlet.dnestrcinema.factory.KinotirConfigFactory

import com.fallgamlet.dnestrcinema.utils.LogUtils
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.leakcanary.LeakCanary

import io.reactivex.plugins.RxJavaPlugins

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        initRx()
        initLeakCanary()

        AppFacade.instance.init(KinotirConfigFactory())
        AppFacade.instance.init(KinoTirCinemaRepository())
    }

    private fun initRx() {
        RxJavaPlugins.setErrorHandler { throwable ->
            Log.d("App", "RxJavaPlugins error handle", throwable)
        }
    }

    private fun initLeakCanary() {
        if (!LogUtils.isDebug) {
            return
        }

        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this)
        }
    }

}
