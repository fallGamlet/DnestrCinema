package com.fallgamlet.dnestrcinema.mvp.factory;

import com.fallgamlet.dnestrcinema.mvp.models.CinemaItem;
import com.fallgamlet.dnestrcinema.network.MapperFactory;
import com.fallgamlet.dnestrcinema.network.RequestFactory;

import java.util.Set;

/**
 * Created by fallgamlet on 16.07.17.
 */

public interface ConfigFactory {

    CinemaItem getCinema();

    Set<Integer> getNavigations();

    MvpPresenterFactory getPresenterFactory();

    MvpViewFragmentFactory getViewFragmentFactory();

    MvpNavigationCreator getNavigationCreator();

    RequestFactory getRequestFactory();

    MapperFactory getMapperFactory();

}