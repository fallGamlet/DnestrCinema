package com.fallgamlet.dnestrcinema.mvp.views;

import com.fallgamlet.dnestrcinema.domain.models.MovieItem;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpTodayPresenter;

import java.util.List;

/**
 * Created by fallgamlet on 03.07.17.
 */

public interface MvpTodayView extends MvpView<MvpTodayPresenter> {

    void showLoading();

    void hideLoading();

    void showData(List<MovieItem> items);

    void setContentVisible(boolean v);

    void setNoContentVisible(boolean v);

}
