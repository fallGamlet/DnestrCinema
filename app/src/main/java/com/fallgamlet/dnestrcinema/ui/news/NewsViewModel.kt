package com.fallgamlet.dnestrcinema.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fallgamlet.dnestrcinema.domain.repositories.remote.NewsRepository
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

class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private var cachedNewsVoList: List<NewsVo> = emptyList()

    private val stateVo = MutableStateFlow(NewsListScreenState())
    val dataState = stateVo.asStateFlow()

    private val errorChannel = Channel<Throwable?>(Channel.BUFFERED)
    val errorsState = errorChannel.receiveAsFlow()

    init {
        loadData()
    }

    @Suppress("UnstableApiUsage")
    fun loadData() {
        newsRepository.getItems()
            .doOnSuccess { items ->
                cachedNewsVoList = items.map(NewsVoMapper::mapNews)
            }
            .doOnSubscribe {
                stateVo.update { state ->
                    state.copy(isRefreshing = true)
                }
            }
            .schedulersIoToUi()
            .doOnError {
                viewModelScope.launch {
                    errorChannel.send(it)
                }
            }
            .mapTrue()
            .doOnTerminate {
                stateVo.update { state ->
                    state.copy(
                        isRefreshing = false,
                        items = cachedNewsVoList,
                    )
                }
            }
            .subscribeDisposable()
    }
}
