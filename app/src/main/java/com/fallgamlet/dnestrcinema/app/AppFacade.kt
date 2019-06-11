package com.fallgamlet.dnestrcinema.app

import com.fallgamlet.dnestrcinema.data.DummyCinemaRepository
import com.fallgamlet.dnestrcinema.domain.models.AccountItem
import com.fallgamlet.dnestrcinema.domain.models.CinemaItem
import com.fallgamlet.dnestrcinema.domain.models.NavigationItem
import com.fallgamlet.dnestrcinema.mvp.factory.ConfigFactory
import com.fallgamlet.dnestrcinema.mvp.factory.MvpNavigationCreator
import com.fallgamlet.dnestrcinema.mvp.factory.MvpPresenterFactory
import com.fallgamlet.dnestrcinema.mvp.factory.MvpViewFragmentFactory
import com.fallgamlet.dnestrcinema.mvp.routers.NavigationRouter
import com.fallgamlet.dnestrcinema.data.network.MapperFactory
import com.fallgamlet.dnestrcinema.data.network.NetClient
import com.fallgamlet.dnestrcinema.data.network.RequestFactory
import com.fallgamlet.dnestrcinema.domain.CinemaInteractor
import com.fallgamlet.dnestrcinema.domain.CinemaRepository
import com.fallgamlet.dnestrcinema.domain.KinoTirCinemaInteractor

import java.util.ArrayList


class AppFacade private constructor() {

    companion object {
        val instance: AppFacade = AppFacade()
    }


    private var cinemaInteractor: CinemaInteractor = KinoTirCinemaInteractor(DummyCinemaRepository())


    fun init(cinemaRepository: CinemaRepository) {
        cinemaInteractor = KinoTirCinemaInteractor(cinemaRepository)
    }

    fun init(configFactory: ConfigFactory) {
        this.cinemaItem = configFactory.cinema
        this.navigations = configFactory.navigations
        this.presenterFactory = configFactory.presenterFactory
        this.fragmentFactory = configFactory.viewFragmentFactory
        this.navigationCreator = configFactory.navigationCreator
        this.requestFactory = configFactory.requestFactory
        this.mapperFactory = configFactory.mapperFactory
        this.netClient = NetClient(this.requestFactory, this.mapperFactory)
    }


    fun getCinemaInteractor(): CinemaInteractor {
        return cinemaInteractor
    }




    var navigationRouter: NavigationRouter? = null
    var netClient: NetClient? = null
        private set
    var accountItem: AccountItem = AccountItem()
    var cinemaItem: CinemaItem? = null
        private set
    var navigations: List<Int>? = null
        private set
    var presenterFactory: MvpPresenterFactory? = null
        private set
    var fragmentFactory: MvpViewFragmentFactory? = null
        private set
    var navigationCreator: MvpNavigationCreator? = null
        private set
    var requestFactory: RequestFactory? = null
        private set
    var mapperFactory: MapperFactory? = null
        private set

    private val isCanCreateNavigations: Boolean
        get() = this.navigations != null && this.navigationCreator != null

    fun createNavigations(): List<NavigationItem> {
        val items = ArrayList<NavigationItem>()

        if (!isCanCreateNavigations) {
            return items
        }

        for (navigationId in this.navigations!!) {
            val item = this.navigationCreator!!.getNavigationItem(navigationId)

            if (item != null) {
                items.add(item)
            }
        }

        return items
    }


}
