package com.fallgamlet.dnestrcinema.ui.movie.model

import androidx.compose.runtime.Immutable

@Immutable
data class MovieVo(
    val title: String,
    val link: String,
    val duration: String,
    val posterUtl: String,
    val schedules: List<ScheduleItemVo>,
    val pubDate: String,
)

@Immutable
data class ScheduleItemVo(
    val room: String,
    val time: String,
)
