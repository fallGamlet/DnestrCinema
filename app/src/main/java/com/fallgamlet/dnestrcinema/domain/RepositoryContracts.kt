package com.fallgamlet.dnestrcinema.domain

import com.fallgamlet.dnestrcinema.domain.models.*
import io.reactivex.Single

interface CinemaRepository {

    fun todayMovies(): Single<List<MovieItem>>

    fun soonMovies(): Single<List<MovieItem>>

    fun movieDetails(path: String): Single<MovieItem>

    fun tickets(): Single<List<TicketItem>>

    fun newses(): Single<List<NewsItem>>

    fun cinema(): Single<CinemaItem>

}