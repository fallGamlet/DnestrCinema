package com.fallgamlet.dnestrcinema.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fallgamlet.dnestrcinema.app.AppFacade
import com.fallgamlet.dnestrcinema.domain.models.NewsItem
import com.fallgamlet.dnestrcinema.utils.reactive.mapTrue
import com.fallgamlet.dnestrcinema.utils.reactive.schedulersIoToUi
import com.fallgamlet.dnestrcinema.utils.reactive.subscribeDisposable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsViewModelImpl @Inject constructor() : ViewModel() {

    private var newsItems: List<NewsItem> = emptyList()

    private val stateVo = MutableStateFlow(NewsListScreenState())
    val dataState = stateVo.asStateFlow()

    private val errorChannel = Channel<Throwable?>(Channel.BUFFERED)
    val errorsState = errorChannel.receiveAsFlow()

    init {
        loadData()
    }

    fun loadData() {
        val client = AppFacade.instance.netClient ?: return


        client.news
            .doOnNext { newsItems = it }
            .schedulersIoToUi()
            .doOnError {
                viewModelScope.launch {
                    errorChannel.send(it)
                }
            }
            .mapTrue()
            .doOnSubscribe {
                stateVo.update { state ->
                    state.copy(isRefreshing = true)
                }
            }
            .doOnTerminate {
                stateVo.update { state ->
                    state.copy(
                        isRefreshing = false,
                        items = newsItems.map(NewsVoMapper::mapNews),
                    )
                }
            }
            .subscribeDisposable()
    }
}
