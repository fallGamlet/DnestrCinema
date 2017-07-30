package com.fallgamlet.dnestrcinema.ui.tickets.detail;

import com.fallgamlet.dnestrcinema.mvp.models.TicketItem;
import com.fallgamlet.dnestrcinema.mvp.presenters.BasePresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpTicketDetailPresenter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpTicketDetailView;

/**
 * Created by fallgamlet on 30.07.17.
 */

public class TicketDetailPresenterImpl
        extends
            BasePresenter<MvpTicketDetailView>
        implements
            MvpTicketDetailPresenter
{

    TicketItem ticketItem;


    public TicketDetailPresenterImpl() {

    }


    @Override
    public void onMoviePressed() {

    }

    @Override
    public void setData(TicketItem ticketItem) {

    }

    @Override
    public void bindView(MvpTicketDetailView view) {
        super.bindView(view);

    }

    private void showData() {
        if (!isViewBinded()) {
            return;
        }

        TicketItem ticketItem = getTicket();

        MvpTicketDetailView view = getView();
        view.setTitle(ticketItem.getTitle());
        view.setOrder(ticketItem.getId());
        view.setStatus(ticketItem.getStatus());
        view.setRoom(ticketItem.getRoom());
        view.setDate(ticketItem.getDate());
        view.setTime(ticketItem.getTime());
    }

    private synchronized TicketItem getTicket() {
        if (ticketItem == null) {
            ticketItem = new TicketItem();
        }

        return ticketItem;
    }

}
