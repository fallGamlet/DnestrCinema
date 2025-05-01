package com.fallgamlet.dnestrcinema.factory

import com.fallgamlet.dnestrcinema.data.network.RequestFactory
import com.fallgamlet.dnestrcinema.data.network.kinotir.KinotirRequestFactory
import com.fallgamlet.dnestrcinema.mvp.factory.ConfigFactory

class KinotirConfigFactory : ConfigFactory {

    override fun getRequestFactory(): RequestFactory {
        return KinotirRequestFactory()
    }
}
