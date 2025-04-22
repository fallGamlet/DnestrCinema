package com.fallgamlet.dnestrcinema.ui.movie.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fallgamlet.dnestrcinema.app.AppFacade
import com.fallgamlet.dnestrcinema.domain.models.Film
import com.fallgamlet.dnestrcinema.domain.repositories.remote.FilmRepository
import com.fallgamlet.dnestrcinema.mvp.routers.NavigationRouter
import com.fallgamlet.dnestrcinema.ui.movie.MovieItemMapper
import com.fallgamlet.dnestrcinema.ui.movie.model.MovieVo
import com.fallgamlet.dnestrcinema.ui.movie.model.MovieVoMapper
import com.fallgamlet.dnestrcinema.utils.reactive.mapTrue
import com.fallgamlet.dnestrcinema.utils.reactive.schedulersIoToUi
import com.fallgamlet.dnestrcinema.utils.reactive.subscribeDisposable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TodayMoviesViewModel @Inject constructor(
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
        filmRepository.getToday()
            .doOnSuccess {
                films = it
                cachedMovies = it.map(MovieVoMapper::map)

            }
            .doOnError {
                viewModelScope.launch {
                    errorChannel.send(it)
                }
            }
            .mapTrue()
            .doOnSubscribe {
                isRefreshingMutableState.value = true
            }
            .doAfterTerminate {
                moviesVoMutableState.value = films
                    .map(MovieVoMapper::map)
                isRefreshingMutableState.value = false
            }
            .schedulersIoToUi()
            .subscribeDisposable()
    }

    fun onMovieSelected(link: String) {
        val film = films.firstOrNull { it.link == link }
            ?: return

        val movie = MovieItemMapper.map(film)
        router?.showMovieDetail(movie)
    }
}
