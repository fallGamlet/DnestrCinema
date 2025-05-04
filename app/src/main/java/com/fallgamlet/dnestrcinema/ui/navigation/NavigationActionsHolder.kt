package com.fallgamlet.dnestrcinema.ui.navigation

import androidx.navigation.NavController
import kotlinx.coroutines.flow.Flow

interface NavigationActionsHolder {
    val actions: Flow<(navController: NavController) -> Unit>
}
