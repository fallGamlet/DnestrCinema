package com.fallgamlet.dnestrcinema.mvp.views;

import com.fallgamlet.dnestrcinema.mvp.presenters.MvpTicketDetailPresenter;

/**
 * Created by fallgamlet on 30.07.17.
 */

public interface MvpTicketDetailView
        extends MvpView<MvpTicketDetailPresenter>
{

    void setOrder(CharSequence value);

    void setStatus(CharSequence value);

    void setTitle(CharSequence value);

    void setRoom(CharSequence value);

    void setDate(CharSequence value);

    void setTime(CharSequence value);

    void setOrderVisible(boolean value);

    void setStatusVisible(boolean value);

    void setTitleVisible(boolean value);

    void setRoomVisible(boolean value);

    void setDateVisible(boolean value);

    void setTimeVisible(boolean value);

}
