package com.fallgamlet.dnestrcinema.factory

import com.fallgamlet.dnestrcinema.data.network.RequestFactory
import com.fallgamlet.dnestrcinema.data.network.kinotir.KinotirRequestFactory
import com.fallgamlet.dnestrcinema.domain.models.NavigationItem
import com.fallgamlet.dnestrcinema.mvp.factory.ConfigFactory
import com.fallgamlet.dnestrcinema.mvp.factory.MvpNavigationCreator
import com.fallgamlet.dnestrcinema.mvp.factory.MvpViewFragmentFactory

class KinotirConfigFactory : ConfigFactory {

    override fun getNavigations(): List<Int> {
        return listOf(
            NavigationItem.NavigationId.TODAY,
            NavigationItem.NavigationId.SOON,
            NavigationItem.NavigationId.NEWS,
            NavigationItem.NavigationId.ABOUT
        )
    }

    override fun getViewFragmentFactory(): MvpViewFragmentFactory {
        return KinotirViewFragmentFactory()
    }

    override fun getNavigationCreator(): MvpNavigationCreator {
        return NavigationCreatorImpl()
    }

    override fun getRequestFactory(): RequestFactory {
        return KinotirRequestFactory()
    }
}
