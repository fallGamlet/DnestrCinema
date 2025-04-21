package com.fallgamlet.dnestrcinema.app

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.fallgamlet.dnestrcinema.dagger.AppComponent
import com.fallgamlet.dnestrcinema.dagger.AppComponentProvider
import com.fallgamlet.dnestrcinema.dagger.DaggerAppComponent
import com.fallgamlet.dnestrcinema.factory.KinotirConfigFactory
import com.jakewharton.threetenabp.AndroidThreeTen
import io.reactivex.plugins.RxJavaPlugins


class App : MultiDexApplication(),
    AppComponentProvider
{

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        initDagger()
        AndroidThreeTen.init(this)
        initRx()

        AppFacade.instance.init(KinotirConfigFactory())
    }

    private fun initDagger() {
        appComponent = DaggerAppComponent.builder()
            .setApp(this)
            .build()
    }

    private fun initRx() {
        RxJavaPlugins.setErrorHandler { throwable ->
            Log.d("App", "RxJavaPlugins error handle", throwable)
        }
    }

    override fun provideComponent(): AppComponent {
        return appComponent
    }

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}
