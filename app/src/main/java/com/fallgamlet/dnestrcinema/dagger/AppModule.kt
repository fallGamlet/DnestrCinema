package com.fallgamlet.dnestrcinema.dagger

import android.content.Context
import com.fallgamlet.dnestrcinema.app.App
import dagger.Binds
import dagger.Module

@Module(includes = [
    NetworkModule::class,
    RepositoryModule::class,
    ViewModelModule::class,
])
interface AppModule {

    @Binds
    fun bindContext(app: App): Context

}
