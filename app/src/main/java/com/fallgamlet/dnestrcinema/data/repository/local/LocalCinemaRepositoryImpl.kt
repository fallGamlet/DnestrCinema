package com.fallgamlet.dnestrcinema.data.repository.local

import com.fallgamlet.dnestrcinema.domain.models.Cinema
import com.fallgamlet.dnestrcinema.domain.repositories.local.LocalCinemaRepository
import io.reactivex.Single

class LocalCinemaRepositoryImpl: LocalCinemaRepository {

    override fun getItems(): Single<List<Cinema>> {
        return Single.just(cinemaList)
    }

    override fun getItem(selector: String): Single<Cinema> {
        return Single.fromCallable {
            cinemaList
                .firstOrNull { it.id == selector }
                ?: Cinema.EMPTY
        }
    }

    companion object {
        private val cinemaList = listOf(
            Cinema("1", "Кинотеатр Тирасполь")
        )
    }

}
