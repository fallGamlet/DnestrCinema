package com.fallgamlet.dnestrcinema.domain

import com.fallgamlet.dnestrcinema.domain.models.CinemaItem
import com.fallgamlet.dnestrcinema.domain.models.MovieItem
import com.fallgamlet.dnestrcinema.domain.models.NewsItem
import com.fallgamlet.dnestrcinema.domain.models.TicketItem
import io.reactivex.Single

interface CinemaRepository {

    fun todayMovies(): Single<List<MovieItem>>

    fun soonMovies(): Single<List<MovieItem>>

    fun movieDetails(path: String): Single<MovieItem>

    fun tickets(): Single<List<TicketItem>>

    fun newses(): Single<List<NewsItem>>

    fun cinema(): Single<CinemaItem>

}
