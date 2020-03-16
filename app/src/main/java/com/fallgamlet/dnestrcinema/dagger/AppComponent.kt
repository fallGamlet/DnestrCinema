package com.fallgamlet.dnestrcinema.dagger

import com.fallgamlet.dnestrcinema.app.App
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ActivityModule::class
])
interface AppComponent : AndroidInjector<App> {

    @Component.Factory
    interface Factory: AndroidInjector.Factory<App>

}
