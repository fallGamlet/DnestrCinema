package com.fallgamlet.dnestrcinema.dagger

import com.fallgamlet.dnestrcinema.ui.navigation.NavigationActionsHolder
import com.fallgamlet.dnestrcinema.ui.navigation.Navigator
import com.fallgamlet.dnestrcinema.ui.navigation.NavigatorImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NavigationModule {

    @Singleton
    @Provides
    fun provideNavigatorImpl(): NavigatorImpl {
        return NavigatorImpl()
    }

    @Provides
    fun bindNavigator(navigator: NavigatorImpl): Navigator {
        return navigator
    }

    @Provides
    fun bindNavigationActionsHolder(navigator: NavigatorImpl): NavigationActionsHolder {
        return navigator
    }
}
