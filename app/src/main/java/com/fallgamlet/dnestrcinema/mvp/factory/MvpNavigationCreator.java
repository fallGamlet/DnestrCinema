package com.fallgamlet.dnestrcinema.mvp.factory;

import com.fallgamlet.dnestrcinema.domain.models.NavigationItem;

/**
 * Created by fallgamlet on 15.07.17.
 */

public interface MvpNavigationCreator {

    NavigationItem getNavigationItem(int id);

}
