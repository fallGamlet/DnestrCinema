package com.fallgamlet.dnestrcinema.mvp.views;

import com.fallgamlet.dnestrcinema.mvp.presenters.MvpAboutPresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpLoginPresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpMovieDetailPresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpNavigationPresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpNewsPresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpSoonPresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpTicketsPresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpTodayPresenter;

/**
 * Created by fallgamlet on 10.07.17.
 */

@Deprecated
public interface Fragments {

    abstract class MvpNavigationViewFragment
            extends MvpBaseFragment <MvpNavigationPresenter>
            implements MvpNavigationView {}

    abstract class MvpAboutViewFragment
            extends MvpBaseFragment<MvpAboutPresenter>
            implements MvpAboutView {}

    abstract class MvpSoonViewFragment
            extends MvpBaseFragment<MvpSoonPresenter>
            implements MvpSoonView {}

    abstract class MvpTodayViewFragment
            extends MvpBaseFragment<MvpTodayPresenter>
            implements MvpTodayView {}

    abstract class MvpMovieDetailViewFragment
            extends MvpBaseFragment<MvpMovieDetailPresenter>
            implements MvpMovieDetailView {}

    abstract class MvpNewsViewFragment
            extends MvpBaseFragment<MvpNewsPresenter>
            implements MvpNewsView {}

    abstract class MvpTicketsViewFragment
            extends MvpBaseFragment<MvpTicketsPresenter>
            implements MvpTicketsView {}

    abstract class MvpLoginViewFragment
            extends MvpBaseFragment<MvpLoginPresenter>
            implements MvpLoginView {}

}
