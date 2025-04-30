package com.fallgamlet.dnestrcinema.ui.movie.model

import com.fallgamlet.dnestrcinema.domain.models.Film
import com.fallgamlet.dnestrcinema.utils.DateTimeUtils

object MovieVoMapper {
    fun map(film: Film): MovieVo {
        return MovieVo(
            title = film.title,
            link = film.link,
            duration = film.duration,
            posterUtl = film.posterUrl,
            pubDate = DateTimeUtils.getDateNamed(film.pubDate) ?: "",
            schedules = film.sessions.map {
                ScheduleItemVo(
                    room = it.room,
                    time = it.times,
                )
            }
        )
    }
}
