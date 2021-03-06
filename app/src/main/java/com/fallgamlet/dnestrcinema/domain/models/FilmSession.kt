package com.fallgamlet.dnestrcinema.domain.models

data class FilmSession(
    val room: String = "",
    val times: String = ""
) {
    fun isEmpty() = this == EMPTY

    companion object {
        val EMPTY = FilmSession()
    }
}
