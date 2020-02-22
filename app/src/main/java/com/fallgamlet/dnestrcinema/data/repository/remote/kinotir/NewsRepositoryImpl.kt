package com.fallgamlet.dnestrcinema.data.repository.remote.kinotir

import com.fallgamlet.dnestrcinema.domain.models.NewsPost
import com.fallgamlet.dnestrcinema.domain.repositories.remote.NewsRepository
import com.kinotir.api.KinotirApi
import com.kinotir.api.NewsJson
import io.reactivex.Single
import java.util.*

internal class NewsRepositoryImpl(
    private val api: KinotirApi
) : NewsRepository {

    override fun getItems(): Single<List<NewsPost>> {
        return api.newses()
            .map(::mapNewsList)
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
