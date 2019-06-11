package com.fallgamlet.dnestrcinema.mvp.views;

import com.fallgamlet.dnestrcinema.domain.models.NewsItem;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpNewsPresenter;

import java.util.List;

/**
 * Created by fallgamlet on 03.07.17.
 */

public interface MvpNewsView extends MvpView<MvpNewsPresenter> {

    void showLoading();

    void hideLoading();

    void showData(List<NewsItem> items);

    void setContentVisible(boolean v);

    void setNoContentVisible(boolean v);

}
