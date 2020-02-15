package com.fallgamlet.dnestrcinema.domain.repositories.remote

import com.fallgamlet.dnestrcinema.domain.models.NewsPost
import io.reactivex.Single

interface NewsRepository {
    fun getItems(): Single<List<NewsPost>>
}
