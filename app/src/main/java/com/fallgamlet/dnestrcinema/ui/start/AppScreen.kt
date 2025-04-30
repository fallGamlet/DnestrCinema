package com.fallgamlet.dnestrcinema.ui.start

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.fallgamlet.dnestrcinema.ui.about.navigation.aboutNavigation
import com.fallgamlet.dnestrcinema.ui.movie.soon.navigation.soonMoviesNavigation
import com.fallgamlet.dnestrcinema.ui.movie.today.navigation.todayMoviesNavigation
import com.fallgamlet.dnestrcinema.ui.navigation.TopLevelRoute
import com.fallgamlet.dnestrcinema.ui.news.navigation.newsListNavigation

@Composable
fun AppScreen(
    viewModelFactory: ViewModelProvider.Factory,
    context: () -> Context,
) {
    val navController = rememberNavController()
    val getViewModelFactory: () -> ViewModelProvider.Factory = remember {
        { viewModelFactory }
    }
    val navigationSelectedItem = remember { mutableStateOf(0) }

    MaterialTheme {

        Scaffold(
//            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar {
                    listOf(
                        TopLevelRoute.SoonMovies,
                        TopLevelRoute.TodayMovies,
                        TopLevelRoute.Newses,
                        TopLevelRoute.About,
                    ).forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = index == navigationSelectedItem.value,
                            label = {
                                Text(item.destination.name)
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(item.iconResId),
                                    contentDescription = item.destination.name,
                                )
                            },
                            onClick = {
                                navigationSelectedItem.value = index
                                navController.navigate(route = item.destination.route) {
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
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = TopLevelRoute.SoonMovies.destination.route,
                modifier = Modifier.padding(paddingValues = innerPadding)
            ) {
                todayMoviesNavigation(
                    navController = navController,
                    viewModelFactory = getViewModelFactory,
                )
                soonMoviesNavigation(
                    navController = navController,
                    viewModelFactory = getViewModelFactory,
                )
                newsListNavigation(
                    navController = navController,
                    viewModelFactory = getViewModelFactory,
                )
                aboutNavigation(
                    navController = navController,
                    viewModelFactory = getViewModelFactory,
                    context = context,
                )
            }
        }
    }
}
