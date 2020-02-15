package com.fallgamlet.dnestrcinema.domain.models

import java.util.*


data class Film(
    val id: String = "",
    val title: String = "",
    val link: String = "",
    val duration: String = "",
    val posterUrl: String = "",
    val trailerUrls: List<String> = emptyList(),
    val imageUrls: List<String> = emptyList(),
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
    companion object {
        val EMPTY = Film()
    }
}
