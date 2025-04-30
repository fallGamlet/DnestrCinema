package com.fallgamlet.dnestrcinema.ui.movie.today.navigation

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.fallgamlet.dnestrcinema.ui.movie.composable.MoviesComposable
import com.fallgamlet.dnestrcinema.ui.movie.today.TodayMoviesViewModel
import com.fallgamlet.dnestrcinema.ui.navigation.RouteDestination

fun NavGraphBuilder.todayMoviesNavigation(
    navController: NavController,
    viewModelFactory: () -> ViewModelProvider.Factory,
) {
    composable(route = RouteDestination.TodayMovies.route) {
        val viewModel: TodayMoviesViewModel = viewModel(factory = viewModelFactory())
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
