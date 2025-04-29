package com.fallgamlet.dnestrcinema.domain.models

import java.util.Date


data class Film(
    val id: String = "",
    val title: String = "",
    val link: String = "",
    val buyTicketLink: String = "",
    val duration: String = "",
    val posterUrl: String = "",
    val trailerUrls: List<String> = emptyList(),
    val imageUrls: List<ImageUrl> = emptyList(),
    val sessions: List<FilmSession> = emptyList(),
    val pubDate: Date = Date(0),
    val description: String = "",
    val genre: String = "",
    val ageLimit: String = "",
    val country: String = "",
    val director: String = "",
    val scenario: String = "",
    val actors: String = "",
    val budget: String = ""
) {

    fun isEmpty() = this == EMPTY

    companion object {
        val EMPTY = Film()
    }
}
