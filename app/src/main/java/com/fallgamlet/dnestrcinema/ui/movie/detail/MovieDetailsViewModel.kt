package com.fallgamlet.dnestrcinema.ui.movie.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fallgamlet.dnestrcinema.domain.repositories.remote.FilmRepository
import com.fallgamlet.dnestrcinema.utils.reactive.schedulersIoToUi
import com.fallgamlet.dnestrcinema.utils.reactive.subscribeDisposable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class MovieDetailsViewModel @Inject constructor(
    private val filmRepository: FilmRepository
) : ViewModel() {

    private val movieStateVo = MutableStateFlow(MovieDetailsVo())
    val movieState = movieStateVo.asStateFlow()

    private val isRefreshingMutableState = MutableStateFlow(false)
    val isRefreshingState = isRefreshingMutableState.asStateFlow()

    private val errorChannel = Channel<Throwable?>(Channel.BUFFERED)
    val errorsState = errorChannel.receiveAsFlow()

    fun loadData(movieLink: String) {
        filmRepository.getDetails(movieLink)
            .map(MovieDetailsVoMapper::map)
            .doOnSuccess { movie ->
                movieStateVo.value = movie
            }
            .doOnSubscribe {
                isRefreshingMutableState.value = true
            }
            .doOnError { error ->
                viewModelScope.launch {
                    errorChannel.send(error)
                }
            }
            .doAfterTerminate {
                isRefreshingMutableState.value = false
            }
            .schedulersIoToUi()
            .subscribeDisposable()
    }
}
