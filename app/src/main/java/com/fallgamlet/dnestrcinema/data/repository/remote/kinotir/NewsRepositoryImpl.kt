package com.fallgamlet.dnestrcinema.data.repository.remote.kinotir

import com.fallgamlet.dnestrcinema.domain.models.NewsPost
import com.fallgamlet.dnestrcinema.domain.repositories.remote.NewsRepository
import io.reactivex.Single

internal class NewsRepositoryImpl: NewsRepository {

    override fun getItems(): Single<List<NewsPost>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
