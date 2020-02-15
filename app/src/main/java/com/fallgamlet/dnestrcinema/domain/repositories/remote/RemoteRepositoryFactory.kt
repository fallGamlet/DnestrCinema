package com.fallgamlet.dnestrcinema.domain.repositories.remote

interface RemoteRepositoryFactory {
    fun filmRepository(): FilmRepository
    fun newsRepository(): NewsRepository
}
