package com.fallgamlet.dnestrcinema.data.repository.remote.kinotir

import com.fallgamlet.dnestrcinema.domain.models.Film
import com.fallgamlet.dnestrcinema.domain.models.FilmSession
import com.fallgamlet.dnestrcinema.domain.models.ImageUrl
import com.fallgamlet.dnestrcinema.domain.repositories.remote.FilmRepository
import com.kinotir.api.*
import io.reactivex.Single
import java.util.*

internal class FilmRepositoryImpl(
    private val api: KinotirApi
) : FilmRepository {

    override fun getToday(): Single<List<Film>> {
        return api.todayFilms()
            .map(::mapFilms)
    }

    override fun getSoon(): Single<List<Film>> {
        return api.soonFilms()
            .map(::mapFilms)
    }

    override fun getDetails(selector: String): Single<Film> {
        return api.filmDetails(selector)
            .map(::mapFilmDetails)
    }

    private fun mapFilms(jsonList: List<FilmJson>) = jsonList.map(::mapFilm)

    private fun mapFilm(json: FilmJson): Film {
        return Film(
            id = json.link ?: "",
            title = json.title ?: "",
            link = json.link ?: "",
            duration = json.duration ?: "",
            posterUrl = json.posterUrl ?: "",
            trailerUrls = json.trailerUrls ?: emptyList(),
            sessions = json.sessions.mapFilmSessions(),
            pubDate = json.pubDate ?: Date(0),
            genre = json.genre ?: "",
            ageLimit = json.ageLimit ?: ""
        )
    }

    private fun List<FilmSessionJson>?.mapFilmSessions() = this?.map(::mapFilmSession)
        ?.filterNot(FilmSession::isEmpty)
        ?: emptyList()

    private fun mapFilmSession(json: FilmSessionJson): FilmSession {
        return FilmSession(
            room = json.room ?: "",
            times = json.value ?: ""
        )
    }

    private fun mapFilmDetails(json: FilmDetailsJson): Film {
        return Film(
            title = json.title ?: "",
            duration = json.duration ?: "",
            buyTicketLink = json.buyTicketLink ?: "",
            posterUrl = json.posterUrl ?: "",
            trailerUrls = json.trailerUrls ?: emptyList(),
            imageUrls = json.imageUrls.mapImageUrls(),
            pubDate = json.pubDate ?: Date(0),
            description = json.description ?: "",
            genre = json.genre ?: "",
            ageLimit = json.ageLimit ?: "",
            country = json.country ?: "",
            director = json.director ?: "",
            scenario = json.scenario ?: "",
            actors = json.actors ?: "",
            budget = json.budget ?: ""
        )
    }

    private fun List<ImageUrlJson>?.mapImageUrls() = this?.map(::mapImageUrl)
        ?.filterNot(ImageUrl::isEmpty)
        ?: emptyList()

    private fun mapImageUrl(json: ImageUrlJson) = ImageUrl(
        hight = json.big ?: "",
        low = json.small ?: ""
    )

}
