package com.fallgamlet.dnestrcinema.ui.news.navigation

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.fallgamlet.dnestrcinema.ui.navigation.destinations.NewsesDestination
import com.fallgamlet.dnestrcinema.ui.news.NewsViewModel
import com.fallgamlet.dnestrcinema.ui.news.composable.NewsListScreen


fun NavGraphBuilder.newsListNavigation(
    viewModelFactory: () -> ViewModelProvider.Factory,
) {
    composable<NewsesDestination> {
        val viewModel: NewsViewModel = viewModel(factory = viewModelFactory())
        val dataState = viewModel.dataState.collectAsStateWithLifecycle()
        NewsListScreen(
            newsList = dataState.value.items,
            isRefreshing = dataState.value.isRefreshing,
            onRefresh = { viewModel.loadData() },
        )
    }
}
