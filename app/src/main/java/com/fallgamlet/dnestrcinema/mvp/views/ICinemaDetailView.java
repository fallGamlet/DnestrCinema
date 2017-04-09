package com.fallgamlet.dnestrcinema.mvp.views;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;

import java.util.Collection;
import java.util.Date;

/**
 * Created by fallgamlet on 09.04.17.
 */

public interface CinemaDetailView extends IView {
    void setImageAdapter(RecyclerView.Adapter adapter);
    void setPosterImage(Drawable drawable);
    void setTitle(CharSequence title);
    void setPubDate(Date date);
    void setRooms(Collection<CharSequence> rooms);

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