package com.fallgamlet.dnestrcinema.domain.repositories.remote

import com.fallgamlet.dnestrcinema.domain.models.Film
import io.reactivex.Single

interface FilmRepository {
    fun getToday(): Single<List<Film>>
    fun getSoon(): Single<List<Film>>
    fun getDetails(selector: String): Single<Film>
}
