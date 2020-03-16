package com.fallgamlet.dnestrcinema.app

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.fallgamlet.dnestrcinema.dagger.DaggerAppComponent
import com.fallgamlet.dnestrcinema.data.KinoTirCinemaRepository
import com.fallgamlet.dnestrcinema.factory.KinotirConfigFactory
import com.fallgamlet.dnestrcinema.utils.LogUtils
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.reactivex.plugins.RxJavaPlugins
import javax.inject.Inject


class App : MultiDexApplication(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()
        initDagger()
        AndroidThreeTen.init(this)
        initRx()
        initLeakCanary()

        AppFacade.instance.init(KinotirConfigFactory())
        AppFacade.instance.init(KinoTirCinemaRepository())
    }

    private fun initDagger() {
        DaggerAppComponent.factory()
            .create(this)
            .inject(this)
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

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}
