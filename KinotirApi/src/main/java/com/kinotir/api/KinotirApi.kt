package com.kinotir.api


interface KinotirApi {

    suspend fun todayFilms(): List<FilmJson>
    suspend fun soonFilms(): List<FilmJson>
    suspend fun filmDetails(path: String): FilmDetailsJson
    suspend fun newses(): List<NewsJson>
    suspend fun tickets(): List<TicketJson>

}
