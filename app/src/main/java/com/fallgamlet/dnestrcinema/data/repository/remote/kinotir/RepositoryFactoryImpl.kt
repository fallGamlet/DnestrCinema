package com.fallgamlet.dnestrcinema.data.repository.remote.kinotir

import com.fallgamlet.dnestrcinema.domain.repositories.remote.FilmRepository
import com.fallgamlet.dnestrcinema.domain.repositories.remote.NewsRepository
import com.fallgamlet.dnestrcinema.domain.repositories.remote.RemoteRepositoryFactory
import com.kinotir.api.KinotirApi

class RepositoryFactoryImpl(
  api: KinotirApi
) : RemoteRepositoryFactory {

    private val filmRepository: FilmRepository = FilmRepositoryImpl(api)
    private val newsRepository: NewsRepository = NewsRepositoryImpl(api)

    override fun filmRepository(): FilmRepository = filmRepository
    override fun newsRepository(): NewsRepository = newsRepository
}
