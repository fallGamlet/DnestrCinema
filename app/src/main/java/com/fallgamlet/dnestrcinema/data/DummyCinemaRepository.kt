package com.fallgamlet.dnestrcinema.data

import com.fallgamlet.dnestrcinema.domain.CinemaRepository
import com.fallgamlet.dnestrcinema.domain.models.*
import io.reactivex.Single

class DummyCinemaRepository: CinemaRepository {

    override fun todayMovies(): Single<List<MovieItem>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun soonMovies(): Single<List<MovieItem>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun movieDetails(path: String): Single<MovieDetailItem> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun tickets(): Single<List<TicketItem>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun newses(): Single<List<NewsItem>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cinema(): Single<CinemaItem> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}