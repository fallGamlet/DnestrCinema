package com.fallgamlet.dnestrcinema.ui.news

data class NewsVo(
    val id: String,
    val tag: String,
    val title: String,
    val date: String,
    val body: String,
    val imgUrls: List<String>,
)
