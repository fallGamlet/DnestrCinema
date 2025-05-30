package com.fallgamlet.dnestrcinema.ui.movie.soon.navigation

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.fallgamlet.dnestrcinema.ui.movie.composable.MoviesComposable
import com.fallgamlet.dnestrcinema.ui.movie.soon.SoonMoviesViewModel
import com.fallgamlet.dnestrcinema.ui.navigation.destinations.SoonMoviesDestination

fun NavGraphBuilder.soonMoviesNavigation(
    viewModelFactory: () -> ViewModelProvider.Factory,
) {
    composable<SoonMoviesDestination> {
        val viewModel: SoonMoviesViewModel = viewModel(factory = viewModelFactory())
        val movies = viewModel.moviesVoState.collectAsState(emptyList())
        val isRefreshing = viewModel.isRefreshingState.collectAsState(false)
        MoviesComposable(
            movies = movies.value,
            isRefreshing = isRefreshing.value,
            onRefresh = {
                viewModel.loadData()
            },
            onClick = {
                viewModel.onMovieSelected(it.link)
            }
        )
    }
}
