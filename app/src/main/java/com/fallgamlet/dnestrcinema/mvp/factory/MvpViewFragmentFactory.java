package com.fallgamlet.dnestrcinema.mvp.factory;

import androidx.fragment.app.Fragment;


public interface MvpViewFragmentFactory {

    Fragment createTodayView();

    Fragment createSoonView();

    Fragment createTicketsView();

    Fragment createLoginView();

    Fragment createNewsView();

    Fragment createAboutView();

}
