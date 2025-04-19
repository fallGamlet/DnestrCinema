package com.fallgamlet.dnestrcinema.ui.movie.model


data class MovieVo(
    val title: String,
    val link: String,
    val duration: String,
    val posterUtl: String,
    val schedules: List<ScheduleItemVo>,
    val pubDate: String,
)

data class ScheduleItemVo(
    val room: String,
    val time: String,
)
