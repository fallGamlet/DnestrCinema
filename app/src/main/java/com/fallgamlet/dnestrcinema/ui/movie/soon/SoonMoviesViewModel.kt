package com.fallgamlet.dnestrcinema.ui.movie.soon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fallgamlet.dnestrcinema.app.AppFacade
import com.fallgamlet.dnestrcinema.domain.models.Film
import com.fallgamlet.dnestrcinema.domain.repositories.remote.FilmRepository
import com.fallgamlet.dnestrcinema.mvp.routers.NavigationRouter
import com.fallgamlet.dnestrcinema.ui.movie.model.MovieVo
import com.fallgamlet.dnestrcinema.ui.movie.model.MovieVoMapper
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
import kotlinx.coroutines.launch
import javax.inject.Inject

class SoonMoviesViewModel @Inject constructor(
    private val filmRepository: FilmRepository
) : ViewModel() {

    private var films: List<Film> = emptyList()
    private var cachedMovies: List<MovieVo> = emptyList()

    private val moviesVoMutableState = MutableStateFlow(emptyList<MovieVo>())
    val moviesVoState = moviesVoMutableState.asStateFlow()

    private val isRefreshingMutableState = MutableStateFlow(false)
    val isRefreshingState = isRefreshingMutableState.asStateFlow()

    private val errorChannel = Channel<Throwable?>(Channel.BUFFERED)
    val errorsState = errorChannel.receiveAsFlow()

    private val router: NavigationRouter?
        get() = AppFacade.instance.navigationRouter


    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            flow { emit(filmRepository.getSoon()) }
                .onEach {
                    films = it
                    cachedMovies = it.map(MovieVoMapper::map)

                }
                .onStart {
                    isRefreshingMutableState.value = true
                }
                .onCompletion {
                    moviesVoMutableState.value = films
                        .map(MovieVoMapper::map)
                    isRefreshingMutableState.value = false
                }
                .catch {
                    viewModelScope.launch {
                        errorChannel.send(it)
                    }
                }
                .collect()
        }
    }

    fun onMovieSelected(link: String) {
        router?.showMovieDetail(link)
    }
}
