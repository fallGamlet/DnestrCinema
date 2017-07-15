package com.fallgamlet.dnestrcinema.mvp.models;

import com.fallgamlet.dnestrcinema.mvp.factory.MvpNavigationCreator;
import com.fallgamlet.dnestrcinema.mvp.factory.MvpPresenterFactory;
import com.fallgamlet.dnestrcinema.mvp.factory.MvpViewFragmentFactory;
import com.fallgamlet.dnestrcinema.mvp.factory.ConfigFactory;
import com.fallgamlet.dnestrcinema.network.MapperFactory;
import com.fallgamlet.dnestrcinema.network.RequestFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by fallgamlet on 15.07.17.
 */

public class Config {

    private static Config settings;

    private AccountItem accountItem;
    private CinemaItem cinemaItem;
    private Set<Integer> navigations;
    private MvpPresenterFactory presenterFactory;
    private MvpViewFragmentFactory fragmentFactory;
    private MvpNavigationCreator navigationCreator;
    private RequestFactory requestFactory;
    private MapperFactory mapperFactory;


    public static synchronized Config getInstance() {
        if (settings == null) {
            settings = new Config();
        }
        return settings;
    }


    private Config() {
        accountItem = new AccountItem();
    }

    public void init(ConfigFactory configFactory) {
        this.cinemaItem = configFactory.getCinema();
        this.navigations = configFactory.getNavigations();
        this.presenterFactory = configFactory.getPresenterFactory();
        this.fragmentFactory = configFactory.getViewFragmentFactory();
        this.navigationCreator = configFactory.getNavigationCreator();
        this.requestFactory = configFactory.getRequestFactory();
        this.mapperFactory = configFactory.getMapperFactory();
    }


    public MvpPresenterFactory getPresenterFactory() {
        return presenterFactory;
    }

    public MvpViewFragmentFactory getFragmentsFactory() {
        return fragmentFactory;
    }

    public Set<Integer> getNavigations() {
        return navigations;
    }

    public List<NavigationItem> createNavigations() {
        List<NavigationItem> items = new ArrayList<>();

        if (isCanCreateNavigations()) {
            return items;
        }

        for (Integer navigationId: this.navigations) {
            NavigationItem item = this.navigationCreator.getNavigationItem(navigationId);

            if (item != null) {
                items.add(item);
            }
        }

        return items;
    }

    private boolean isCanCreateNavigations() {
        return this.navigations != null && this.navigationCreator != null;
    }

    public RequestFactory getRequestFactory() {
        return requestFactory;
    }

    public MapperFactory getMapperFactory() {
        return mapperFactory;
    }

    public CinemaItem getCinemaItem() {
        return cinemaItem;
    }

    public AccountItem getAccountItem() {
        return accountItem;
    }

    public void setAccountItem(AccountItem accountItem) {
        this.accountItem = accountItem;
    }
}
