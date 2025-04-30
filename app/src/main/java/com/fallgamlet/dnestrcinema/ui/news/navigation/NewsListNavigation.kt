package com.fallgamlet.dnestrcinema.ui.news.navigation

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.fallgamlet.dnestrcinema.ui.navigation.RouteDestination
import com.fallgamlet.dnestrcinema.ui.news.NewsViewModel
import com.fallgamlet.dnestrcinema.ui.news.composable.NewsListScreen


fun NavGraphBuilder.newsListNavigation(
    navController: NavController,
    viewModelFactory: () -> ViewModelProvider.Factory,
) {
    composable(route = RouteDestination.Newses.route) {
        val viewModel: NewsViewModel = viewModel(factory = viewModelFactory())
        val dataState = viewModel.dataState.collectAsStateWithLifecycle()
        NewsListScreen(
            newsList = dataState.value.items,
            isRefreshing = dataState.value.isRefreshing,
            onRefresh = { viewModel.loadData() },
        )
    }
}
