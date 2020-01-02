package com.fallgamlet.dnestrcinema.mvp.views;

import androidx.fragment.app.Fragment;

import com.fallgamlet.dnestrcinema.mvp.presenters.MvpLoginPresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpNavigationPresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpTicketsPresenter;

/**
 * Created by fallgamlet on 10.07.17.
 */

@Deprecated
public interface Fragments {

    abstract class MvpNavigationViewFragment
            extends MvpBaseFragment <MvpNavigationPresenter>
            implements MvpNavigationView {}

    abstract class MvpTicketsViewFragment
            extends MvpBaseFragment<MvpTicketsPresenter>
            implements MvpTicketsView {}

    abstract class MvpLoginViewFragment
            extends MvpBaseFragment<MvpLoginPresenter>
            implements MvpLoginView {}

}
