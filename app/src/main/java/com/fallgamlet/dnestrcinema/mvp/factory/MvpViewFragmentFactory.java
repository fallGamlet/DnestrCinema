package com.fallgamlet.dnestrcinema.mvp.factory;

import androidx.fragment.app.Fragment;


public interface MvpViewFragmentFactory {

    Fragment createTodayView();

    Fragment createSoonView();

    Fragment createNewsView();

    Fragment createAboutView();

}
