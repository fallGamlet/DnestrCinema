package com.fallgamlet.dnestrcinema.ui.movie.model

import com.fallgamlet.dnestrcinema.app.AppFacade
import com.fallgamlet.dnestrcinema.domain.models.MovieItem
import com.fallgamlet.dnestrcinema.utils.DateTimeUtils
import com.fallgamlet.dnestrcinema.utils.HttpUtils

object MovieVoMapper {
    fun map(movie: MovieItem): MovieVo {
        return MovieVo(
            title = movie.title?.replace("[ ]/[ ]".toRegex(), "\n")?.trim() ?: "",
            link = movie.link ?: "",
            duration = movie.duration ?: "",
            posterUtl = HttpUtils.getAbsoluteUrl(
                baseUrl = AppFacade.instance.requestFactory?.baseUrl,
                url = movie.posterUrl ?: "",
            ) ?: "",
            pubDate = DateTimeUtils.getDateNamed(movie.pubDate) ?: "",
            schedules = movie.schedules.map {
                ScheduleItemVo(
                    room = it.room ?: "",
                    time = it.value ?: "",
                )
            }
        )
    }
}
