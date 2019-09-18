package com.fallgamlet.dnestrcinema.mvp.presenters;

import com.fallgamlet.dnestrcinema.domain.models.MovieItem;
import com.fallgamlet.dnestrcinema.mvp.views.MvpSoonView;

/**
 * Created by fallgamlet on 03.07.17.
 */
@Deprecated
public interface MvpSoonPresenter extends MvpPresenter<MvpSoonView> {

    void onRefresh();

    void onMovieSelected(MovieItem movieItem);

}
