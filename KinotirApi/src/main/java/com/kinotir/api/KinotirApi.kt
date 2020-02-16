package com.kinotir.api

import io.reactivex.Single

interface KinotirApi {

    fun todayFilms(): Single<List<FilmJson>>
    fun soonFilms(): Single<List<FilmJson>>
    fun filmDetails(path: String): Single<FilmDetailsJson>
    fun newses(): Single<List<NewsJson>>
    fun tickets(): Single<List<TicketJson>>

}
