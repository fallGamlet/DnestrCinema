package com.fallgamlet.dnestrcinema.ui.movie.detail

data class MovieDetailsVo(
    val title: String = "",
    val pubDate: String = "",
    val schedule: List<String> = emptyList(),
    val duration: String = "",
    val director: String = "",
    val actors: String = "",
    val scenario: String = "",
    val ageLimit: String = "",
    val budget: String = "",
    val country: String = "",
    val genre: String = "",
    val description: String = "",
    val posterUrl: String = "",
    val imageUrls: List<String> = emptyList(),
    val trailerMovieUrls: List<String> = emptyList(),
)
