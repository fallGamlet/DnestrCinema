package com.fallgamlet.dnestrcinema.data.repository.remote.kinotir

import com.fallgamlet.dnestrcinema.domain.models.NewsPost
import com.fallgamlet.dnestrcinema.domain.repositories.remote.NewsRepository
import com.kinotir.api.KinotirApi
import com.kinotir.api.NewsJson
import java.util.Date

internal class NewsRepositoryImpl(
    private val api: KinotirApi,
) : NewsRepository {

    override suspend fun getItemsSuspend(): List<NewsPost> {
        val newsJson = api.newses()
        return mapNewsList(newsJson) ?: return emptyList()
    }

    private fun mapNewsList(jsonList: List<NewsJson>?) = jsonList
        ?.map(::mapNews)
        ?.filterNot(NewsPost::isEmpty)

    private fun mapNews(json: NewsJson) = NewsPost(
        id = json.title ?: "",
        tag = json.tag ?: "",
        title = json.title ?: "",
        date = json.date ?: Date(0),
        text = json.body ?: "",
        imageUrls = json.imageUrls ?: emptyList()
    )

}
