package com.fallgamlet.dnestrcinema.ui.movie.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fallgamlet.dnestrcinema.domain.repositories.remote.FilmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
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
        viewModelScope.launch(Dispatchers.IO) {
            flow { emit(filmRepository.getDetails(movieLink)) }
                .map(MovieDetailsVoMapper::map)
                .onEach { movie ->
                    movieStateVo.value = movie
                }
                .onStart {
                    isRefreshingMutableState.value = true
                }
                .onCompletion {
                    isRefreshingMutableState.value = false
                }
                .catch { error ->
                    viewModelScope.launch {
                        errorChannel.send(error)
                    }
                }
                .collect()
        }
    }
}
