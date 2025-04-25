package com.kinotir.api

import com.kinotir.api.mappers.KinotirMapper
import okhttp3.ResponseBody
import retrofit2.Response

internal class KinotirApiImpl(
    private val serverApi: ServerApi
) : KinotirApi{

    override suspend  fun todayFilms(): List<FilmJson> {
        val result = serverApi.todayFilms()
            .checkResponse()
        return KinotirMapper.mapFilms(result) ?: emptyList()
    }

    override suspend  fun soonFilms(): List<FilmJson> {
        val result = serverApi.soonFilms()
            .checkResponse()
        return KinotirMapper.mapFilms(result) ?: emptyList()
    }

    override suspend  fun filmDetails(path: String): FilmDetailsJson {
        val result = serverApi.filmDetails(path)
            .checkResponse()
        return KinotirMapper.mapFilmDetails(result) ?: FilmDetailsJson()
    }

    override suspend  fun newses(): List<NewsJson> {
        val result = serverApi.newses()
            .checkResponse()
        return KinotirMapper.mapNewses(result) ?: emptyList()
    }

    override suspend  fun tickets(): List<TicketJson> {
        val result = serverApi.tickets()
            .checkResponse()
        return KinotirMapper.mapTickets(result) ?: emptyList()
    }

    private fun Response<ResponseBody>.checkResponse(): String {
        if (code() >= 400) {
            throw ApiException(code(), message())
        }
        return body()?.string() ?: ""
    }
}
