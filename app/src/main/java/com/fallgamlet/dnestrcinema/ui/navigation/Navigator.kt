package com.fallgamlet.dnestrcinema.ui.navigation

import androidx.navigation.NavController

interface Navigator {
    suspend fun pushAction(action: (navController: NavController) -> Unit)
}
