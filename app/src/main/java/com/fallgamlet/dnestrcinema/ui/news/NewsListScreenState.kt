package com.fallgamlet.dnestrcinema.ui.news

import androidx.compose.runtime.Immutable

@Immutable
data class NewsListScreenState(
    val items: List<NewsVo> = emptyList(),
    val isRefreshing: Boolean = false,
)
