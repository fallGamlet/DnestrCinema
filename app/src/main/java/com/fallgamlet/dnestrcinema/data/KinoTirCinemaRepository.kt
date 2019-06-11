package com.fallgamlet.dnestrcinema.data

import com.fallgamlet.dnestrcinema.data.network.NetClient
import com.fallgamlet.dnestrcinema.data.network.kinotir.KinotirMapperFactory
import com.fallgamlet.dnestrcinema.data.network.kinotir.KinotirRequestFactory
import com.fallgamlet.dnestrcinema.domain.CinemaRepository
import com.fallgamlet.dnestrcinema.domain.models.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class KinoTirCinemaRepository: CinemaRepository {

    private val netClient: NetClient = NetClient(
        KinotirRequestFactory(),
        KinotirMapperFactory()
    )

    override fun todayMovies(): Single<List<MovieItem>> {
        return netClient.todayMovies
            .handleObservable()
    }

    override fun soonMovies(): Single<List<MovieItem>> {
        return netClient.soonMovies
            .handleObservable()
    }

    override fun movieDetails(path: String): Single<MovieItem> {
        return netClient.getDetailMovies(path)
            .handleObservable()
    }

    override fun tickets(): Single<List<TicketItem>> {
        return netClient.tickets
            .handleObservable()
    }

    override fun newses(): Single<List<NewsItem>> {
        return netClient.news
            .handleObservable()
    }

    override fun cinema(): Single<CinemaItem> {
        return Single.fromCallable {
            val item = CinemaItem()
            item.id = 1
            item.name = "к.Тирасполь"

            return@fromCallable item
        }.handleThreads()
    }

    private fun <T> Observable<T>.handleObservable(): Single<T> {
        return this.singleOrError()
            .handleThreads()
    }

    private fun <T> Single<T>.handleThreads(): Single<T> {
        return this.subscribeOn(Schedulers.io())
    }

}