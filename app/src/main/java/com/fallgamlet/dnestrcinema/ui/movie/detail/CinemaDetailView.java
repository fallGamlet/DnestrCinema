package com.fallgamlet.dnestrcinema.ui.movie.detail;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.fallgamlet.dnestrcinema.mvp.views.MvpView;

import java.util.Collection;
import java.util.Date;

/**
 * Created by fallgamlet on 09.04.17.
 */

public interface CinemaDetailView extends MvpView <CinemaDetailPresenter> {
    void setImageAdapter(RecyclerView.Adapter adapter);
    ImageView getPosterImageView();
    void setPosterImage(Drawable drawable);
    void setTitle(CharSequence title);
    void setPubDate(Date date);
    void setRooms(Collection<? extends CharSequence> rooms);

    void showImages(boolean v);
    void showBuyTicketButton(boolean v);
    void showTrailerButton(boolean v);
    void showDuration(boolean v);
    void showGenre(boolean v);
    void showAgeLimit(boolean v);
    void showCountry(boolean v);
    void showDirector(boolean v);
    void showScenario(boolean v);
    void showActors(boolean v);
    void showBudget(boolean v);
    void showDescription(boolean v);

    void setDuration(CharSequence v);
    void setGenre(CharSequence v);
    void setAgeLimit(CharSequence v);
    void setCountry(CharSequence v);
    void setDirector(CharSequence v);
    void setScenario(CharSequence v);
    void setActors(CharSequence v);
    void setBudget(CharSequence v);
    void setDescription(CharSequence v);
}
