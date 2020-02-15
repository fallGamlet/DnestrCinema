package com.fallgamlet.dnestrcinema.domain.models

import java.util.*

data class NewsPost(
    val id: String = "",
    val tag: String = "",
    val title: String = "",
    val date: Date = Date(0),
    val text: String = "",
    val imageUrls: List<String> = emptyList()
)
