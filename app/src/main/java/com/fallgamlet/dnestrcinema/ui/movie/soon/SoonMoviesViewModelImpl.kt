package com.fallgamlet.dnestrcinema.ui.movie.soon

import androidx.lifecycle.ViewModel
import com.fallgamlet.dnestrcinema.app.AppFacade
import com.fallgamlet.dnestrcinema.domain.models.MovieItem
import com.fallgamlet.dnestrcinema.mvp.routers.NavigationRouter
import com.fallgamlet.dnestrcinema.utils.LiveDataUtils
import com.fallgamlet.dnestrcinema.utils.reactive.mapTrue
import com.fallgamlet.dnestrcinema.utils.reactive.schedulersIoToUi
import com.fallgamlet.dnestrcinema.utils.reactive.subscribeDisposable


class SoonMoviesViewModelImpl : ViewModel() {

    val viewState = SoonMoviesViewState()
    private var movies: List<MovieItem> = emptyList()

    private val router: NavigationRouter?
        get() = AppFacade.instance.navigationRouter


    init {
        loadData()
    }

    fun loadData() {
        val client = AppFacade.instance.netClient ?: return

        viewState.loading.value = true

        client.soonMovies
            .schedulersIoToUi()
            .doOnNext {
                movies = it
            }
            .doOnError { viewState.error.value = it }
            .mapTrue()
            .doOnComplete {
                viewState.movies.value = movies
                viewState.loading.value = false
            }
            .subscribeDisposable()
    }

    fun refreshViewState() {
        LiveDataUtils.refreshSignal(
            viewState.error,
            viewState.loading
        )

        viewState.movies.value = movies
    }

    fun onMovieSelected(movieItem: MovieItem) {
        router?.showMovieDetail(movieItem)
    }
}
