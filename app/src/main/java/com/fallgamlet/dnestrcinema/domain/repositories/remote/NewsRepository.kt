package com.fallgamlet.dnestrcinema.domain.repositories.remote

import com.fallgamlet.dnestrcinema.domain.models.NewsPost

interface NewsRepository {
    suspend fun getItemsSuspend(): List<NewsPost>
}
