package com.fallgamlet.dnestrcinema.data.repository.remote.kinotir

import com.fallgamlet.dnestrcinema.domain.models.Film
import com.fallgamlet.dnestrcinema.domain.repositories.remote.FilmRepository
import io.reactivex.Single

internal class FilmRepositoryImpl: FilmRepository {

    override fun getToday(): Single<List<Film>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSoon(): Single<List<Film>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDetails(selector: String): Single<Film> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
