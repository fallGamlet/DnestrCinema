package com.fallgamlet.dnestrcinema.domain

import com.fallgamlet.dnestrcinema.domain.models.*
import io.reactivex.Observable


class KinoTirCinemaInteractor(private val cinemaRepository: CinemaRepository)
    : CinemaInteractor {

    override fun todayMovies(): Observable<List<MovieItem>> {
        return cinemaRepository.todayMovies()
            .toObservable()
    }

    override fun soonMovies(): Observable<List<MovieItem>> {
        return cinemaRepository.soonMovies()
            .toObservable()
    }

    override fun movieDetails(path: String): Observable<MovieItem> {
        return cinemaRepository.movieDetails(path)
            .toObservable()
    }

    override fun tickets(): Observable<List<TicketItem>> {
        return cinemaRepository.tickets()
            .toObservable()
    }

    override fun newses(): Observable<List<NewsItem>> {
        return cinemaRepository.newses()
            .toObservable()
    }

    override fun cinema(): Observable<CinemaItem> {
        return cinemaRepository.cinema()
            .toObservable()
    }
}