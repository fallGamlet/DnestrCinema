package com.kinotir.api

import com.kinotir.api.mappers.KinotirMapper
import io.reactivex.Single
import retrofit2.Response

internal class KinotirApiImpl(
    private val serverApi: ServerApi
) : KinotirApi{

    override fun todayFilms(): Single<List<FilmJson>> {
        return serverApi.todayFilms()
            .checkResponse()
            .map { KinotirMapper.mapFilms(it) ?: emptyList() }
    }

    override fun soonFilms(): Single<List<FilmJson>> {
        return serverApi.soonFilms()
            .checkResponse()
            .map { KinotirMapper.mapFilms(it) ?: emptyList() }
    }

    override fun filmDetails(path: String): Single<FilmDetailsJson> {
        return serverApi.filmDetails(path)
            .checkResponse()
            .map { KinotirMapper.mapFilmDetails(it) ?: FilmDetailsJson() }
    }

    override fun newses(): Single<List<NewsJson>> {
        return serverApi.newses()
            .checkResponse()
            .map { KinotirMapper.mapNewses(it) ?: emptyList() }
    }

    override fun tickets(): Single<List<TicketJson>> {
        return serverApi.tickets()
            .checkResponse()
            .map { KinotirMapper.mapTickets(it) ?: emptyList() }
    }

    private fun Single<Response<String>>.checkResponse(): Single<String>
            = map { it.checkResponse() }

    private fun Response<String>.checkResponse(): String {
        if (this.code() >= 400) {
            throw ApiException(this.code(), this.message())
        }
        return this.body() ?: ""
    }
}
