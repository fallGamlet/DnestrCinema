package com.fallgamlet.dnestrcinema.factory;

import com.fallgamlet.dnestrcinema.mvp.factory.ConfigFactory;
import com.fallgamlet.dnestrcinema.mvp.factory.MvpNavigationCreator;
import com.fallgamlet.dnestrcinema.mvp.factory.MvpPresenterFactory;
import com.fallgamlet.dnestrcinema.mvp.factory.MvpViewFragmentFactory;
import com.fallgamlet.dnestrcinema.mvp.models.CinemaItem;
import com.fallgamlet.dnestrcinema.mvp.models.NavigationItem;
import com.fallgamlet.dnestrcinema.network.MapperFactory;
import com.fallgamlet.dnestrcinema.network.RequestFactory;
import com.fallgamlet.dnestrcinema.network.kinotir.KinotirMapperFactory;
import com.fallgamlet.dnestrcinema.network.kinotir.KinotirRequestFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fallgamlet on 16.07.17.
 */

public class KinotirConfigFactory implements ConfigFactory {
    @Override
    public CinemaItem getCinema() {
        CinemaItem item = new CinemaItem();
        item.setId(1);
        item.setName("к.Тирасполь");

        return item;
    }

    @Override
    public List<Integer> getNavigations() {
        List<Integer> set = new ArrayList<>();
        set.add(NavigationItem.NavigationId.TODAY);
        set.add(NavigationItem.NavigationId.SOON);
        set.add(NavigationItem.NavigationId.TICKETS);
        set.add(NavigationItem.NavigationId.NEWS);
        set.add(NavigationItem.NavigationId.ABOUT);

        return set;
    }

    @Override
    public MvpPresenterFactory getPresenterFactory() {
        return new KinotirPresenterFactory();
    }

    @Override
    public MvpViewFragmentFactory getViewFragmentFactory() {
        return new KinotirViewFragmentFactory();
    }

    @Override
    public MvpNavigationCreator getNavigationCreator() {
        return new NavigationCreatorImpl();
    }

    @Override
    public RequestFactory getRequestFactory() {
        return new KinotirRequestFactory();
    }

    @Override
    public MapperFactory getMapperFactory() {
        return new KinotirMapperFactory();
    }
}
