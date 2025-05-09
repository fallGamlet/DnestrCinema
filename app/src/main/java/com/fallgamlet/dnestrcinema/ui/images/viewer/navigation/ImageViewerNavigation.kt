package com.fallgamlet.dnestrcinema.ui.images.viewer.navigation

import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.fallgamlet.dnestrcinema.ui.images.viewer.ImageViewerScreen
import com.fallgamlet.dnestrcinema.ui.images.viewer.ImageViewerVo
import com.fallgamlet.dnestrcinema.ui.navigation.destinations.ImageViewerDestination


fun NavGraphBuilder.imageViewerNavigation(
    navController: NavController,
    viewModelFactory: () -> ViewModelProvider.Factory,
) {
    composable<ImageViewerDestination> { backStackEntry ->
        val args: ImageViewerDestination = backStackEntry.toRoute()
        val state = remember {
            ImageViewerVo(
                imageUrl = args.link,
            )
        }
        ImageViewerScreen(
            state = state,
            backAction = { navController.popBackStack() },
        )
    }
}
