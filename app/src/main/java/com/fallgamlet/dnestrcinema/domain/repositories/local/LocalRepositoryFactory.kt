package com.fallgamlet.dnestrcinema.domain.repositories.local

interface LocalRepositoryFactory {
    fun cinemaREpository(): LocalCinemaRepository
    fun filmRepository(): LocalFilmRepository
    fun newsRepository(): LocalNewsRepository
}
