package com.fallgamlet.dnestrcinema.data.network.kinotir;

import com.fallgamlet.dnestrcinema.data.network.RequestFactory;

/**
 * Created by fallgamlet on 09.07.17.
 */

public class KinotirRequestFactory implements RequestFactory {

    private final String scheme = "http";
    private final String domain = "kinotir.md";
    private final String baseUrl = scheme +"://"+ domain;

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }
}
