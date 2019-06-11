package com.fallgamlet.dnestrcinema.mvp.views;

import com.fallgamlet.dnestrcinema.domain.models.TicketItem;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpTicketsPresenter;

import java.util.List;

/**
 * Created by fallgamlet on 03.07.17.
 */

public interface MvpTicketsView extends MvpView<MvpTicketsPresenter> {

    void showLoading();

    void hideLoading();

    void showData(List<TicketItem> items);

    void setContentVisible(boolean v);

    void setNoContentVisible(boolean v);

}
