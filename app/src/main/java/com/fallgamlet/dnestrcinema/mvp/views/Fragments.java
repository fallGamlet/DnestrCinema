package com.fallgamlet.dnestrcinema.mvp.views;

import com.fallgamlet.dnestrcinema.mvp.presenters.MvpNavigationPresenter;

/**
 * Created by fallgamlet on 10.07.17.
 */

@Deprecated
public interface Fragments {

    abstract class MvpNavigationViewFragment
            extends MvpBaseFragment <MvpNavigationPresenter>
            implements MvpNavigationView {}

}
