package com.fallgamlet.dnestrcinema.dagger

import com.fallgamlet.dnestrcinema.data.repository.remote.kinotir.FilmRepositoryImpl
import com.fallgamlet.dnestrcinema.data.repository.remote.kinotir.NewsRepositoryImpl
import com.fallgamlet.dnestrcinema.domain.repositories.remote.FilmRepository
import com.fallgamlet.dnestrcinema.domain.repositories.remote.NewsRepository
import com.kinotir.api.KinotirApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideFilmRepository(api: KinotirApi): FilmRepository {
        return FilmRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideNewsRepository(api: KinotirApi): NewsRepository {
        return NewsRepositoryImpl(api)
    }

}
