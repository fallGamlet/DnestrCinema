package com.fallgamlet.dnestrcinema.ui.news

import com.fallgamlet.dnestrcinema.domain.models.NewsItem
import com.fallgamlet.dnestrcinema.utils.DateTimeUtils

object NewsVoMapper {
    fun mapNews(news: NewsItem): NewsVo {
        return NewsVo(
            id = news.id.toString(),
            tag = news.tag ?: "",
            title = news.title ?: "",
            body = news.body ?: "",
            imgUrls = news.imgUrls.toList(),
            date = DateTimeUtils.getDateDotWithoutTime(news.date) ?: "",
        )
    }
}
