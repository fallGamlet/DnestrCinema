package com.fallgamlet.dnestrcinema.ui.movie

import com.fallgamlet.dnestrcinema.domain.models.Film
import com.fallgamlet.dnestrcinema.domain.models.MovieItem
import com.fallgamlet.dnestrcinema.domain.models.ScheduleItem

object MovieItemMapper {
    fun map(film: Film): MovieItem {
        return MovieItem().apply {
            this.title = film.title
            this.link = film.link
            this.pubDate = film.pubDate
            this.duration = film.duration
            this.setSchedules(
                film.sessions.map {
                    ScheduleItem().apply {
                        room = it.room
                        value = it.times
                    }
                }
            )
        }
    }
}
