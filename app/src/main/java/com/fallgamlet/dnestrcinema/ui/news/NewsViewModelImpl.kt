package com.fallgamlet.dnestrcinema.ui.news

import androidx.lifecycle.ViewModel
import com.fallgamlet.dnestrcinema.app.AppFacade
import com.fallgamlet.dnestrcinema.domain.models.NewsItem
import com.fallgamlet.dnestrcinema.utils.LiveDataUtils
import com.fallgamlet.dnestrcinema.utils.reactive.mapTrue
import com.fallgamlet.dnestrcinema.utils.reactive.schedulersIoToUi
import com.fallgamlet.dnestrcinema.utils.reactive.subscribeDisposable


class NewsViewModelImpl : ViewModel() {

    val viewState = NewsViewState()
    private var newsItems: List<NewsItem> = emptyList()

    init {
        loadData()
    }

    fun loadData() {
        val client = AppFacade.instance.netClient ?: return

        viewState.loading.value = true

        client.news
            .schedulersIoToUi()
            .doOnNext { newsItems = it }
            .doOnError { viewState.error.value = it }
            .mapTrue()
            .doOnComplete {
                viewState.items.value = newsItems
                viewState.loading.value = false
            }
            .subscribeDisposable()
    }

    fun refreshViewState() {
        LiveDataUtils.refreshSignal(
            viewState.error,
            viewState.loading
        )

        viewState.items.value = newsItems
    }

}
