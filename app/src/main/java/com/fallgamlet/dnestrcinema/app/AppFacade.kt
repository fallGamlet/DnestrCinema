package com.fallgamlet.dnestrcinema.app

import com.fallgamlet.dnestrcinema.data.network.RequestFactory
import com.fallgamlet.dnestrcinema.domain.models.CinemaItem
import com.fallgamlet.dnestrcinema.domain.models.NavigationItem
import com.fallgamlet.dnestrcinema.mvp.factory.ConfigFactory
import com.fallgamlet.dnestrcinema.mvp.factory.MvpNavigationCreator
import com.fallgamlet.dnestrcinema.mvp.factory.MvpViewFragmentFactory
import com.fallgamlet.dnestrcinema.mvp.routers.NavigationRouter


class AppFacade private constructor() {

    companion object {
        val instance: AppFacade by lazy { AppFacade() }
    }

    fun init(configFactory: ConfigFactory) {
        this.cinemaItem = configFactory.cinema
        this.navigations = configFactory.navigations
        this.fragmentFactory = configFactory.viewFragmentFactory
        this.navigationCreator = configFactory.navigationCreator
        this.requestFactory = configFactory.requestFactory
    }


    var navigationRouter: NavigationRouter? = null
    var cinemaItem: CinemaItem? = null
        private set
    var navigations: List<Int> = emptyList()
        private set
    var fragmentFactory: MvpViewFragmentFactory? = null
        private set
    private var navigationCreator: MvpNavigationCreator? = null
    var requestFactory: RequestFactory? = null
        private set

    fun createNavigations(): List<NavigationItem> {
        val items = mutableListOf<NavigationItem>()

        val navigationCreator = navigationCreator ?: return items

        for (navigationId in this.navigations) {
            val item = navigationCreator.getNavigationItem(navigationId)
            if (item != null) {
                items.add(item)
            }
        }

        return items
    }


}
