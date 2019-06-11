package com.fallgamlet.dnestrcinema.app;

import com.fallgamlet.dnestrcinema.domain.models.AccountItem;
import com.fallgamlet.dnestrcinema.domain.models.CinemaItem;
import com.fallgamlet.dnestrcinema.domain.models.NavigationItem;
import com.fallgamlet.dnestrcinema.mvp.factory.ConfigFactory;
import com.fallgamlet.dnestrcinema.mvp.factory.MvpNavigationCreator;
import com.fallgamlet.dnestrcinema.mvp.factory.MvpPresenterFactory;
import com.fallgamlet.dnestrcinema.mvp.factory.MvpViewFragmentFactory;
import com.fallgamlet.dnestrcinema.mvp.routers.NavigationRouter;
import com.fallgamlet.dnestrcinema.data.network.MapperFactory;
import com.fallgamlet.dnestrcinema.data.network.NetClient;
import com.fallgamlet.dnestrcinema.data.network.RequestFactory;

import java.util.ArrayList;
import java.util.List;


public class AppFacade {

    private static AppFacade insatance;

    private NavigationRouter navigationRouter;
    private NetClient netClient;
    private AccountItem accountItem;
    private CinemaItem cinemaItem;
    private List<Integer> navigations;
    private MvpPresenterFactory presenterFactory;
    private MvpViewFragmentFactory fragmentFactory;
    private MvpNavigationCreator navigationCreator;
    private RequestFactory requestFactory;
    private MapperFactory mapperFactory;


    public static synchronized AppFacade getInstance() {
        if (insatance == null) {
            insatance = new AppFacade();
        }
        return insatance;
    }


    private AppFacade() {
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
        this.netClient = new NetClient(this.requestFactory, this.mapperFactory);
    }


    public NetClient getNetClient() {
        return this.netClient;
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

    public MvpPresenterFactory getPresenterFactory() {
        return presenterFactory;
    }

    public MvpViewFragmentFactory getFragmentFactory() {
        return fragmentFactory;
    }

    public MvpNavigationCreator getNavigationCreator() {
        return this.navigationCreator;
    }

    public RequestFactory getRequestFactory() {
        return requestFactory;
    }

    public MapperFactory getMapperFactory() {
        return mapperFactory;
    }

    public List<Integer> getNavigations() {
        return navigations;
    }

    public List<NavigationItem> createNavigations() {
        List<NavigationItem> items = new ArrayList<>();

        if (!isCanCreateNavigations()) {
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

    public NavigationRouter getNavigationRouter() {
        return navigationRouter;
    }

    public void setNavigationRouter(NavigationRouter navigationRouter) {
        this.navigationRouter = navigationRouter;
    }
}
