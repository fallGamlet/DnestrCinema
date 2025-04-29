package com.fallgamlet.dnestrcinema.app

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.fallgamlet.dnestrcinema.dagger.AppComponent
import com.fallgamlet.dnestrcinema.dagger.AppComponentProvider
import com.fallgamlet.dnestrcinema.dagger.DaggerAppComponent
import com.fallgamlet.dnestrcinema.factory.KinotirConfigFactory
import com.jakewharton.threetenabp.AndroidThreeTen


class App : MultiDexApplication(),
    AppComponentProvider
{

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        initDagger()
        AndroidThreeTen.init(this)

        AppFacade.instance.init(KinotirConfigFactory())
    }

    private fun initDagger() {
        appComponent = DaggerAppComponent.builder()
            .setApp(this)
            .build()
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
