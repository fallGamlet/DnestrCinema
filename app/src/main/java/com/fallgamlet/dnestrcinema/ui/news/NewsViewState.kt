package com.fallgamlet.dnestrcinema.ui.news

import androidx.lifecycle.MutableLiveData
import com.fallgamlet.dnestrcinema.domain.models.NewsItem
import com.fallgamlet.dnestrcinema.ui.base.BaseViewState

class NewsViewState(
    val items: MutableLiveData<List<NewsItem>> = MutableLiveData()
): BaseViewState()
