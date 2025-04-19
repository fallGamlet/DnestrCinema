package com.fallgamlet.dnestrcinema.ui.news

data class NewsListScreenState(
    val items: List<NewsVo> = emptyList(),
    val isRefreshing: Boolean = false,
)
