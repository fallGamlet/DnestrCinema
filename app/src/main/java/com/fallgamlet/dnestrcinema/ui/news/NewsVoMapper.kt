package com.fallgamlet.dnestrcinema.ui.news

import com.fallgamlet.dnestrcinema.domain.models.NewsPost
import com.fallgamlet.dnestrcinema.utils.DateTimeUtils

object NewsVoMapper {
    fun mapNews(news: NewsPost): NewsVo {
        return NewsVo(
            id = news.id,
            tag = news.tag,
            title = news.title,
            body = news.text,
            imgUrls = news.imageUrls,
            date = DateTimeUtils.getDateDotWithoutTime(news.date) ?: "",
        )
    }
}
