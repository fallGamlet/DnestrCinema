package com.fallgamlet.dnestrcinema.ui.start

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.fallgamlet.dnestrcinema.ui.about.navigation.aboutNavigation
import com.fallgamlet.dnestrcinema.ui.images.viewer.navigation.imageViewerNavigation
import com.fallgamlet.dnestrcinema.ui.movie.detail.navigation.movieDetailsNavigation
import com.fallgamlet.dnestrcinema.ui.movie.soon.navigation.soonMoviesNavigation
import com.fallgamlet.dnestrcinema.ui.movie.today.navigation.todayMoviesNavigation
import com.fallgamlet.dnestrcinema.ui.navigation.NavigationActionsHolder
import com.fallgamlet.dnestrcinema.ui.navigation.TopLevelRoute
import com.fallgamlet.dnestrcinema.ui.news.navigation.newsListNavigation

@Composable
fun AppScreen(
    viewModelFactory: ViewModelProvider.Factory,
    navItems: List<TopLevelRoute>,
    navigationActionsHolder: NavigationActionsHolder,
) {
    val navController = rememberNavController()
    val getViewModelFactory: () -> ViewModelProvider.Factory = remember {
        { viewModelFactory }
    }
    val navigationSelectedItem = remember { mutableIntStateOf(0) }
    val colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()

    LaunchedEffect(navigationActionsHolder) {
        navigationActionsHolder.actions
            .collect { action ->
                action(navController)
            }
    }

    MaterialTheme(colorScheme = colorScheme) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.systemBars,
            bottomBar = {
                AppBottomBar(
                    navItems = navItems,
                    navigationSelectedItem = navigationSelectedItem,
                    navController = navController,
                )
            }
        ) { innerPadding ->
            AppNavHost(
                startDestination = navItems.first().destination,
                navController = navController,
                innerPadding = innerPadding,
                getViewModelFactory = getViewModelFactory,
            )
        }
    }
}

@Composable
private fun AppBottomBar(
    navItems: List<TopLevelRoute>,
    navigationSelectedItem: MutableIntState,
    navController: NavHostController,
) {
    NavigationBar {
        navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == navigationSelectedItem.intValue,
                label = {
                    Text(item.title)
                },
                icon = {
                    Icon(
                        painter = painterResource(item.iconResId),
                        contentDescription = item.title,
                    )
                },
                onClick = {
                    navigationSelectedItem.intValue = index
                    navController.navigate(route = item.destination) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}

@Composable
private fun AppNavHost(
    startDestination: Any,
    navController: NavHostController,
    innerPadding: PaddingValues,
    getViewModelFactory: () -> ViewModelProvider.Factory,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(paddingValues = innerPadding)
    ) {
        todayMoviesNavigation(
            viewModelFactory = getViewModelFactory,
        )
        soonMoviesNavigation(
            viewModelFactory = getViewModelFactory,
        )
        movieDetailsNavigation(
            navController = navController,
            viewModelFactory = getViewModelFactory,
        )
        newsListNavigation(
            viewModelFactory = getViewModelFactory,
        )
        aboutNavigation(
            navController = navController,
            viewModelFactory = getViewModelFactory,
        )
        imageViewerNavigation(
            navController = navController,
            viewModelFactory = getViewModelFactory,
        )
    }
}
