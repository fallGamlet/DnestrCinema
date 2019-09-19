package com.fallgamlet.dnestrcinema.data

import com.fallgamlet.dnestrcinema.domain.CinemaRepository
import com.fallgamlet.dnestrcinema.domain.models.CinemaItem
import com.fallgamlet.dnestrcinema.domain.models.MovieItem
import com.fallgamlet.dnestrcinema.domain.models.NewsItem
import com.fallgamlet.dnestrcinema.domain.models.TicketItem
import io.reactivex.Single

class DummyCinemaRepository: CinemaRepository {

    override fun todayMovies(): Single<List<MovieItem>> {
        return Single.error(getDummyException())
    }

    override fun soonMovies(): Single<List<MovieItem>> {
        return Single.error(getDummyException())
    }

    override fun movieDetails(path: String): Single<MovieItem> {
        return Single.error(getDummyException())
    }

    override fun tickets(): Single<List<TicketItem>> {
        return Single.error(getDummyException())
    }

    override fun newses(): Single<List<NewsItem>> {
        return Single.error(getDummyException())
    }

    override fun cinema(): Single<CinemaItem> {
        return Single.error(getDummyException())
    }


    private fun getDummyException(): RuntimeException {
        return RuntimeException("Dummy source")
    }
}
