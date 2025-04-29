package com.fallgamlet.dnestrcinema.domain.models

import java.util.Date

data class NewsPost(
    val id: String = "",
    val tag: String = "",
    val title: String = "",
    val date: Date = Date(0),
    val text: String = "",
    val imageUrls: List<String> = emptyList()
) {

    fun isEmpty() = this == EMPTY

    companion object {
        val EMPTY = NewsPost()
    }
}
