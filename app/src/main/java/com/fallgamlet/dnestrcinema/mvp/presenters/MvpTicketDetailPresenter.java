package com.fallgamlet.dnestrcinema.mvp.presenters;

import com.fallgamlet.dnestrcinema.mvp.models.TicketItem;
import com.fallgamlet.dnestrcinema.mvp.views.MvpTicketDetailView;

/**
 * Created by fallgamlet on 30.07.17.
 */

public interface MvpTicketDetailPresenter
        extends MvpPresenter <MvpTicketDetailView>
{

    void onMoviePressed();

    void setData(TicketItem ticketItem);

}
