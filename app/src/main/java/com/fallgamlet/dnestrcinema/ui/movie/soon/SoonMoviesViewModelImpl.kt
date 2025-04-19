package com.fallgamlet.dnestrcinema.ui.movie.soon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fallgamlet.dnestrcinema.app.AppFacade
import com.fallgamlet.dnestrcinema.domain.models.MovieItem
import com.fallgamlet.dnestrcinema.mvp.routers.NavigationRouter
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

class SoonMoviesViewModelImpl : ViewModel() {

    private var movies: List<MovieItem> = emptyList()

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
        val client = AppFacade.instance.netClient ?: return

        client.soonMovies
            .schedulersIoToUi()
            .doOnNext {
                movies = it
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
            .doOnTerminate {
                moviesVoMutableState.value = movies
                    .map(MovieVoMapper::map)
                isRefreshingMutableState.value = false
            }
            .subscribeDisposable()
    }

    fun onMovieSelected(link: String) {
        val movie = movies.firstOrNull { it.link == link }
            ?: return

        router?.showMovieDetail(movie)
    }
}
