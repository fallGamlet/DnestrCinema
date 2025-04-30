package com.fallgamlet.dnestrcinema.mvp.factory;

import com.fallgamlet.dnestrcinema.data.network.RequestFactory;

import java.util.List;

/**
 * Created by fallgamlet on 16.07.17.
 */

public interface ConfigFactory {

    List<Integer> getNavigations();

    MvpViewFragmentFactory getViewFragmentFactory();

    MvpNavigationCreator getNavigationCreator();

    RequestFactory getRequestFactory();

}
