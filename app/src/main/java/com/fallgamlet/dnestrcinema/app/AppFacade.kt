package com.fallgamlet.dnestrcinema.app

import com.fallgamlet.dnestrcinema.data.network.RequestFactory
import com.fallgamlet.dnestrcinema.mvp.factory.ConfigFactory
import com.fallgamlet.dnestrcinema.mvp.routers.NavigationRouter


class AppFacade private constructor() {

    companion object {
        val instance: AppFacade by lazy { AppFacade() }
    }

    fun init(configFactory: ConfigFactory) {
        this.requestFactory = configFactory.requestFactory
    }


    var navigationRouter: NavigationRouter? = null

    var requestFactory: RequestFactory? = null
        private set

}
