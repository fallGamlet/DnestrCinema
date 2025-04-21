package com.fallgamlet.dnestrcinema.dagger

import com.kinotir.api.ApiFactory
import com.kinotir.api.KinotirApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideApiFactory(): ApiFactory {
        return ApiFactory(
            appName = "DnestrCinemaViewer",
            debug = false,
        )
    }

    @Provides
    fun provideKinotirApi(factory: ApiFactory): KinotirApi {
        return factory.api
    }
}
