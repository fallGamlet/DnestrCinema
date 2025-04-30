package com.fallgamlet.dnestrcinema.ui.movie.detail

import androidx.compose.runtime.Immutable

@Immutable
data class MovieDetailsLabels(
    val duration: String,
    val director: String,
    val actors: String,
    val scenario: String,
    val ageLimit: String,
    val budget: String,
    val country: String,
    val genre: String,
    val trailer: String,
)
