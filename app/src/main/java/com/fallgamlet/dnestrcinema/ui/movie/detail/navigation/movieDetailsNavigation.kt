package com.fallgamlet.dnestrcinema.ui.movie.detail.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.fallgamlet.dnestrcinema.R
import com.fallgamlet.dnestrcinema.ui.movie.detail.MovieDetailsLabels
import com.fallgamlet.dnestrcinema.ui.movie.detail.MovieDetailsScreen
import com.fallgamlet.dnestrcinema.ui.movie.detail.MovieDetailsViewModel
import com.fallgamlet.dnestrcinema.ui.movie.detail.MovieDetailsVo
import com.fallgamlet.dnestrcinema.ui.navigation.destinations.MovieDetailsDestination
import com.fallgamlet.dnestrcinema.utils.IntentUtils

fun NavGraphBuilder.movieDetailsNavigation(
    navController: NavController,
    viewModelFactory: () -> ViewModelProvider.Factory,
) {
    composable<MovieDetailsDestination> { backStackEntry ->
        val args: MovieDetailsDestination = backStackEntry.toRoute()
        val viewModel: MovieDetailsViewModel = viewModel(factory = viewModelFactory())
        val movieState = viewModel.movieState.collectAsState(MovieDetailsVo())
        val labels = getLabels()
        val context = LocalContext.current

        LaunchedEffect(viewModel) {
            viewModel.loadData(movieLink = args.link)
        }

        MovieDetailsScreen(
            movie = movieState.value,
            labels = labels,
            trailerAction = { url ->
                IntentUtils.openUrl(context, url)
            },
            backAction = {
                navController.popBackStack()
            }
        )
    }
}

@Composable
private fun getLabels(): MovieDetailsLabels {
    return MovieDetailsLabels(
        duration = stringResource(R.string.label_duration),
        director = stringResource(R.string.label_director),
        scenario = stringResource(R.string.label_scenario),
        actors = stringResource(R.string.label_actors),
        budget = stringResource(R.string.label_budget),
        ageLimit = stringResource(R.string.label_agelimit),
        country = stringResource(R.string.label_country),
        genre = stringResource(R.string.label_genre),
        trailer = stringResource(R.string.label_trailer),
    )
}
