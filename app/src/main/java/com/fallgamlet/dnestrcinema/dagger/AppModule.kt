package com.fallgamlet.dnestrcinema.dagger

import android.app.Application
import android.content.Context
import com.fallgamlet.dnestrcinema.app.App
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideContext(app: App): Context = app.applicationContext

    @Provides
    fun provideApplication(app: App): Application = app

}
