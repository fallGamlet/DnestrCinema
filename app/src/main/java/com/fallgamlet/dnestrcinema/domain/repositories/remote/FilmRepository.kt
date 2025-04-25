package com.fallgamlet.dnestrcinema.domain.repositories.remote

import com.fallgamlet.dnestrcinema.domain.models.Film

interface FilmRepository {
    suspend fun getToday(): List<Film>
    suspend fun getSoon(): List<Film>
    suspend fun getDetails(selector: String): Film
}
