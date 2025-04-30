package com.fallgamlet.dnestrcinema.ui.news

import androidx.compose.runtime.Immutable

@Immutable
data class NewsVo(
    val id: String,
    val tag: String,
    val title: String,
    val date: String,
    val body: String,
    val imgUrls: List<String>,
)
