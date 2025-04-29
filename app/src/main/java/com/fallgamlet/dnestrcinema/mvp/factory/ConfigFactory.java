package com.fallgamlet.dnestrcinema.mvp.factory;

import com.fallgamlet.dnestrcinema.data.network.RequestFactory;
import com.fallgamlet.dnestrcinema.domain.models.CinemaItem;

import java.util.List;

/**
 * Created by fallgamlet on 16.07.17.
 */

public interface ConfigFactory {

    CinemaItem getCinema();

    List<Integer> getNavigations();

    MvpViewFragmentFactory getViewFragmentFactory();

    MvpNavigationCreator getNavigationCreator();

    RequestFactory getRequestFactory();

}
