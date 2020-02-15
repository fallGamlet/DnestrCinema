package com.fallgamlet.dnestrcinema.domain.repositories.local

import com.fallgamlet.dnestrcinema.domain.models.Cinema
import io.reactivex.Single

interface LocalCinemaRepository {
    fun getItems(): Single<List<Cinema>>
    fun getItem(selector: String): Single<Cinema>
}
