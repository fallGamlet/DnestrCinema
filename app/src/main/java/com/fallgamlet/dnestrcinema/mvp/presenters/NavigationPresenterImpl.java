package com.fallgamlet.dnestrcinema.mvp.presenters;

import com.fallgamlet.dnestrcinema.mvp.routers.NavigationRouter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpNavigationView;

/**
 * Created by fallgamlet on 02.07.17.
 */

public class NavigationPresenterImpl
        extends BasePresenter<MvpNavigationView>
        implements NavigationPresenter
{

    private NavigationRouter router;
    private State state;


    public NavigationPresenterImpl(MvpNavigationView view, NavigationRouter router) {
        this.state = new State();

        bindView(view);
        setRouter(router);
    }


    public NavigationRouter getRouter() {
        return router;
    }

    public void setRouter(NavigationRouter router) {
        this.router = router;
    }

    public boolean isRouterBinded() {
        return this.router != null;
    }


    @Override
    public void onTodaySelected() {
        if (isViewBinded()) {
            getView().selectToday();
        }

        if (!this.state.isStateToday()) {
            this.state.setStateToday();

            if (isRouterBinded()) {
                getRouter().showToday();
            }
        }
    }

    @Override
    public void onSoonSelected() {
        if (isViewBinded()) {
            getView().selectSoon();
        }

        if (!this.state.isStateSoon()) {
            this.state.setStateSoon();

            if (isRouterBinded()) {
                getRouter().showSoon();
            }
        }
    }

    @Override
    public void onTicketsSelected() {
        if (isViewBinded()) {
            getView().selectTickets();
        }

        if (!this.state.isStateTickets()) {
            this.state.setStateTickets();

            if (isRouterBinded()) {
                getRouter().showTickets();
            }
        }
    }

    @Override
    public void onAboutSelected() {
        if (isViewBinded()) {
            getView().selectAbout();
        }

        if (!this.state.isStateAbout()) {
            this.state.setStateAbout();

            if (isRouterBinded()) {
                getRouter().showAbout();
            }
        }
    }

    @Override
    public void onNewsSelected() {
        if (isViewBinded()) {
            getView().selectNews();
        }

        if (!this.state.isStateNews()) {
            this.state.setStateNews();

            if (isRouterBinded()) {
                getRouter().showNews();
            }
        }
    }


    private class State {
        private static final int STATE_TODAY = 1;
        private static final int STATE_SOON = 2;
        private static final int STATE_TICKETS = 3;
        private static final int STATE_NEWS = 4;
        private static final int STATE_ABOUT = 5;

        private int value;

        public State() {
            setStateToday();
        }

        public void setStateToday() {
            this.value = STATE_TODAY;
        }

        public void setStateSoon() {
            this.value = STATE_SOON;
        }

        public void setStateTickets() {
            this.value = STATE_TICKETS;
        }

        public void setStateNews() {
            this.value = STATE_NEWS;
        }

        public void setStateAbout() {
            this.value = STATE_ABOUT;
        }

        public boolean isStateToday() {
            return this.value == STATE_TODAY;
        }

        public boolean isStateSoon() {
            return this.value == STATE_SOON;
        }

        public boolean isStateTickets() {
            return this.value == STATE_TICKETS;
        }

        public boolean isStateNews() {
            return this.value == STATE_NEWS;
        }

        public boolean isStateAbout() {
            return this.value == STATE_ABOUT;
        }
    }

}
