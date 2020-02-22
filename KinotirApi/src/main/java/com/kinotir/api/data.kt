package com.kinotir.api

import java.util.*

data class FilmJson(
    var title: String? = null,
    var link: String? = null,
    var buyTicketLink: String? = null,
    var posterUrl: String? = null,
    var pubDate: Date? = null,
    var duration: String? = null,
    var genre: String? = null,
    var ageLimit: String? = null,
    var sessions: List<FilmSessionJson>? = null,
    var trailerUrls: List<String>? = null
)

data class FilmSessionJson(
    var room: String? = null,
    var value: String? = null
)

data class FilmDetailsJson(
    var posterUrl: String? = null,
    var buyTicketLink: String? = null,
    var description: String? = null,
    var pubDate: Date? = null,
    var duration: String? = null,
    var country: String? = null,
    var director: String? = null,
    var scenario: String? = null,
    var actors: String? = null,
    var genre: String? = null,
    var budget: String? = null,
    var ageLimit: String? = null,
    var imageUrls: List<ImageUrlJson>? = null,
    var trailerUrls: List<String>? = null
)

data class ImageUrlJson(
    var big: String? = null,
    var small: String? = null
)

data class NewsJson(
    var tag: String? = null,
    var title: String? = null,
    var body: String? = null,
    var date: Date? = null,
    var imageUrls: List<String>? = null
)

data class TicketJson(
    var id: String? = null,
    var title: String? = null,
    var url: String? = null,
    var room: String? = null,
    var status: String? = null,
    var date: String? = null,
    var time: String? = null,
    var ticketPlaces: List<TicketPlaceJson>? = null
)

data class TicketPlaceJson(
    var row: String? = null,
    var place: String? = null,
    var url: String? = null
)

data class ApiException(
    val code: Int = 0,
    val tag: String = "",
    override val message: String? = ""
) : Throwable(message)
