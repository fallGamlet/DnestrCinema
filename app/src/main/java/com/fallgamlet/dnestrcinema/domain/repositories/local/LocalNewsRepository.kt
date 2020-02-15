package com.fallgamlet.dnestrcinema.domain.repositories.local

import com.fallgamlet.dnestrcinema.domain.models.NewsPost
import io.reactivex.Single

interface LocalNewsRepository {
    fun getItems(): Single<List<NewsPost>>
    fun getItem(selector: String): Single<NewsPost>
    fun setItems(items: List<NewsPost>): Single<Boolean>
    fun setItem(item: NewsPost): Single<Boolean>
}
