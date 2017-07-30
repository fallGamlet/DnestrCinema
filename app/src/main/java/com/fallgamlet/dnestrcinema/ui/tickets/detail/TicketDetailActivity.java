package com.fallgamlet.dnestrcinema.ui.tickets.detail;

import android.os.Bundle;

import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpTicketDetailPresenter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpBaseActivity;
import com.fallgamlet.dnestrcinema.mvp.views.MvpTicketDetailView;

public class TicketDetailActivity
        extends
            MvpBaseActivity<MvpTicketDetailPresenter>
        implements
            MvpTicketDetailView
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);
    }





    @Override
    public void setOrder(CharSequence value) {

    }

    @Override
    public void setStatus(CharSequence value) {

    }

    @Override
    public void setRoom(CharSequence value) {

    }

    @Override
    public void setDate(CharSequence value) {

    }

    @Override
    public void setTime(CharSequence value) {

    }

    @Override
    public void setOrderVisible(boolean value) {

    }

    @Override
    public void setStatusVisible(boolean value) {

    }

    @Override
    public void setTitleVisible(boolean value) {

    }

    @Override
    public void setRoomVisible(boolean value) {

    }

    @Override
    public void setDateVisible(boolean value) {

    }

    @Override
    public void setTimeVisible(boolean value) {

    }

}
