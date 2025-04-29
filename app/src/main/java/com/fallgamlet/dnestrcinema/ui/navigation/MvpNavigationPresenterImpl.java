package com.fallgamlet.dnestrcinema.ui.navigation;

import com.fallgamlet.dnestrcinema.mvp.presenters.BasePresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpNavigationPresenter;
import com.fallgamlet.dnestrcinema.mvp.routers.NavigationRouter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpNavigationView;


public class MvpNavigationPresenterImpl
        extends BasePresenter<MvpNavigationView>
        implements MvpNavigationPresenter
{

    private NavigationRouter router;
    private State state;

    public MvpNavigationPresenterImpl(MvpNavigationView view, NavigationRouter router) {
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
        if (!this.state.isStateToday()) {
            this.state.setStateToday();

            if (isViewBinded()) {
                getView().selectToday();
            }

            if (isRouterBinded()) {
                getRouter().showToday();
            }
        }
    }

    @Override
    public void onSoonSelected() {
        if (!this.state.isStateSoon()) {
            this.state.setStateSoon();

            if (isViewBinded()) {
                getView().selectSoon();
            }

            if (isRouterBinded()) {
                getRouter().showSoon();
            }
        }
    }

    @Override
    public void onAboutSelected() {
        if (!this.state.isStateAbout()) {
            this.state.setStateAbout();

            if (isViewBinded()) {
                getView().selectAbout();
            }

            if (isRouterBinded()) {
                getRouter().showAbout();
            }
        }
    }

    @Override
    public void onNewsSelected() {
        if (!this.state.isStateNews()) {
            this.state.setStateNews();

            if (isViewBinded()) {
                getView().selectNews();
            }

            if (isRouterBinded()) {
                getRouter().showNews();
            }
        }
    }


    private class State {
        private static final int STATE_TODAY = 1;
        private static final int STATE_SOON = 2;
        private static final int STATE_NEWS = 3;
        private static final int STATE_ABOUT = 4;

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

        public boolean isStateNews() {
            return this.value == STATE_NEWS;
        }

        public boolean isStateAbout() {
            return this.value == STATE_ABOUT;
        }
    }

}
