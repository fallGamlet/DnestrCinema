package com.fallgamlet.dnestrcinema.domain.models

data class Cinema(
    val id: String = "",
    val title: String = ""
) {
    companion object {
        val EMPTY = Cinema()
    }
}
