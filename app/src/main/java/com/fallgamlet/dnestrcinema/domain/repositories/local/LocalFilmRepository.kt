package com.fallgamlet.dnestrcinema.domain.repositories.local

import com.fallgamlet.dnestrcinema.domain.models.Film
import io.reactivex.Single

interface LocalFilmRepository {
    fun getItems(): Single<List<Film>>
    fun getItem(selector: String): Single<Film>
    fun setItems(items: List<Film>): Single<Boolean>
    fun setItem(item: Film): Single<Boolean>
}
