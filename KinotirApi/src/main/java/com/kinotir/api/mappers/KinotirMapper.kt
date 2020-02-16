package com.kinotir.api.mappers

import com.kinotir.api.FilmDetailsJson
import com.kinotir.api.FilmJson
import com.kinotir.api.NewsJson
import com.kinotir.api.TicketJson

object KinotirMapper {

    fun mapFilmDetails(html: String): FilmDetailsJson? = HtmlFilmDetailMapper().map(html)

    fun mapFilms(html: String): List<FilmJson>? = HtmlFilmsMapper().map(html)

    fun mapNewses(html: String): List<NewsJson>? = HtmlNewsMapper().map(html)

    fun mapTickets(html: String): List<TicketJson>? = HtmlTicketsMapper().map(html)

    fun mapLoginResult(json: String): Boolean = JsonLoginMapper().map(json)
}
