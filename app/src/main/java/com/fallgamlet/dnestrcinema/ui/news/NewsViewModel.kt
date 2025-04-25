package com.fallgamlet.dnestrcinema.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fallgamlet.dnestrcinema.domain.repositories.remote.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
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

    fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            flow { emit(newsRepository.getItemsSuspend()) }
                .onEach { items ->
                    cachedNewsVoList = items.map(NewsVoMapper::mapNews)
                }
                .onStart {
                    stateVo.update { state ->
                        state.copy(isRefreshing = true)
                    }
                }
                .onCompletion {
                    stateVo.update { state ->
                        state.copy(
                            isRefreshing = false,
                            items = cachedNewsVoList,
                        )
                    }
                }
                .catch { error ->
                    errorChannel.send(error)
                }
                .collect()
        }
    }
}
