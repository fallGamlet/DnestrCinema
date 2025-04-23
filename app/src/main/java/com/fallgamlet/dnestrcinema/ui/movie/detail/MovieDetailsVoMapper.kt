package com.fallgamlet.dnestrcinema.ui.movie.detail

import com.fallgamlet.dnestrcinema.domain.models.Film
import com.fallgamlet.dnestrcinema.utils.DateTimeUtils

object MovieDetailsVoMapper {
    fun map(film: Film): MovieDetailsVo {
        return MovieDetailsVo(
            title = film.title,
            pubDate = DateTimeUtils.getDateNamed(film.pubDate) ?: "",
            schedule = film.sessions.map { session ->
                "${session.room}: ${session.times}"
            },
            duration = film.duration,
            director = film.director,
            actors = film.actors,
            scenario = film.scenario,
            ageLimit = film.ageLimit,
            budget = film.budget,
            country = film.country,
            genre = film.genre,
            description = film.description,
            posterUrl = film.posterUrl,
            imageUrls = film.imageUrls.map { url -> url.low },
            trailerMovieUrls = film.trailerUrls,
        )
    }
}
