package com.fallgamlet.dnestrcinema.domain

import com.fallgamlet.dnestrcinema.domain.models.CinemaItem
import com.fallgamlet.dnestrcinema.domain.models.MovieItem
import com.fallgamlet.dnestrcinema.domain.models.NewsItem
import com.fallgamlet.dnestrcinema.domain.models.TicketItem
import io.reactivex.Observable

interface CinemaInteractor {

    fun todayMovies(): Observable<List<MovieItem>>

    fun soonMovies(): Observable<List<MovieItem>>

    fun movieDetails(path: String): Observable<MovieItem>

    fun tickets(): Observable<List<TicketItem>>

    fun newses(): Observable<List<NewsItem>>

    fun cinema(): Observable<CinemaItem>

}
