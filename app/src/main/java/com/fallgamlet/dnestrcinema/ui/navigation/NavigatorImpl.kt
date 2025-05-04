package com.fallgamlet.dnestrcinema.ui.navigation

import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class NavigatorImpl @Inject constructor() : Navigator, NavigationActionsHolder {
    private val navActions = MutableSharedFlow<(NavController) -> Unit>()

    override suspend fun pushAction(action: (navController: NavController) -> Unit) {
        navActions.emit(action)
    }

    override val actions = navActions.asSharedFlow()
}
